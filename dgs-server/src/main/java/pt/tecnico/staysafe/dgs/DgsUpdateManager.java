package pt.tecnico.staysafe.dgs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import pt.tecnico.staysafe.dgs.grpc.DgsServiceGrpc;
import pt.tecnico.staysafe.dgs.grpc.*;

import pt.tecnico.staysafe.dgs.update.DgsDebugger;
import pt.tecnico.staysafe.dgs.update.TimestampVetorial;

import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;

// class that is initialized in serverImpl to manage updates
public class DgsUpdateManager {
	private TimestampVetorial _currentTV;
	private String _replicaId;
	private ArrayList<Update> _executedUpdates;
	
	public DgsUpdateManager(String zooHost, String zooPort, String replicaId)
	{
		_currentTV = new TimestampVetorial();
		_replicaId = replicaId;
		_executedUpdates = new ArrayList<>();
		PropagationThread thread = 
		  new PropagationThread(zooHost, zooPort);
		thread.start();
	}
	
	//  --receives a request to add to the update list--
	// receives timestamp that came with the request
	// if the timestamp is null, it's a request from a client and we regist
	// the update with the server's current timestamp
	// otherwise, it's a request from a replica and we give it the timestamp
	// that the request came with
	public synchronized TimestampVetorial update(com.google.protobuf.GeneratedMessageV3 request, List<Integer> receivedTVList) {
		debug("Starting update process, my timestamp is "+_currentTV.toString());
		debug("update: received "+request.getClass().getSimpleName());

		// increment timestamp
		_currentTV.setPos(Integer.valueOf(_replicaId), _currentTV.getPos(Integer.valueOf(_replicaId)) + 1);
		// get mutex for list of updates
		synchronized (_executedUpdates) {
			// add update to list
			if (receivedTVList == null || receivedTVList.isEmpty()) {
				debug("update(): Received a request from client");
				// update from client, give it our current TV
				_executedUpdates.add(new Update(_currentTV, request));
			}
			else {
				// update from replica, give it the TV it had and max ours
				debug("Received propagation, TS received is "+receivedTVList);
				TimestampVetorial receivedTV = new TimestampVetorial(receivedTVList);
				
				_executedUpdates.add(
					new Update( receivedTV, request));
				
				// update our timestamp in the position of the other replicas
				_currentTV.update(receivedTV);
				//note: valueTS[i] = max (valueTS[i], otherTS[i])
			}
			
		}
		debug("Update added: " + _currentTV.toString());
		return _currentTV;
	}
	
	// returns the current replica TV
	public TimestampVetorial getCurrentTV()
	{
		return _currentTV;
	}

	private void debug(String s) {
		DgsServerApp.debug("(UpdateManager) "+s);
	}

	// receives timestamp in List<> format, may be null
	// returns true if the timestamp is not null and is present in
	// our update list
	public Boolean wasExecuted(List<Integer> tsList) {
		if (tsList == null || tsList.isEmpty()){
			return false;
		}
		TimestampVetorial tv = new TimestampVetorial(tsList);
		// search in the list to see if we already executed him
		for (Update up : _executedUpdates) {
			try {
				if ( up.getTS().equals(tv) ) {
					// found it, it was executed before
					return true;
				}
			} catch (IOException ioe) {
				// timestamps have different size, cant parse this
				debug("updateManager.wasExecuted() : "+ioe.getMessage());
				ioe.printStackTrace();
			}
		}
		// didnt find it in the list, it wasnt executed before
		return false;
	}

	class PropagationThread extends Thread {
		private ZKNaming _zkNaming;

		private ManagedChannel channel;
		private DgsServiceGrpc.DgsServiceBlockingStub stub;

		private Integer[] _seenVersions = new Integer[3];
	
		private final Long PROPAGATION_DELAY_SECS = 30L;
		private final String PATH = "/grpc/staysafe/dgs";

		PropagationThread(String zooHost, String zooPort) {
			for (int i =0; i< 3; i++) {
				_seenVersions[i] = 0;
			}
			_zkNaming = new ZKNaming(zooHost, zooPort);
		}
	
		public void run(){
			try {
				while(true) {
					// wait 30 secs to propagate
					Thread.sleep(PROPAGATION_DELAY_SECS * 1000);
					
					try {
						// send updates to all other replicas
						for (ZKRecord rec : _zkNaming.listRecords(PATH) ) {
							// verify that I am not sending things to myself
							// compare the replicaID in the path i'm looking with my own ReplicaID
							if (rec.getPath().split("/")[PATH.split("/").length].equals(_replicaId) ) {
								// I am selecting my own replica, choose another
								continue;
							}

							connect(rec); // connect to new replica
							// get mutex for update list
							synchronized (_executedUpdates) {

								String receivingRepId = rec.getPath().split("/")[PATH.split("/").length];
								try {
									// send all updates to this replica
									for (Update up : _executedUpdates.subList(getLastIndexSeenBy(receivingRepId), _executedUpdates.size()) ) {

										debug("Sending "+up.getRequest().getClass().getSimpleName().split("Request")[0]+
										 " to replica "+receivingRepId);

										// select to which method we are going to send this
										switch(up.getRequest().getClass().getSimpleName().split("Request")[0]) {

											case "SnifferJoin":
												stub.snifferJoin((SnifferJoinRequest)up.getRequest());
												break;
											case "Report":
												stub.report((ReportRequest) up.getRequest());
												break;
											case "Clear":
												stub.ctrlClear((ClearRequest) up.getRequest());
												break;
											case "Init":
												stub.ctrlInit((InitRequest) up.getRequest());
												break;
											default:
												throw new
												Error("Unexpected type of update: "+
												up.getRequest().getClass().getSimpleName());
										}
										increaseLastIndexSeenBy(receivingRepId);
									}
									
								} catch (Exception e) {
									// some error ocurred, try next replica
									debug("Exception while sending request to replica "+receivingRepId+
									 "message: "+e.getMessage());
									continue;
								}
							}
						}
					} catch (ZKNamingException zke ) {
						// problem connecting to zookeeper
						// let's close, by precaution
						return;
					}
				}
			} catch (InterruptedException ie) {
				// interrupted by main program, time to close
			}
		}

		// returns the last index in update list seen
		// by the replica that is receiving it
		private Integer getLastIndexSeenBy(String repId) {
			return _seenVersions[Integer.valueOf(repId)-1];
		}

		private void increaseLastIndexSeenBy(String repId) {
			_seenVersions[Integer.valueOf(repId)-1]++; 
		}
	
		private final void connect(ZKRecord record) {
			if (channel != null) {
				close(); // shutdown previous channel
			}
			
			String target = record.getURI();
			Integer port = Integer.valueOf(target.split(":")[1]);
	
			channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
			// Create a blocking stub.
			stub = DgsServiceGrpc.newBlockingStub(channel);
			debug("Connected to replica with port "+
			 String.valueOf( port ) );
		}
	
		private void close() {
			channel.shutdown();
		}
	}
}

