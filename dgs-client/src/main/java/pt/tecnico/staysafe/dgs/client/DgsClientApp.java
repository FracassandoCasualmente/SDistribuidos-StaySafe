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
	    PingRequest request = PingRequest.getDefaultInstance();
	    PingResponse response = frontend.ctrlPing(request);
	    System.out.println(response.getResult());
	    
	}
	
}
