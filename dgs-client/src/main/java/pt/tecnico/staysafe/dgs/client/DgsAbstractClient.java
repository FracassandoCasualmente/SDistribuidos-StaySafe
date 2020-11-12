package pt.tecnico.staysafe.dgs.client;


import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.lang.RuntimeException;
import java.io.IOException;

import io.grpc.StatusRuntimeException;


public abstract class DgsAbstractClient {

	protected DgsFrontend _frontend;
	protected static Boolean _debug = false; // true if wants to print debugs
	protected ArrayList<Command> _commands;
	
	//client is to select the appropriate commands for the respective client
	public DgsAbstractClient(String host, int port, String client)
	{
		_commands = new ArrayList<>();
		_frontend = new DgsFrontend(host,port);
		
		if(client.equals("journalist"))
		{
			_commands.add(new MeanDevCommand(_frontend));
			_commands.add(new PercentilesCommand(_frontend));
		}
		
		else if(client.equals("researcher"))
		{
			_commands.add(new SingleProbCommand(_frontend));
			_commands.add(new MeanDevCommand(_frontend));
			_commands.add(new PercentilesCommand(_frontend));
		}
		else if (client.equals("DgsClient")) {
			_commands.add(new SingleProbCommand(_frontend));
			_commands.add(new MeanDevCommand(_frontend));
			_commands.add(new PercentilesCommand(_frontend));
			_commands.add(new SnifferInfoCommand(_frontend));
			_commands.add(new SnifferJoinCommand(_frontend));
			// report implemented in DgsClient methods
			// DEPRECATED, DOESNT WORK IN SHELL (Cause: space inside timestmap)
			//_commands.add(new ReportCommand(_frontend, SNIFFER_NAME));
			
		}
		else {
			throw new RuntimeException("Unknown type of client: "+client);
		}
		// add ctrl and help commands
		_commands.add(new PingCommand(_frontend));
		_commands.add(new ClearCommand(_frontend));
		_commands.add(new InitCommand(_frontend));
		_commands.add(new HelpCommand(_frontend,_commands));
		

	}

	// receives input string and returns 
	private final String[] parseInput(String inputString) throws IOException{
		// split input by function name and args
		String[] words = inputString.split(" ");

		if (words.length > 2) {
			throw new IOException("Inputs may have a maximum of 1 space!");
		}
     
		//Removing the whitespaces in the beginning and end of the words
		for(int i = 0; i < words.length; i++) {
			words[i] = words[i].trim();
		}
		
		// verify if called command exists
		Boolean validCmd = false;
		Command cmdCalled = null;
		for (Command c : _commands ) {
			if ( c.getName().equals(words[0]) ) {
				validCmd = true;
				cmdCalled = c;
				break;
			}
		}
		if (!validCmd) {
			throw new IOException("Invalid command: "+words[0]);
		}

		// debug args
		Integer i = 0;
		debug("args length (with command)= "+String.valueOf(words.length));
		for (String s : words) {
			debug("arg["+(i++)+"] = "+s);
		}

		String[] commandArgs = (words.length != 1) ? words[1].split(",") : new String[0];

		// continue to debug args
		i = 0;
		debug("args length (without command)= "+String.valueOf(commandArgs.length));
		for (String s : commandArgs) {
			debug("arg["+(i++)+"] = "+s);
		}
		// verify if number of arguments is right
		
		if ( cmdCalled.getArgsNumMin() > (commandArgs.length) ||
		  cmdCalled.getArgsNumMax() < (commandArgs.length) ) {
			throw new IOException(
			"Wrong number of args: "+ String.valueOf( commandArgs.length )+"\n"+
			"Expected: "+cmdCalled.getArgsNumMin()+" - "+cmdCalled.getArgsNumMax());
		}

		// if everything went right, return processed input arguments
		return words;

	}

	// parses and runs command
	// receives command name identifier and its arguments
	// returns result
	protected String runCommand(String input) throws IOException{
		// parse input generic problems
		// and process input
		// throws IOException if any error is detected
		debug("Entered in runCommand(), going to parse");
		String[] words = parseInput(input);
		debug("Parsed successful, continue runCommand()");

		// no exception, the input is generally fine

		// we know the command exists because of parseInput()
		// lets get our command instance
		Command cmdCalled = null;
		for (Command c : _commands ) {
			if ( c.getName().equals(words[0]) ) {
				cmdCalled = c;
				break;
			}
		}

		//parse and execute command related args
		debug("arguments:");
		debug("command_name= "+words[0]);
		if (words.length > 1) {
			debug("command_args= "+words[1]);
		}

		String[] commandArgs = (words.length != 1) ? words[1].split(",") : new String[0];
		String[] cmdArgs = commandArgs;
		String result;

		debug("going to execute");
		try {
			result = cmdCalled.execute(cmdArgs);
		} catch (IOException e) {
			throw new IOException("Invalid Input arguments!\n"+e.getMessage());
		} catch (StatusRuntimeException sre) {
			throw new IOException("Error from server: "+sre.getStatus().getDescription());
		}
		debug("ended execution");
		
		// return the result of the command	
		return result;
	}
	
	// read input until blank line or EOF and execute accordingly
	public final void run() {
		Scanner scanner = new Scanner(System.in);
		try  {
			// main cycle
			for ( String input = scanner.nextLine() ; !input.equals(""); input = scanner.nextLine() ) {
				
				// check if we are reading a comment
				if ( input.charAt(0) == '#' ) {
					continue;
				}
				
				
				// parses and executes input accordingly
				String result;
				try {
					result = runCommand(input);
				} catch (IOException ioe) {
					//result = ioe.getMessage();
					result = "ERROR: couldn't execute command.";
				}
				// print the result of the command
				System.out.println(result);
				
             
			} // end of main cycle
   
		} // close try
		// EOF found, close
		catch ( NoSuchElementException nsee ) {
			// found EOF! end of program
			debug("Found EOF, time to stop running.");
		}
		
		//If can't reach the server
		catch ( Exception e) {
			System.out.println("Unexpected ERROR:\n"+e.getMessage());
			e.printStackTrace();
		}
		finally {
			scanner.close();
		}
	}

	public static void debug(String debugMessage) {
		if (_debug) {
			System.out.println(debugMessage);
		}
	}
	
	//closes the connection
	public void close()
	{
		_frontend.close();
	}
	
}

