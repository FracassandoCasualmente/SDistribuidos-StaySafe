package pt.tecnico.staysafe.dgs;

import java.io.IOException;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;

import sun.misc.Signal; //  signal handler

public class DgsServerApp {
	private static final Boolean _debug = true;
	private static final Integer MAX_PORT = 9500;
	

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("StaySafe dgs server");
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// check arguments
		if (args.length != 6) {
			System.err.println("Argument(s) are wrong! We need 6!");
			System.err.printf("Usage: java %s port%n", DgsServerApp.class.getName());
			System.err.println("./dgs-server zkHost zkPort path replica_id server_host server_port");
			return;
		}
		// ./dgs-server zkHost zkPort path replica_id server_host server_port
		// load arguments
		final String zooHost = args[0];
		final String zooPort = args[1];
		final String path = args[2];
		final String repId = args[3];
		final String host = args[4];
		final String port = args[5]; // going to be substituted dynamically

		// create signal handler
		Signal.handle(new Signal("INT"), 
    	signal -> {
			System.out.println("\nOk, I'll (kindly) kill myself. Bye");
			System.exit(1);
		}
		);

		ZKNaming zkNaming = null;

		try {

			zkNaming = new ZKNaming(zooHost, zooPort);
			// publish
			debug("path: \""+path+"\"");

			// regists in naming server
			zkNaming.rebind(path, host, port);

			//final BindableService impl = new DgsServerImpl();
			final BindableService impl = new DgsServerImpl( Integer.valueOf(port) );

			// Create a new server to listen on port
			Server server = ServerBuilder.forPort( Integer.valueOf(port) ).addService(impl).build();

			// Start the server
			server.start();

			System.out.println("DgsServer started");

			// Do not exit the main thread. Wait until server is terminated.
			server.awaitTermination();
		}
		catch (Exception e) {
			
			debug("Exception name: "+e.getClass().getName());
			debug("Exception msg:\n"+e.getMessage()+" -> "+e.getCause().getMessage());
			debug("exception in outer catch: "+e.getStackTrace());
			// prints {error in ZKNaming} -> {error in ZooKeeper}
			System.out.println("ERROR: "+e.getMessage()+" -> "+
			e.getCause().getMessage());

		} finally {
			System.out.println("server going to finish...");

			// removes the zNode in ZooKeeper associated with my ip and port
			if (zkNaming != null) {
				// remove
				try {
					zkNaming.unbind(path,host,port);
				} catch (ZKNamingException e) {
					System.out.println("Problem ocurred while unbinding:\n"+
					 e.getLocalizedMessage());
				}
			}
		}
		
	}
	
	private static void debug(String s) {
		if (_debug) {
			System.out.println(s);
		}
	}

	// finds path that is not taken yet, binds an andress 
	// and returns the Record containing info about the path and port
	// it's not used in the project, but it might be useful 
	private static ZKRecord findPath(String zooHost, String zooPort,String host, String path) {
		ZKNaming zkNaming = new ZKNaming(zooHost, zooPort);
		ZKRecord res = null;
		Integer newId = 1;
		while (true) {
			if (newId +8080 > MAX_PORT ) {
				System.out.println("I've reached the maximum value for a port, closing");
				System.exit(-1);
			}
			try {
				// tries to bind this path
				res = new ZKRecord(path+String.valueOf(newId), host, String.valueOf(8080+newId) );
				zkNaming.bind(res);
				// binds was successful
				return res;
				
				
			} catch (ZKNamingException zke) {
				debug("Exception inside ZK catch:\n"+zke.getMessage()+" -> "+zke.getCause().getMessage());
				// this path already exists in ZooKeeper, try new one
				newId++;
				continue;
			}
		}
	}
}	

