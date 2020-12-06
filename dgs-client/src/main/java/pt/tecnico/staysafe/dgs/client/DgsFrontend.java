package pt.tecnico.staysafe.dgs.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import pt.tecnico.staysafe.dgs.client.exceptions.OutdatedReadException;
import pt.tecnico.staysafe.dgs.grpc.*;
import com.google.protobuf.GeneratedMessageV3;

import pt.tecnico.staysafe.dgs.update.DgsDebugger;
import pt.tecnico.staysafe.dgs.update.TimestampVetorial;

import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;

public class DgsFrontend implements AutoCloseable, DgsDebugger{
	private ManagedChannel channel;
	private DgsServiceGrpc.DgsServiceBlockingStub stub;

	/* ZooKeeper stuff */
	ZKNaming zkNaming; // Naming server
	private final String PATH = "/grpc/staysafe/dgs";

	// List that saves the existing servers
	private ArrayList<ZKRecord> _serverRecords;
	// The replica we are currently connected to
	private ZKRecord _currentRecord;
	// The last version of the server that we saw
	private TimestampVetorial _prevTV = new TimestampVetorial();

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

	/* METHODS TO HANDLE CLIENT COERENCE AND VECTORIAL TIMESTAMPS */

	// called in methods that make readings
	// receives a response's TV from server
	// updates our TV if it is newer than the last version the client had seen
	// throws exception if the version of server happened before or is concurrent
	private void read(TimestampVetorial serverCurrTV) throws OutdatedReadException{
		try {
			if ( serverCurrTV.happensBefore(_prevTV) != true) {
				throw new OutdatedReadException();
			}
		} catch ( IOException ioe ) {
			System.out.println(ioe.getMessage());
			ioe.printStackTrace();
			throw new RuntimeException();
		}
		// our timestamp is older than the server's, lets update our tv
		_prevTV.update(serverCurrTV);
	}

	private void read(List<Integer> listTV) throws OutdatedReadException {
		read(new TimestampVetorial( (Integer[]) listTV.toArray() ) );
	}

	// called in methods that make writings
	// receives a response's TV from server
	// updates our TV with the new server's TV after our writing
	private void write(TimestampVetorial serverCurrTV) {	
		// lets update our tv with the new one
		_prevTV.update(serverCurrTV);
	}
	private void write(List<Integer> listTV) {
		write(new TimestampVetorial( (Integer[])listTV.toArray() ) );
	}

	/* METHODS TO HANDLE MULTIPLE REPLICAS */
	// connects to the current Record
	private final void connect() {
		String target = _currentRecord.getURI();
		Integer port = Integer.valueOf(target.split(":")[1]);

		channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
		// Create a blocking stub.
		stub = DgsServiceGrpc.newBlockingStub(channel);
		debug("Connected to replica "+
		 String.valueOf( port - 8080 ) );
	}

	// connects to a certain replica
	// Puts the replica address in _currentRecord
	// if null is given (regular use) then it connects to a random replica
	private void connectToReplica(String repId) {
		Random random = new Random();
		if (repId == null) {
			debug("Going to connect to random replica");
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

	// refreshes the list of replicas
	private void updateReplicas()
	{
		try {
			// gets a List of available servers
			_serverRecords = 
			  new ArrayList<ZKRecord>( zkNaming.listRecords(PATH) );

		} catch (ZKNamingException zke) {
			System.out.println("Error connecting to ZooKeeper:\n"+
			 zke.getMessage()+" -> "+zke.getCause().getMessage());
			System.exit(-1);
		}		
	}

	// handles the fault
	private void faultAction()
	{
		updateReplicas();
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
		// ensures the replica is available
		PingResponse pr = null;
		try
		{
			pr = stub.ctrlPing(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				pr = ctrlPing(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		return pr;
	}
	// Clear the server
	public ClearResponse ctrlClear(ClearRequest request)
	{
		ClearResponse cr = null;
		try
		{
			cr = stub.ctrlClear(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				cr = ctrlClear(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		// updates our TV with the new TV from server
		write(cr.getCurrentTVList());
		return cr;
	}	
	// Init the server with predefined configs
	public InitResponse ctrlInit(InitRequest request) {
		InitResponse ir = null;
		try
		{
			ir = stub.ctrlInit(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				ir = ctrlInit(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		// updates our TV with the new TV from server
		write(ir.getCurrentTVList());
		return ir;
	}

	/* SYSTEM OPERATIONS */
	public SnifferJoinResponse snifferJoin( SnifferJoinRequest request ) {
		SnifferJoinResponse sjr = null;
		try
		{
			sjr = stub.snifferJoin(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				sjr = snifferJoin(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		// updates our TV with the new TV from server
		write(sjr.getCurrentTVList());
		return sjr;
	}

	public SnifferInfoResponse snifferInfo (SnifferInfoRequest request)
	  throws OutdatedReadException {
		SnifferInfoResponse sir = null;
		try
		{
			sir = stub.snifferInfo(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				sir = snifferInfo(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		// updates our TV with the current server TV
		// or throw exception if the server is delayed
		read(sir.getCurrentTVList());
		return sir;
	}
	
	public ReportResponse report(ReportRequest request)
	{
		ReportResponse rr = null;
		try
		{
			rr = stub.report(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				rr = report(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		// updates our TV with the new TV from server
		write(rr.getCurrentTVList());
		return rr;
	}

	public IndividualInfectionProbabilityResponse individualInfectionProbability( 
	  IndividualInfectionProbabilityRequest request)
	  throws OutdatedReadException {
		IndividualInfectionProbabilityResponse iipr = null;
		try
		{
			System.out.println("Reconecting...");

			iipr = stub.individualInfectionProbability(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				iipr = individualInfectionProbability(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		// updates our TV with the current server TV
		// or throw exception if the server is delayed
		read(iipr.getCurrentTVList());
		return iipr;
	}
	
	public AggregateInfectionProbabilityResponse aggregateInfectionProbability(
			AggregateInfectionProbabilityRequest request)
			throws OutdatedReadException
	{
		AggregateInfectionProbabilityResponse aipr = null;
		try
		{

			aipr = stub.aggregateInfectionProbability(request);
		}
		catch(StatusRuntimeException e)
		{
			// if the exception means unavailable server
			if ( isDcException(e) ) {
				// change replica and try again
				faultAction();
				aipr = aggregateInfectionProbability(request);
			}
			// if it's an exception from server
			else {
				throw e;
			}
		}
		// updates our TV with the current server TV
		// or throw exception if the server is delayed
		read(aipr.getCurrentTVList());
		return aipr;
	}
	
	@Override
	public final void close() {
		channel.shutdown();
	}

	private void debug(String s) {
		DgsDebugger.debug(s);
	}

	// returns true if exception means that we lost connection
	private Boolean isDcException(Exception e) {
		if (e.getClass().getName().equals("io.grpc.StatusRuntimeException")) {
			debug("Exception name is statusRuntime");
			if ( e.getMessage().equals("UNAVAILABLE: io exception") ) {
				return true;
			}
		}
		return false;
	}

}
