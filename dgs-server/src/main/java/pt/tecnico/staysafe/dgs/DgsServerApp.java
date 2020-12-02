package pt.tecnico.staysafe.dgs;

import java.io.IOException;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;

public class DgsServerApp {
	private static Boolean _debug = false;

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("StaySafe dgs server");
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// check arguments
		if (args.length != 5) {
			System.err.println("Argument(s) are wrong! We need 5!");
			System.err.printf("Usage: java %s port%n", DgsServerApp.class.getName());
			return;
		}
		
		// parse arguments
		final String zooHost = args[0];
		final String zooPort = args[1];
		final String host = args[2];
		final String port = args[3];
		final String path = args[4];

		ZKNaming zkNaming = null;

		try {

			zkNaming = new ZKNaming(zooHost, zooPort);
			// publish
			debug("path: \""+path+"\"");

			// Tell ZKNaming to link the path (...)/dgs/1 to my ip and port
			zkNaming.rebind(path, host, port);

			final BindableService impl = new DgsServerImpl();

			// Create a new server to listen on port
			Server server = ServerBuilder.forPort( Integer.parseInt(port) ).addService(impl).build();

			// Start the server
			server.start();

			System.out.println("DgsServer started");

			// Do not exit the main thread. Wait until server is terminated.
			server.awaitTermination();
		}
		catch (pt.ulisboa.tecnico.sdis.zk.ZKNamingException e) {
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
}	

