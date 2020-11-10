package pt.tecnico.staysafe.dgs.client;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Arrays;

import java.lang.RuntimeException;
import java.io.IOException;

import pt.tecnico.staysafe.dgs.grpc.*;
import io.grpc.StatusRuntimeException;
import com.google.protobuf.Timestamp;



public abstract class DgsAbstractClient {

	protected DgsFrontend _frontend;
	protected Boolean _debug = false; // true if wants to print debugs
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

		// verify if number of arguments is right
		if ( cmdCalled.getArgsNumMin() > (words.length-1) ||
		  cmdCalled.getArgsNumMax() < (words.length-1) ) {
			throw new IOException(
			"Wrong number of args: "+ String.valueOf( words.length )+"\n"+
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
		String[] words = parseInput(input);

		// no exception, the input is generally fine

		// we know the command exists because of parseInput()
		Command cmdCalled = null;
		for (Command c : _commands ) {
			if ( c.getName().equals(words[0]) ) {
				cmdCalled = c;
				break;
			}
		}
		//parse and execute command related args
		String result;
		try {
			result = cmdCalled.execute(words[words.length > 1 ? 1:0].split(","));
		} catch (IOException e) {
			throw new IOException("Invalid Input arguments!\n"+e.getMessage());
		} catch (StatusRuntimeException sre) {
			throw new IOException("Error:\n"+
			sre.getStatus().getDescription());
		}
		
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
					result = ioe.getMessage();
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
			System.out.println("ERROR: Server Unreachable.");
		}
		finally {
			scanner.close();
		}
	}

	protected void debug(String debugMessage) {
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

