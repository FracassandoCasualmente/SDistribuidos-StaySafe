package pt.tecnico.staysafe.dgs;

import pt.tecnico.staysafe.dgs.grpc.*;
import io.grpc.stub.StreamObserver;

public class DgsServerImpl extends DgsServiceGrpc.DgsServiceImplBase{
	
	private DgsSystem dgsSystem = new DgsSystem();

	//Control Operations
	
	//ctrl_ping 
	public void ctrlPing(PingRequest request, StreamObserver<PingResponse> responseObserver)
	{
		String output = "Dgs server state\n" + dgsSystem.observationsToString();
		PingResponse response = PingResponse.newBuilder().setResult(output).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	
	//Dgs Operations
	
	// sends "OK" if success, Error message otherwise
	@Override
	public void snifferJoin(SnifferJoinRequest request, StreamObserver<SnifferJoinResponse> responseObserver) {
		// tries to join sniffer, gets false if a sniffer with same name and different
		// address exists
		Boolean success = dgsSystem.joinSniffer(request.getName(), request.getAddress());
		// build message sent to client
		String result = ( (success)? "OK" : "Error: A sniffer with the same name and different address already exists.\n"+
			"Name: "+request.getName()+"\n"+
			"Address: "+request.getAddress()+"\n" );

		//builds and sends response
		SnifferJoinResponse response = SnifferJoinResponse.newBuilder().setResult(result).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	//report operation
	public void report(ReportRequest request, StreamObserver<ReportResponse> responseObserver)
	{
		Observation newObs = new Observation(request.getSnifferName(),request.getInsertionTime(),
				request.getType(),request.getCitizenId(),request.getEnterTime(),request.getLeaveTime());
		
		dgsSystem.addReport(newObs);
		ReportResponse response = ReportResponse.getDefaultInstance();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
				
	}
}
