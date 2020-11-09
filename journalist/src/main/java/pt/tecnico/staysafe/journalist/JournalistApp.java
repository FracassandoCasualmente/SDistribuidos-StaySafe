package pt.tecnico.staysafe.journalist;

import pt.tecnico.staysafe.dgs.client.DgsAbstractClient;

public class JournalistApp extends DgsAbstractClient{
	
	//call the super class constructor because of frontend
	public JournalistApp(String host, int port)
	{
		super(host,port,"journalist");
	}
		
	public static void main(String[] args) {
						
		System.out.println(JournalistApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		//basic arguments checking
		if(args.length != 2)
		{
			System.out.println("ERROR: Invalid number of arguments!");
			System.out.println("./journalist host port");
			System.exit(-1);
		}
		
		//Connection parameters
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		
		JournalistApp journalist = new JournalistApp(host,port);
			
		//Add journalist commands
		journalist.run();
		journalist.close();
		
		 
	}


}
