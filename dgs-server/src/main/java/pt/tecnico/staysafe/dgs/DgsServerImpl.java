package pt.tecnico.staysafe.dgs;

import pt.tecnico.staysafe.dgs.grpc.*;
import io.grpc.stub.StreamObserver;

import com.google.protobuf.Timestamp;
import java.util.Date;


public class DgsServerImpl extends DgsServiceGrpc.DgsServiceImplBase{
	
	private DgsSystem dgsSystem = new DgsSystem();

	//Control Operations
	
	//ctrl_ping
	@Override
	public void ctrlPing(PingRequest request, StreamObserver<PingResponse> responseObserver)
	{
		String output = "Dgs server state\n" + dgsSystem.observationsToString();
		PingResponse response = PingResponse.newBuilder().setResult(output).build();
		dgsSystem.debugObservations(); // TESTE
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
	@Override
	public void report(ReportRequest request, StreamObserver<ReportResponse> responseObserver)
	{
		// build insertionTime
		Date insertionDate = java.util.Calendar.getInstance().getTime();
		Timestamp insertionTimestamp = Timestamp.newBuilder().
		setSeconds( insertionDate.getTime() ).
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
