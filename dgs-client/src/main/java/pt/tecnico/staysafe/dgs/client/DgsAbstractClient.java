package pt.tecnico.staysafe.dgs.client;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.lang.RuntimeException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

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
		}
		else {
			throw new RuntimeException("Unknown type of client: "+client);
		}
		
		_commands.add(new PingCommand(_frontend));
		_commands.add(new HelpCommand(_frontend,_commands));
		_commands.add(new ClearCommand(_frontend));

	}
	
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
				if ( cmdCalled.getArgsNumMin() > (words.length-1) ||
				  cmdCalled.getArgsNumMax() < (words.length-1) ) {
					System.out.println(
					"Wrong number of args: "+ String.valueOf( words.length )+"\n"+
					"Expected: "+cmdCalled.getArgsNumMin()+" - "+cmdCalled.getArgsNumMax());
					continue;
				}
				//parse and execute input args
				String result;
				try {
					result = cmdCalled.execute(words[words.length > 1 ? 1:0].split(","));
				} catch (IOException e) {
					System.out.println("Invalid Input arguments!\n"+e.getMessage());
					continue;
				} catch (StatusRuntimeException sre) {
					System.out.println("Error:\n"+
					sre.getStatus().getDescription());
					continue;
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
	abstract public String execute(String[] args) throws IOException, StatusRuntimeException;
}

//help
class HelpCommand extends Command{
	List<Command> _availableCmds;

	public HelpCommand(DgsFrontend fe, List<Command> availableCmds) {
		super("help", 0, 0, fe);
		_availableCmds = availableCmds;
	}

	@Override
	public String getHelp() {
		
		return "help - Shows the available commands and the respective arguments";
	}

	@Override
	public String execute(String[] args) throws IOException, StatusRuntimeException{

		String commandList = "\nCOMMANDS\n--------\n";
		
		for(Command cmd: _availableCmds)
		{
			commandList += cmd.getHelp() + "\n";
		}
		
		return commandList;
	}
}

//ping
class PingCommand extends Command
{
	public PingCommand(DgsFrontend fe)
	{
		super("ctrl_ping", 0, 0, fe);
	}
	
	@Override
	public String getHelp()
	{
		return "ctrl_ping - Checks if the server is up and running";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException
	{
	    PingRequest pingRequest = PingRequest.getDefaultInstance();
	    PingResponse pingResponse = _fe.ctrlPing(pingRequest);
		return pingResponse.getResult();
	}
}

//mean_dev
class MeanDevCommand extends Command
{
	public MeanDevCommand(DgsFrontend fe)
	{
		super("mean_dev",0,0,fe);
	}
	
	@Override
	public String getHelp()
	{
		return "mean_dev - Returns the mean and standard deviation of the probabilities of any person that's not declared infected be actually infected.";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException
	{
		AggregateInfectionProbabilityRequest aipRequest = AggregateInfectionProbabilityRequest.newBuilder().setStatistic(Statistic.MEAN_DEV).build();
		AggregateInfectionProbabilityResponse aipResponse = _fe.aggregateInfectionProbability(aipRequest);
		return aipResponse.getResult();
	}
}

//percentiles
class PercentilesCommand extends Command
{
	public PercentilesCommand(DgsFrontend fe)
	{
		super("percentiles",0,0,fe);
	}
	
	@Override
	public String getHelp()
	{
		return "percentiles - Returns the percentile-50, percentile-25 and percentile-75 of the probabilites of any person not declared infected be actually infected.";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException
	{
		AggregateInfectionProbabilityRequest aipRequest = AggregateInfectionProbabilityRequest.newBuilder().setStatistic(Statistic.PERCENTILES).build();
		AggregateInfectionProbabilityResponse aipResponse = _fe.aggregateInfectionProbability(aipRequest);
		return aipResponse.getResult();
	}
}

//clear
class ClearCommand extends Command
{
	public ClearCommand(DgsFrontend fe)
	{
		super("ctrl_clear",0,0,fe);
	}
	
	@Override
	public String getHelp()
	{
		return "ctrl_clear - Clears the server state.";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException
	{
		ClearRequest clearRequest = ClearRequest.getDefaultInstance();
		ClearResponse clearResponse = _fe.ctrlClear(clearRequest);
		return clearResponse.getResult();
	}
}

// ctrl init
class InitCommand extends Command {

	public InitCommand(DgsFrontend fe)
	{
		super("ctrl_init", 0, 0, fe);
	}
	
