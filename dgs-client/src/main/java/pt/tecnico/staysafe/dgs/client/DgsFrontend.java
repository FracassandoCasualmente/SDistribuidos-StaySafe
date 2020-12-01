package pt.tecnico.staysafe.dgs.client;


import java.util.ArrayList;
import java.util.Collection;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.staysafe.dgs.grpc.*;

import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;

public class DgsFrontend implements AutoCloseable{
	private final ManagedChannel channel;
	private final DgsServiceGrpc.DgsServiceBlockingStub stub;

	public DgsFrontend(String zooHost, String zooPort) {
		
		try {
			// start zookeeper modifications
			ZKNaming zkNaming = new ZKNaming(zooHost,zooPort);
			//ZKRecord record = zkNaming.lookup(/*path*/ "/");

			//TESTE
			for (ZKRecord zkr : zkNaming.listRecords("/grpc/staysafe/dgs") ) {
				System.out.println(zkr);
			}

			//String target = record.getURI();

		} catch (ZKNamingException zke) {
			System.out.println("Error connecting to ZooKeeper:\n"+
			 zke.getMessage()+" -> "+zke.getCause().getMessage());
			System.exit(-1);
		}

		channel = ManagedChannelBuilder.forTarget(/*target*/ "localhost:8081" ).usePlaintext().build();

		//this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		// end of zookeeper modifications

		// Create a blocking stub.
		stub = DgsServiceGrpc.newBlockingStub(channel);
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
