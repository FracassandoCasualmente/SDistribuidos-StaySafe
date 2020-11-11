package pt.tecnico.staysafe.dgs.client;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Date;
import java.util.Arrays;

import java.io.IOException;

import pt.tecnico.staysafe.dgs.grpc.*;
import io.grpc.StatusRuntimeException;
import com.google.protobuf.Timestamp;

import pt.tecnico.staysafe.dgs.client.DgsAbstractClient; // use debug

public abstract class Command {
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
	    _fe.ctrlInit(initRequest);
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
		String strDouble = "";
		
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

			// convert this probability to string
			DgsAbstractClient.debug("debug prob as double: "+response.toString());
			strDouble = String.format("%.4f", response).replace(",",".");
			res += strDouble + "\n";
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
	
	public ReportCommand(DgsFrontend fe)
	{
		super("report", 5, 5, fe);
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
	public String getHelp() {
		return "report - sends a sniffer report to server\n"+
		"Arguments syntax: snifferName,infectedState,citizenID,Timestamp of entrance,Timestamp of leaving";
	}
	
	@Override
	public String execute(String [] args) throws IOException, StatusRuntimeException {	
 
		//variables
		ReportRequest obs;
		Timestamp entryTimestamp;
		Timestamp leaveTimestamp;
	    Date entryDate;
	    Date leaveDate;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// separate sniffer name from observation args
		String [] words = Arrays.copyOfRange(args, 1, args.length);
		String _snifferName = args[0];
		
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
		super("sniffer_join", 2, Integer.MAX_VALUE, fe);
	}

	@Override
	public String getHelp() {
		return "sniffer_join - Registers a sniffer, receives a name and address";
	}

	@Override
	public String execute(String[] args)  throws IOException, StatusRuntimeException {
		
		String name = args[0];
		String addr = args[1];
		
		for (String aux : Arrays.copyOfRange(args, 2, args.length)) {
			addr += " "+aux;
		}

		// returns exception if problem occurs
		_fe.snifferJoin(SnifferJoinRequest.newBuilder().setName(name).setAddress(addr).build());
		return "SnifferJoin successful";
	}
}