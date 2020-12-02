package pt.tecnico.staysafe.researcher;

import pt.tecnico.staysafe.dgs.client.DgsAbstractClient;

public class ResearcherApp extends DgsAbstractClient{

	public ResearcherApp(String host, Integer port, String repId) {
		super(host, port, repId, "researcher");
	}
	public ResearcherApp (String host, Integer port) {
		super(host, port, null, "researcher");
	}
	
	public static void main(String[] args) {
		System.out.println(ResearcherApp.class.getSimpleName());

		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// number of arguments checking
		if(args.length != 2 && args.length != 3)
		{
			System.out.println("ERROR: Invalid number of arguments!");
			System.out.println("./{client} host port [%ReplicaID%]");
			System.exit(-1);
		}

		// parse the input length and extract replica id (optional parameter)
		String repId = DgsAbstractClient.extractRepId(args);
		//Connection parameters
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		
		// create researcher instance
		ResearcherApp researcher = new ResearcherApp(host, port, repId); 

		// read input and execute commands until blank line or EOF
		researcher.run();
		// Ending the program, close and finish
		researcher.close();
	}
}


