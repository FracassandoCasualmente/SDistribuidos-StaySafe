package pt.tecnico.staysafe.dgs.client;

import java.io.IOException;
import io.grpc.StatusRuntimeException;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;

public final class DgsClientApp extends DgsAbstractClient{
	
	public DgsClientApp (String host, Integer port, String repId) {
		super(host, port, repId, "DgsClient");
	}
	public DgsClientApp (String host, Integer port) {
		super(host, port, null, "DgsClient");
	}
	public static void main(String[] args) {
		
		System.out.println(DgsClientApp.class.getSimpleName());
		
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

		DgsClientApp client = new DgsClientApp(host, port, repId);
		
		client.run();

		// close frontend and finish
		client.close();
	}

	// method that can be cast from other classes to test commands
	public String executeCommand(String command) {
		try {
			return runCommand(command);
		} catch (IOException ioe) {
			return ioe.getMessage();
		}
	}

	public String report(String snifferName, String input) {
		String[] args = (snifferName+","+input).split(",");
		ReportCommand newRep = new ReportCommand(_frontend);

		// verify if number of arguments is right
		String res;
		if ( (res = checkNumArgs(newRep, args)) != null ) {
			// returned error msg instead of null
			return res;
		}
		  
		//Removing the whitespaces in the beginning and end of the arguments
		for(int i = 0; i < args.length; i++) {
			args[i] = args[i].trim();
		}
		try {
			return newRep.execute(args);
		} catch (IOException | StatusRuntimeException e) {
			return e.getMessage();
		}
	} 
	
	// receives a command and arguments for it, returns error msg if
	// arguments number is wrong and null otherwise
	private String checkNumArgs(Command cmd, String[] words) {
		// verify if number of arguments is right
		if ( cmd.getArgsNumMin() > (words.length) ||
		 cmd.getArgsNumMax() < (words.length) ) {

		  return (
		  "Wrong number of args: "+ String.valueOf( words.length )+"\n"+
		  "Expected: "+cmd.getArgsNumMin()+" - "+cmd.getArgsNumMax());
		  
		}
		return null;
	}
	

	
}

