package pt.tecnico.staysafe.dgs.client;


import java.util.ArrayList;

import java.util.Random;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.staysafe.dgs.grpc.*;

import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;

public class DgsFrontend implements AutoCloseable{
	private ManagedChannel channel;
	private DgsServiceGrpc.DgsServiceBlockingStub stub;

	/* ZooKeeper stuff */
	ZKNaming zkNaming; // Naming server
	// List that saves the existing servers
	private ArrayList<ZKRecord> _serverRecords;
	private final String PATH = "/grpc/staysafe/dgs";
	private ZKRecord _currentRecord;

	// constructor compatible with IT tests
	public DgsFrontend( String host, String port) {
		this(host, port, null);
	}
	public DgsFrontend(String zooHost, String zooPort, String repId ) {
		_serverRecords = null;

		try {
			// start zookeeper modifications
			zkNaming = new ZKNaming(zooHost,zooPort);

			// gets a List of available servers
			_serverRecords = 
			  new ArrayList<ZKRecord>( zkNaming.listRecords(PATH) );

		} catch (ZKNamingException zke) {
			System.out.println("Error connecting to ZooKeeper:\n"+
			 zke.getMessage()+" -> "+zke.getCause().getMessage());
			System.exit(-1);
		}
		
		// searches for a random replica to connect
		// puts it's address in _currentRecord
		connectToReplica(repId);

		//this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		// end of zookeeper modifications
	}

	// connects to the current Record
	private final void connect() {
		String target = _currentRecord.getURI();
		channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
		// Create a blocking stub.
		stub = DgsServiceGrpc.newBlockingStub(channel);
	}

	// connects to a certain replica
	// Puts the replica address in _currentRecord
	// if null is given (regular use) then it connects to a random replica
	private void connectToReplica(String repId) {
		Random random = new Random();
		if (repId == null) {
			// random record
			_currentRecord = _serverRecords.get( random.nextInt(_serverRecords.size()) );
			connect(); // establish connection
			return;
		}
		
		// repId is specified, lets search it
		Integer portAux;
		for (ZKRecord zkAux : _serverRecords ) {
			// get the port of this record
			portAux = Integer.valueOf(zkAux.getURI().split(":")[1]);
			// if 8080 + repId = this port
			if ( portAux == Integer.valueOf(repId) + 8080 ) {
				// found the replica we wanted
				_currentRecord = zkAux;
				connect(); // establish connection
				return;
			}
		}
		// didnt find our replica, lets choose a random one
		System.out.println("ERROR: The replica you specified \""+repId+"\" wasn't found.");
		connectToReplica(null);

	}

	// called when we lose connection to the current replica
	// connects to different replica, if no other exists, returns false
	private Boolean changeReplica() {
		// no other replicas :(
		if (_serverRecords.size() < 2) {
			return false;
		}

		Random random = new Random();
		
		while (true) { // cycle until finds a new replica
			ZKRecord zkr =
			 _serverRecords.get( random.nextInt(_serverRecords.size()) );

			//found a new record
			if ( !zkr.equals( _currentRecord ) ) { 
				_currentRecord = zkr;
				return true;
			}

		}
	}

	/* CONTROL OPERATIONS */

	// Ping server, receive state info
	public PingResponse ctrlPing(PingRequest request) {
		return stub.ctrlPing(request);
	}
	// Clear the server
	public ClearResponse ctrlClear(ClearRequest request)
	{
		return stub.ctrlClear(request);
	}	
	// Init the server with predefined configs
	public InitResponse ctrlInit(InitRequest request) {
		return stub.ctrlInit(request);
	}

	/* SYSTEM OPERATIONS */
	public SnifferJoinResponse snifferJoin( SnifferJoinRequest request ) {
		return stub.snifferJoin( request );
	}

	public SnifferInfoResponse snifferInfo (SnifferInfoRequest request) {
		return stub.snifferInfo( request );
	}
	
	public ReportResponse report(ReportRequest request)
	{
		return stub.report(request);
	}

	public IndividualInfectionProbabilityResponse individualInfectionProbability( 
		IndividualInfectionProbabilityRequest request) {
	
		return stub.individualInfectionProbability(request);
	}
	
	public AggregateInfectionProbabilityResponse aggregateInfectionProbability(
			AggregateInfectionProbabilityRequest request)
	{
		return stub.aggregateInfectionProbability(request);
	}
	
	@Override
	public final void close() {
		channel.shutdown();
	}
}
