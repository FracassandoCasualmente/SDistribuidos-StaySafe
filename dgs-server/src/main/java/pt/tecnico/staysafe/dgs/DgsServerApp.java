package pt.tecnico.staysafe.dgs;

import java.io.IOException;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class DgsServerApp {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("StaySafe dgs server");
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// check arguments
		if (args.length < 1) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s port%n", DgsServerApp.class.getName());
			return;
		}
		
		final int port = Integer.parseInt(args[0]);
		final BindableService impl = new DgsServerImpl();

		// Create a new server to listen on port
		Server server = ServerBuilder.forPort(port).addService(impl).build();

		// Start the server
		server.start();

		System.out.println("DgsServer started");

		// Do not exit the main thread. Wait until server is terminated.
		server.awaitTermination();
	}
		
}	

