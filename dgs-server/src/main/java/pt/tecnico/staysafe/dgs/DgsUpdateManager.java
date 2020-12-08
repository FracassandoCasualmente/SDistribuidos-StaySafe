package pt.tecnico.staysafe.dgs;

import java.util.LinkedList;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.staysafe.dgs.grpc.DgsServiceGrpc;
import pt.tecnico.staysafe.dgs.grpc.*;

import pt.tecnico.staysafe.dgs.update.DgsDebugger;
import pt.tecnico.staysafe.dgs.update.TimestampVetorial;
import pt.tecnico.staysafe.dgs.update.Update;

import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;

// class that is initialized in serverImpl to manage updates
public class DgsUpdateManager {
	private TimestampVetorial _currentTV;
	private String _replicaId;
	private LinkedList<Update> _executedUpdates;
	
	public DgsUpdateManager(String zooHost, String zooPort, String replicaId)
	{
		_currentTV = new TimestampVetorial();
		_replicaId = replicaId;
		_executedUpdates = new LinkedList<>();
		PropagationThread thread = 
		  new PropagationThread(zooHost, zooPort);
		thread.start();
	}
	
	// updates the current TV, adds the update to the list and returns the new TV to client
	public synchronized TimestampVetorial update(com.google.protobuf.GeneratedMessageV3 request) {
		debug("My timestamp is "+_currentTV.toString());
		debug("update: received "+request.getClass().getSimpleName());
		
		if (request.getClass().getSimpleName().equals("SnifferJoinRequest")) {
			debug("Received a sj request");
			SnifferJoinRequest sjr = (SnifferJoinRequest) request;
			if (sjr.getName() == null) {
				debug("The sniffer name is null...");
			}
			else {
				debug("The sniffer name is "+sjr.getName());
			}
		}
		else {
			debug("request name : "+(request.getClass().getSimpleName()));
		}
		_currentTV.setPos(Integer.valueOf(_replicaId), _currentTV.getPos(Integer.valueOf(_replicaId)) + 1);
		// get mutex for list of updates
		synchronized (_executedUpdates) {
			_executedUpdates.add(new Update(_currentTV,request));
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
		DgsServerApp.debug(s);
	}

	class PropagationThread extends Thread {
		private ZKNaming _zkNaming;

		private ManagedChannel channel;
		private DgsServiceGrpc.DgsServiceBlockingStub stub;
	
		private final Long PROPAGATION_DELAY_SECS = 30L;
		private final String PATH = "/grpc/staysafe/dgs";

		PropagationThread(String zooHost, String zooPort) {
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
								try {
									// send all updates to this replica
									for (Update up : _executedUpdates) {
										
										// select to which method we are going to send this
										// TODO: WE NEED TO ADD THE TIMESTAMP IN UPDATE(), INSIDE EACH OF THIS CASES
										switch(up.getRequest().getClass().getSimpleName().split("Request")[0]) {
											case "SnifferJoin":
												debug("Going to send a snifferJoin");
												
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
									}
									
								} catch (Exception e) {
									// some error ocurred, try next replica
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

