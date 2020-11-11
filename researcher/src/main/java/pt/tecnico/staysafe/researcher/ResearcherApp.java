package pt.tecnico.staysafe.researcher;

import pt.tecnico.staysafe.dgs.client.DgsAbstractClient;

public class ResearcherApp extends DgsAbstractClient{

	public ResearcherApp(String host, Integer port) {
		super(host, port, "researcher");
	}
	
	public static void main(String[] args) {
		System.out.println(ResearcherApp.class.getSimpleName());

		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		//Connection parameters
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		
		// create researcher instance
		ResearcherApp researcher = new ResearcherApp(host, port); 

		// read input and execute commands until blank line or EOF
		researcher.run();
		// Ending the program, close and finish
		researcher.close();
	}
}


