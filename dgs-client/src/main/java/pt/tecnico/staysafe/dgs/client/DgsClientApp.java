package pt.tecnico.staysafe.dgs.client;

import java.io.IOException;
import io.grpc.StatusRuntimeException;

public final class DgsClientApp extends DgsAbstractClient{
	
	public DgsClientApp (String host, Integer port) {
		super(host, port, "DgsClient");
	}
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
		
		DgsClientApp client = new DgsClientApp(host, port);

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
		String[] args = input.split(",");
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
	/*
	public String snifferJoin(String name, String addr) {
		String[] obs = args;
		SnifferJoinCommand snifferJoinCmd = new SnifferJoinCommand(_frontend, name, addr);

		// verify if number of arguments is right
		String res;
		if ( res = checkNumArgs(newRep, args) != null ) {
			// returned error msg instead of null
			return res;
		}
		  
		//Removing the whitespaces in the beginning and end of the words
		for(int i = 0; i < obs.length; i++) {
			obs[i] = obs[i].trim();
		}
		return snifferJoinCmd.execute(obs);
	}*/

	
}

