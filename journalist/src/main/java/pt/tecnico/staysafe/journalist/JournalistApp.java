package pt.tecnico.staysafe.journalist;

import pt.tecnico.staysafe.dgs.client.DgsAbstractClient;

public class JournalistApp extends DgsAbstractClient{
	
	//call the super class constructor because of frontend
	public JournalistApp(String host, int port, String repId)
	{
		super(host,port, repId, "journalist");
	}
	public JournalistApp(String host, Integer port) {
		super(host, port, null, "journalist");
	}
		
	public static void main(String[] args) {
						
		System.out.println(JournalistApp.class.getSimpleName());

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
		

		JournalistApp journalist = new JournalistApp(host, port, repId);
			
		//Add journalist commands
		journalist.run();
		journalist.close();
		
		 
	}


}
