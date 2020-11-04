package pt.tecnico.staysafe.dgs.client;

import io.grpc.StatusRuntimeException;
import pt.tecnico.staysafe.dgs.grpc.*;

public class DgsClientApp {
	
	public static void main(String[] args) {
		
		//Connection parameters
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		
		System.out.println(DgsClientApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		DgsFrontend frontend = new DgsFrontend(host, port);

		//Ping operation
		System.out.println("Pinging");
	    PingRequest pingRequest = PingRequest.getDefaultInstance();
	    PingResponse pingResponse = frontend.ctrlPing(pingRequest);
		System.out.println(pingResponse.getResult());
		
		//Individual infection prob operation
	    System.out.println("Individual inf. prob. of 2225");
	    IndividualInfectionProbabilityRequest iipRequest = IndividualInfectionProbabilityRequest.newBuilder().setCitizenId(2225).build();
	    IndividualInfectionProbabilityResponse iipResponse = frontend.individualInfectionProbability(iipRequest);
		System.out.println(iipResponse.getProbability());
	}
	
}
