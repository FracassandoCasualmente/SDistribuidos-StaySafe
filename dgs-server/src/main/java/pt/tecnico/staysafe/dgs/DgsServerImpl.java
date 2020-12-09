package pt.tecnico.staysafe.dgs;

import pt.tecnico.staysafe.dgs.exception.*;
import pt.tecnico.staysafe.dgs.grpc.*;
import pt.tecnico.staysafe.dgs.update.DgsDebugger;


import io.grpc.stub.StreamObserver;
import static io.grpc.Status.INVALID_ARGUMENT;
import com.google.protobuf.Timestamp;

import java.util.List;
import java.util.Date;


public class DgsServerImpl extends DgsServiceGrpc.DgsServiceImplBase{
	
	private DgsSystem dgsSystem = new DgsSystem();
	private Boolean _debug = true;
	private DgsUpdateManager _replicaManager;

	public DgsServerImpl(String zooHost, String zooPort, String repId) {
		super();
		_replicaManager = new DgsUpdateManager( zooHost, zooPort, repId );
	}
	
	private void debug(String msg) {
		if (_debug) {
			DgsServerApp.debug("(ServerImpl) : "+msg);
		}
	}

	//Control Operations
	
	//ctrl_ping
	@Override
	public void ctrlPing(PingRequest request, StreamObserver<PingResponse> responseObserver)
	{
		String output = "Server State: UP";
		PingResponse response = PingResponse.newBuilder().setResult(output).
				addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	// ctrl_clear
	@Override
	public void ctrlClear(ClearRequest request, StreamObserver<ClearResponse> responseObserver)
	{
		// empty response
		ClearResponse response = null;
		// see if the update was already executed
		if ( _replicaManager.wasExecuted(request.getTVList()) ) {
			// it was executed before, I will just say OK to other replica
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			return;
		}
		// it wasnt executed yet, lets execute the request now

		String output = "";
		synchronized (this) {
			output = dgsSystem.clear();
		}
		
		// inserting into executed updates log
		_replicaManager.update(request, request.getTVList());
		
		response = ClearResponse.newBuilder().setResult(output).
				addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).
				build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	// ctrl_init
	@Override
	public void ctrlInit(InitRequest request, StreamObserver<InitResponse> responseObserver)
	{
		try {
			
			// empty response
			InitResponse response = null;
			// see if the update was already executed
			if ( _replicaManager.wasExecuted(request.getTVList()) ) {
				// it was executed before, I will just say OK to other replica
				responseObserver.onNext(response);
				responseObserver.onCompleted();
				return;
			}
			// it wasnt executed yet, lets execute the request now

			synchronized (this) {
				dgsSystem.init();
			}
			
			// inserting into executed updates log
			_replicaManager.update(request, request.getTVList());
			
			response = InitResponse.newBuilder().
					addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).
					build();
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
			// empty response
			SnifferJoinResponse response = null;

			// debug things
			/*debug("sj: was executed? mine="+_replicaManager.getCurrentTV()+
				" otherTV = "+request.getTVList());
			debug("wasExecuted = "+_replicaManager.wasExecuted(request.getTVList()));*/

			// see if the update was already executed
			if ( _replicaManager.wasExecuted(request.getTVList()) ) {
				// it was executed before, I will just say OK to other replica
				responseObserver.onNext(response);
				responseObserver.onCompleted();
				return;
			}
			// it wasnt executed yet, lets execute the request now

			//joining sniffer and getting respective response
			String result = "";
			synchronized (this) {
				result = dgsSystem.joinSniffer(request.getName(), request.getAddress());
			}
			
			// inserting into executed updates log
			_replicaManager.update(request, request.getTVList());
			
			response = SnifferJoinResponse.newBuilder().setResult(result).
					addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).build();
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
			SnifferInfoResponse response = SnifferInfoResponse.newBuilder().setAddress(result).
					addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).build();
			//debug("SIR: going to send TV");
			//debug("TV = "+_replicaManager.getCurrentTV().getTvAsList());
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
		catch(SnifferDoesNotExistException sdne) {
			responseObserver.onError(INVALID_ARGUMENT.withDescription(sdne.getMessage()).asRuntimeException());
		}
		
		
	}

	//report operation
	@Override
	public void report(ReportRequest request, StreamObserver<ReportResponse> responseObserver)
	{
		// empty response
		ReportResponse response = null;
		// see if the update was already executed
		if ( _replicaManager.wasExecuted(request.getTVList()) ) {
			// it was executed before, I will just say OK to other replica
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			return;
		}
		// it wasnt executed yet, lets execute the request now
		
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
				String result = dgsSystem.addReport(newObs);
								
				// inserting into executed updates log
				_replicaManager.update(request, request.getTVList());
				
				response = ReportResponse.newBuilder().setResult(result).
						addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).
						build();
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			} catch (SnifferDoesNotExistException e) {
				responseObserver.onError(INVALID_ARGUMENT.
				withDescription(e.getMessage()).asRuntimeException());
			}
			
		}
				
	}

	// individual infection probability operation
	@Override
	public void individualInfectionProbability(
	 IndividualInfectionProbabilityRequest request,
	 StreamObserver<IndividualInfectionProbabilityResponse> 
	 responseObserver) {

		synchronized (this) {
			// calculate probability
			try {
				Double probability = dgsSystem.individualInfectionProbability(
					request.getCitizenId() );
				// build response object
				IndividualInfectionProbabilityResponse response = 
				IndividualInfectionProbabilityResponse.newBuilder().
				setProbability( probability ).
				addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).build();

	   			responseObserver.onNext(response);
				   responseObserver.onCompleted();
				   
			} catch (CitizenDoesNotExistException e) {
				responseObserver.onError(INVALID_ARGUMENT.withDescription(
					e.getMessage()).asRuntimeException());
			}
		}			

		

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
				newBuilder().setResult(probability).
				addAllCurrentTV(_replicaManager.getCurrentTV().getTvAsList()).build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}
