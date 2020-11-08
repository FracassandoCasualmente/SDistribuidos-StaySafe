package pt.tecnico.staysafe.dgs.client;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import pt.tecnico.staysafe.dgs.grpc.*;
import pt.tecnico.staysafe.dgs.client.DgsFrontend;

import com.google.protobuf.Timestamp;

public abstract class DgsAbstractClient {

	protected DgsFrontend _frontend;
	protected Boolean _debug = false; // true if wants to print debugs
	protected ArrayList<Command> _commands;

	// read input until blank line or EOF and execute accordingly
	public final void run() {
		Scanner scanner = new Scanner(System.in);
		try  {
			// main cycle
			for ( String input = scanner.nextLine() ; !input.equals(""); input = scanner.nextLine() ) {
				
				// check if we are reading a comment
				if ( input.charAt(0) == '#' ) 
					continue;
     
				// split input by function name and args
				String[] words = input.split(" ");
     
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
					System.out.println("Invalid command: "+words[0]);
					continue;
				}

				// verify if number of arguments is right
				if ( cmdCalled.getArgsNumMin() >= (words.length -1) ||
				  cmdCalled.getArgsNumMax() <= (words.length -1) ) {
					System.out.println(
					"Wrong number of args: "+ String.valueOf( words.length-1 )+"\n"+
					"Expected: "+cmdCalled.getArgsNumMin()+" - "+cmdCalled.getArgsNumMax());
				}
				//parse and execute input args
				String result;
				try {
					result = cmdCalled.execute(words[1].split(","));
				} catch (IOException e) {
					System.out.println("Invalid Input arguments!\n"+e.getMessage());
					continue;
				}
				// print the result of the command
				System.out.println(result);
				
             
			} // end of main cycle
   
		} // close try
		// EOF found, close
		catch ( NoSuchElementException nsee ) {
			// found EOF! end of cycle
			System.exit(0);
		}
		catch ( Exception e) {
			System.out.println("Unexpected exception occurred: "+e.getMessage());
			System.exit(-1);
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
	
}

abstract class Command {
	protected final String _name;
	protected final Integer _argsNumMin;
	protected final Integer _argsNumMax;
	protected final DgsFrontend _fe;

	public Command(String name, Integer argsNumMin, Integer argsNumMax, DgsFrontend fe) {
		_name = name;
		_argsNumMin = argsNumMin;
		_argsNumMax = argsNumMax;
		_fe = fe;
	}

	public final String getName() { return _name;}
	public final Integer getArgsNumMin() { return _argsNumMin;}
	public final Integer getArgsNumMax() { return _argsNumMax;}

	// returns help message related to this command
	abstract public String getHelp();

	// executes the command and returns result
	abstract public String execute(String[] args) throws IOException;
}

class HelpCommand extends Command{
	List<Command> _availableCmds;

	public HelpCommand(DgsFrontend fe, List<Command> availableCmds) {
		super("help", 0, 1, fe);
		_availableCmds = availableCmds;
	}

	@Override
	public String getHelp() {
		String res = "--Available Commands--";
		for ( Command cmd : _availableCmds ) {
			res += "\n"+cmd.getName();
		}
		return res;
	}

	@Override
	public String execute(String[] args) throws IOException{
		// does the requested command exist?
		Boolean cmdExists = false;
		Command foundCmd = null;
		for (Command cmd : _availableCmds) {
			if ( cmd.getName().equals(args[0]) ) {
				cmdExists = true;
				foundCmd = cmd;
				break;
			}
		}
		if (!cmdExists) {
			throw new IOException("Command \""+args[0]+"\" does not exist");
		}
		return foundCmd.getHelp();
	}
}

class SingleProbCommand extends Command{
	public SingleProbCommand(DgsFrontend fe) {
		super("single_prob", 1, Integer.MAX_VALUE, fe);
	}
	@Override
	public String getHelp() {
		return "Receives 1 or more Citizen ID's and returns their individual infection probability";
	}

	@Override
	public String execute(String[] args) throws IOException{
		// parse args
		Long citizenId;
		IndividualInfectionProbabilityRequest request;
		Double response;
		String res = "";
		for (String s : args) {
			try {
				citizenId = Long.valueOf( s );
			} catch (NumberFormatException e) { // if not convertable to long
				throw new IOException("\""+s+"\" not convertable to Long!");
			}
			// valid id, request his individual prob
			request = IndividualInfectionProbabilityRequest.newBuilder().
			 setCitizenId(citizenId).build();
			response = _fe.individualInfectionProbability(request).getProbability();
			res += response.toString() + "\n";
		}
		return res;
	}
}