	@Override
	public String getHelp()
	{
		return "ctrl_init - Initializes server to predefined state";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException
	{
	    InitRequest initRequest = InitRequest.getDefaultInstance();
	    InitResponse initResponse = _fe.ctrlInit(initRequest);
		return "ctrlInit executed";
	}
}

// single_prob
class SingleProbCommand extends Command{
	public SingleProbCommand(DgsFrontend fe) {
		super("single_prob", 1, Integer.MAX_VALUE, fe);
	}
	@Override
	public String getHelp() {
		return "single_prob - Receives 1 or more Citizen ID's and returns their individual infection probability";
	}

	@Override
	public String execute(String[] args) throws IOException, StatusRuntimeException {
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
		return res.substring(0, res.length() - 2); // remove last \n
	}
}


// sniffer info command
class SnifferInfoCommand extends Command {

	public SnifferInfoCommand(DgsFrontend fe)
	{
		super("sniffer_info", 1, 1, fe);
	}
	
	@Override
	public String getHelp()
	{
		return "sniffer_info - Receives a sniffer name and returns it's address.";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException {		
	    SnifferInfoRequest request = SnifferInfoRequest.newBuilder().setName(args[0]).build();
	    SnifferInfoResponse response = _fe.snifferInfo( request );
		return response.getAddress();
	}
}

//for sniffer simulation
class ReportCommand extends Command {

	private final String _snifferName;
	
	public ReportCommand(DgsFrontend fe, String snifferName)
	{
		super("report", 4, 4, fe);
		_snifferName = snifferName;
	}
	
	private void errorChecking(String [] words) throws IOException {
	
     
		//check if we have valid timestamps
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

		try {
			LocalDateTime dateTime1 = LocalDateTime.parse(words[2], formatter);
			LocalDateTime dateTime2 = LocalDateTime.parse(words[3], formatter);
			if (dateTime2.isBefore(dateTime1)) {
				throw new IOException("ERROR: The Timestamp of leaving happens before the Timestamp of entrance");
			}
 		} catch (DateTimeParseException dtpe) {
			throw new IOException("ERROR: Invalid timestamp: "+dtpe.getMessage());
		}
		
		
     
		//check if we have valid person type and citizen id
		if(!((words[0].equals("infetado") || words[0].equals("nao-infetado")) && words[1].matches("[0-9]+")))
			throw new IOException("ERROR: Invalid type of person type: "+words[0]);
 
 		return;
	}
	
	@Override
	public String getHelp()
	{
		return "report - sends a sniffer report to server\n"+
		"Arguments syntax: infectedState,citizenID,Timestamp of entrance,Timestamp of leaving";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException {	
 
		//variables
		String [] words = args;
		ReportRequest obs;
		Timestamp entryTimestamp;
		Timestamp leaveTimestamp;
	    Date entryDate;
	    Date leaveDate;
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		//Removing the whitespaces in the beginning and end of the words
		for(int i = 0; i < words.length; i++)
		words[i] = words[i].trim();
	
		//check if valid observation
		// if not, it will throw IOException
		errorChecking(words);

		// build google Timestamp
		try {
			String[] observation = words;
	        // create Date objects
	       	entryDate = format.parse( observation[2] );
	       	leaveDate = format.parse( observation[3] );
			
	       	// adapt to Timestamp objects
	       	entryTimestamp = Timestamp.newBuilder().setSeconds( entryDate.getTime()/1000L ).buildPartial();
	     	leaveTimestamp = Timestamp.newBuilder().setSeconds( leaveDate.getTime()/1000L ).buildPartial();
	       	
			// build observation object
			obs = ReportRequest.newBuilder().setSnifferName(_snifferName).setType( observation[0].equals("infetado") ? PersonType.INFECTED : PersonType.NOT_INFECTED).
				setCitizenId( Integer.parseInt(observation[1]) ).setEnterTime( entryTimestamp  ).setLeaveTime( leaveTimestamp ).
				build();
		} catch (Exception exp) {
			throw new IOException("ERROR: while building dates: "+exp.getMessage());
		}

		// send report
		_fe.report(obs); // returns exception if error occurs
		return "Report successful";

	}
}

// snifferJoin command
class SnifferJoinCommand extends Command {
	public SnifferJoinCommand(DgsFrontend fe) {
		super("sniffer_join", 2, 2, fe);
	}

	@Override
	public String getHelp() {
		return "sniffer_join - Registers a sniffer, receives a name and address";
	}

	@Override
	public String execute(String[] args)  throws IOException, StatusRuntimeException {
		String name = args[0];
		String addr = args[1];
		// returns exception if problem occurs
		_fe.snifferJoin(SnifferJoinRequest.newBuilder().setName(name).setAddress(addr).build());
		return "SnifferJoin successful";
	}
}