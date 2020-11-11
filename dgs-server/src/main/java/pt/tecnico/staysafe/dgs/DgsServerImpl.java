package pt.tecnico.staysafe.dgs;

import pt.tecnico.staysafe.dgs.exception.*;
import pt.tecnico.staysafe.dgs.grpc.*;
import io.grpc.stub.StreamObserver;
import static io.grpc.Status.INVALID_ARGUMENT;
import com.google.protobuf.Timestamp;
import java.util.Date;


public class DgsServerImpl extends DgsServiceGrpc.DgsServiceImplBase{
	
	private DgsSystem dgsSystem = new DgsSystem();
	private Boolean _debug = false;
	
	private void debug(String msg) {
		if (_debug) {
			System.out.println(msg);
		}
	}

	//Control Operations
	
	//ctrl_ping
	@Override
	public void ctrlPing(PingRequest request, StreamObserver<PingResponse> responseObserver)
	{
		String output = "Server State: UP";
		PingResponse response = PingResponse.newBuilder().setResult(output).build();
		if (_debug) {
			dgsSystem.debugObservations(); // debug
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	// ctrl_clear
	@Override
	public void ctrlClear(ClearRequest request, StreamObserver<ClearResponse> responseObserver)
	{
		String output = "";
		synchronized (this) {
			output = dgsSystem.clear();
		}
		ClearResponse response = ClearResponse.newBuilder().setResult(output).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	// ctrl_init
	@Override
	public void ctrlInit(InitRequest request, StreamObserver<InitResponse> responseObserver)
	{
		try {
			synchronized (this) {
				dgsSystem.init();
			}
			InitResponse response = InitResponse.getDefaultInstance();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} catch(InternalServerErrorException isee){
			responseObserver.onError(INVALID_ARGUMENT.withDescription(isee.getMessage()).asRuntimeException());
		}

	}

	//Dgs Operations
	
	// sends "OK" if success, Error message otherwise
	@Override
	public void snifferJoin(SnifferJoinRequest request, StreamObserver<SnifferJoinResponse> responseObserver) {
		
		try {
			synchronized (this) {
				dgsSystem.joinSniffer(request.getName(), request.getAddress());
			}
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
	
	// search for sniffer name and return it's address
	// returns exception if sniffer does not exist
	@Override
	public void snifferInfo(SnifferInfoRequest request, StreamObserver<SnifferInfoResponse> responseObserver) {
		
		try {
			String result = "";
			synchronized (this) {
				result = dgsSystem.snifferInfo(request.getName());
			}
			//sends response
			SnifferInfoResponse response = SnifferInfoResponse.newBuilder().setAddress(result).build();
			responseObserver.onNext(response);
		}
		catch(SnifferDoesNotExistException sdne) {
			responseObserver.onError(INVALID_ARGUMENT.withDescription(sdne.getMessage()).asRuntimeException());
		}
		
		responseObserver.onCompleted();
		
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
		
		
		synchronized (this) {
			try {
				dgsSystem.addReport(newObs);
			} catch (SnifferDoesNotExistException e) {
				responseObserver.onError(INVALID_ARGUMENT.
				withDescription(e.getMessage()).asRuntimeException());
			}
			
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

		Double probability = new Double(-1);
		synchronized (this) {
			// calculate probability
			try {
				probability = dgsSystem.individualInfectionProbability(
					request.getCitizenId() );
			} catch (CitizenDoesNotExistException e) {
				responseObserver.onError(INVALID_ARGUMENT.withDescription(
					e.getMessage()).asRuntimeException());
			}
		}			

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
		String probability = "";
		synchronized (this) {
			try {
				//calculate probability
				probability = dgsSystem.aggregateInfectionProbability(request.getStatistic());
			}
			catch(CitizenDoesNotExistException | InternalServerErrorException e)
			{
				responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
			}
		}

		//build response
		AggregateInfectionProbabilityResponse response = AggregateInfectionProbabilityResponse.
				newBuilder().setResult(probability).build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}
