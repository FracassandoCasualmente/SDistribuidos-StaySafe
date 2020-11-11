package pt.tecnico.staysafe.dgs.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.staysafe.dgs.grpc.*;

import java.io.IOException;

public class DgsFrontend implements AutoCloseable{
	private final ManagedChannel channel;
	private final DgsServiceGrpc.DgsServiceBlockingStub stub;

	public DgsFrontend(String host, int port) {
		
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

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
