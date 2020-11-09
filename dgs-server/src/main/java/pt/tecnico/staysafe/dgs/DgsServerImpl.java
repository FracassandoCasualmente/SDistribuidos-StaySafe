package pt.tecnico.staysafe.dgs;

import pt.tecnico.staysafe.dgs.exception.SnifferAlreadyRegisteredException;
import pt.tecnico.staysafe.dgs.grpc.*;
import io.grpc.stub.StreamObserver;
import static io.grpc.Status.INVALID_ARGUMENT;
import com.google.protobuf.Timestamp;
import java.util.Date;


public class DgsServerImpl extends DgsServiceGrpc.DgsServiceImplBase{
	
	private DgsSystem dgsSystem = new DgsSystem();

	//Control Operations
	
	//ctrl_ping
	@Override
	public void ctrlPing(PingRequest request, StreamObserver<PingResponse> responseObserver)
	{
		String output = "Server State: UP";
		PingResponse response = PingResponse.newBuilder().setResult(output).build();
		dgsSystem.debugObservations(); // TESTE
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void ctrlClear(ClearRequest request, StreamObserver<ClearResponse> responseObserver)
	{
		String output = dgsSystem.clear();
		ClearResponse response = ClearResponse.newBuilder().setResult(output).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	
	//Dgs Operations
	
	// sends "OK" if success, Error message otherwise
	@Override
	public void snifferJoin(SnifferJoinRequest request, StreamObserver<SnifferJoinResponse> responseObserver) {
		
		try {
			dgsSystem.joinSniffer(request.getName(), request.getAddress());
			//builds and sends response
			String result = "OK";
			SnifferJoinResponse response = SnifferJoinResponse.newBuilder().setResult(result).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
		catch(SnifferAlreadyRegisteredException sare)
		{
			responseObserver.onError(INVALID_ARGUMENT.withDescription(sare.getMessage()).asRuntimeException());
		}
	}
	
	//report operation
	@Override
	public void report(ReportRequest request, StreamObserver<ReportResponse> responseObserver)
	{
		// build insertionTime
		Date insertionDate = java.util.Calendar.getInstance().getTime();
		Timestamp insertionTimestamp = Timestamp.newBuilder().
		setSeconds( insertionDate.getTime()/1000L ).
		buildPartial();
		// build new Observation instance
		Observation newObs = new Observation(request.getSnifferName(),insertionTimestamp,
				request.getType(),request.getCitizenId(),request.getEnterTime(),request.getLeaveTime());
		
		if ( !dgsSystem.addReport(newObs) ) {
			// TODO return error
			System.out.println("Error adding report! Sniffer not found!");
		}
		else {
			System.out.println("report added with success");
		}
			
		ReportResponse response = ReportResponse.getDefaultInstance();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
				
	}

	// individual infection probability operation
	@Override
	public void individualInfectionProbability(
	 IndividualInfectionProbabilityRequest request,
	 StreamObserver<IndividualInfectionProbabilityResponse> 
	 responseObserver) {
		
		// calculate probability
		Double probability = dgsSystem.individualInfectionProbability(
			request.getCitizenId() );
			

		// build response object
		IndividualInfectionProbabilityResponse response = 
		 IndividualInfectionProbabilityResponse.newBuilder().
		 setProbability( probability ).build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();

	}
	
	//aggregate infection probability operation
	@Override
	public void aggregateInfectionProbability(AggregateInfectionProbabilityRequest request,
			StreamObserver<AggregateInfectionProbabilityResponse> responseObserver)
	{
		//calculate probability
		String probability = dgsSystem.aggregateInfectionProbability(request.getStatistic());
		
		//build response
		AggregateInfectionProbabilityResponse response = AggregateInfectionProbabilityResponse.
				newBuilder().setResult(probability).build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}
