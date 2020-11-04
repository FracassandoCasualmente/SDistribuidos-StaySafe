package pt.tecnico.staysafe.dgs.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.staysafe.dgs.grpc.*;

public class DgsFrontend implements AutoCloseable{
	private final ManagedChannel channel;
	private final DgsServiceGrpc.DgsServiceBlockingStub stub;

	public DgsFrontend(String host, int port) {
		
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		// Create a blocking stub.
		stub = DgsServiceGrpc.newBlockingStub(channel);
	}

	public PingResponse ctrlPing(PingRequest request) {
		return stub.ctrlPing(request);
	}
	
	public SnifferJoinResponse snifferJoin( SnifferJoinRequest request ) {
		return stub.snifferJoin( request );
	}
	
	public ReportResponse report(ReportRequest request)
	{
		return stub.report(request);
	}

	public IndividualInfectionProbabilityResponse individualInfectionProbability( 
		IndividualInfectionProbabilityRequest request) {
	
		return stub.individualInfectionProbability(request);
	}
	
	@Override
	public final void close() {
		channel.shutdown();
	}
}
