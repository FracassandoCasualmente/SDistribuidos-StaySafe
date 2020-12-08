package pt.tecnico.staysafe.dgs;

import java.io.IOException;
import java.io.PrintWriter;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;

//import pt.tecnico.staysafe.dgs.update.DgsDebugger;

import sun.misc.Signal; //  signal handler

public class DgsServerApp {

	private static final Boolean _debug = true;
	private static final Integer MAX_PORT = 9500;
	private static final String PATH = "/grpc/staysafe/dgs/";
	private static Integer _repId;

	/* SET THIS TO --FALSE-- IF YOU DONT WANT TO USE COLOURING */
	private static final Boolean _useColours = true;

	// colours for debug
	private static final String COLOUR = "\033[1;93m" ; // yellow bold
	private static final String COLOUR_CYAN = "\033[1;96m" ; // cyan bold
	private static final String COLOUR_BLACK = "\033[1;90m"; // black bold
	private static final String COLOUR_WHITE = "\033[1;97m"; // white bold
	private static final String COLOUR_RESET = "\u001B[0m";
	// coloured backgrounds
	private static final String RED_BACKGROUND = "\033[0;41m";// RED
    private static final String GREEN_BACKGROUND = "\033[0;42m";// GREEN
    private static final String YELLOW_BACKGROUND = "\033[0;43m";// YELLOW
    private static final String BLUE_BACKGROUND = "\033[0;44m";// BLUE
    private static final String PURPLE_BACKGROUND = "\033[0;45m"; // PURPLE
	private static final String CYAN_BACKGROUND = "\033[0;46m";  // CYAN

	// colours used for replicas to communicate
	private static final String[] COLOUR_ARRAY =
	 {BLUE_BACKGROUND,YELLOW_BACKGROUND, RED_BACKGROUND};



	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("StaySafe dgs server");
		
		// receive and print arguments
		/*System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}*/

		// check arguments
		if (args.length != 5) {
			System.err.println("Argument(s) are wrong! We need 5!");
			System.err.printf("Usage: java %s port%n", DgsServerApp.class.getName());
			System.err.println("./dgs-server zkHost zkPort path replica_id server_host server_port");
			return;
		}
		// ./dgs-server zkHost zkPort path replica_id server_host server_port
		// load arguments
		final String zooHost = args[0];
		final String zooPort = args[1];
		//final String path = args[2];
		final String repId = args[2];
		final String host = args[3];
		final String port = args[4]; // going to be substituted dynamically

		_repId = Integer.valueOf(repId);

		// create signal handler
		Signal.handle(new Signal("INT"), 
    	signal -> {
			debug("\nOk, I'll (kindly) kill myself. Bye");
			System.exit(1);
		}
		);
		// handler to kill processes from shell
		Signal.handle(new Signal("TERM"), 
    	signal -> {
			debug("Ok, I'll (sadly) kill myself. Bye");
			System.exit(1);
		}
		);

		ZKNaming zkNaming = null;
		String path = PATH + repId;
		try {

			zkNaming = new ZKNaming(zooHost, zooPort);
			
			// regists in naming server
			zkNaming.rebind(path, host, port);

			//final BindableService impl = new DgsServerImpl();
			final BindableService impl = new DgsServerImpl( Integer.valueOf(port) );

			// Create a new server to listen on port
			Server server = ServerBuilder.forPort( Integer.valueOf(port) ).addService(impl).build();
			debug("created a server");
			// Start the server
			server.start();

			debug("DgsServer started");

			// get and print PID
			Long pid = ProcessHandle.current().pid();

			if (_useColours)
				System.out.println(COLOUR+"PID replica "+COLOUR_CYAN+repId+COLOUR+" = "+pid+COLOUR_RESET);

			// write pid in txt file
			writePid(pid.toString());

			// Do not exit the main thread. Wait until server is terminated.
			server.awaitTermination();
		}
		catch (Throwable e) {
			
			debug("Exception name: "+e.getClass().getName());
			//debug("Exception msg:\n"+e.getMessage()+" -> "+e.getCause().getMessage());
			debug("exception in outer catch: "+e.getStackTrace());
			
			debug("ERROR: "+e.getMessage());

		} finally {
			debug("server going to finish...");

			// removes the zNode in ZooKeeper associated with my ip and port
			if (zkNaming != null) {
				// remove
				try {
					zkNaming.unbind(path,host,port);
					debug("Unbinding successful");
				} catch (ZKNamingException e) {
					debug("Problem ocurred while unbinding:\n"+
					 e.getLocalizedMessage());
				}
			}
		}
		
	}
	
	static void debug(String s) {
		if (_debug) {
			if (!_useColours)
				System.out.println("Rep "+_repId+": "+s);
			else
				System.out.println(COLOUR_BLACK+COLOUR_ARRAY[_repId%3]+COLOUR_WHITE+"Rep "+_repId+": "+s+COLOUR_RESET);
		}
	}

	private static void writePid(String pid) {
		try (PrintWriter out = new PrintWriter("tmp/"+_repId+".txt")) {
			out.println(pid);
		} catch (Exception e) {
			debug("Error saving file with pid: "+e.getMessage());
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

