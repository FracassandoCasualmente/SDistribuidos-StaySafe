package pt.tecnico.staysafe.dgs;

import pt.tecnico.staysafe.dgs.grpc.*;
import io.grpc.stub.StreamObserver;

public class DgsServerImpl extends DgsServiceGrpc.DgsServiceImplBase{
	
	private DgsSystem dgsSystem = new DgsSystem();

	public void ctrlPing(PingRequest request, StreamObserver<PingResponse> responseObserver)
	{
		String output = "Dgs server state\n" + dgsSystem.observationsToString();
		PingResponse response = PingResponse.newBuilder().setResult(output).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
