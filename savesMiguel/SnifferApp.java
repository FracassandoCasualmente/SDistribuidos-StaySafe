package pt.tecnico.staysafe.sniffer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import pt.tecnico.staysafe.dgs.grpc.*;
import pt.tecnico.staysafe.dgs.client.DgsFrontend;
import pt.tecnico.staysafe.dgs.client.DgsAbstractClient;

import com.google.protobuf.Timestamp;

public class SnifferApp extends DgsAbstractClient{
	// buffer of messages to send
	private List<String[]> buffer = new ArrayList<>();

	@Override
	private static Boolean errorChecking(String [] words)
	{
       //check if we have the correct number of words
		if(!((words.length == 4 && !words[0].equals("sleep")) || (words.length == 2 && words[0].equals("sleep"))))
			return false;

		// check if valid command
		Boolean validCommand = false;
		for (String c : _validCommands) {
			debug("Checking command: "+c);
			// found matching command
			if ( c.equals(words[0]) ) {
				validCommand = true;
				break;
			}
		}
		if (!validCommand) {
			System.out.println("Invalid command: "+words[0]);
			continue;
		}
     
		//check if we have valid timestamps
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

		try {
			LocalDateTime dateTime = LocalDateTime.parse(words[2], formatter);
			dateTime = LocalDateTime.parse(words[3], formatter);
 		} catch (DateTimeParseException dtpe) {
     		return false;
 		}
     
		//check if we have valid person type and citizen id
		if(!((words[0].equals("infetado") || words[0].equals("nao-infetado")) && words[1].matches("[0-9]+")))
			return false;
 
 		return true;
	}

	@Override
	// execute the command received in the form of String[]
	public void execute(String[] words) {

	}

	public static void main(String[] args) {
		System.out.println(SnifferApp.class.getSimpleName());
		
		_debug = false; // set debugging messages OFF

		// check arguments
		if (args.length < 4) {
			debug("Argument(s) missing!");
			debug("Usage: java %s host port%n", SnifferApp.class.getName());
			return;
		}
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
 
		//Connection parameters
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
 
		// sniffer parameters
		final String name = args[2];
		// build address
		String snifferAux = "";
		for ( int i = 3; i < args.length; i++) {

			snifferAux+= " "+args[i];
		}
		final String address = snifferAux.substring(1, snifferAux.length());
 
		debug("My name: "+name+"\n"+
							"My address: "+address); // DEBUG

		_frontend = new DgsFrontend(host, port);
		
		try{
			_frontend.snifferJoin(SnifferJoinRequest.newBuilder().setName(name).setAddress(address).build());
		} catch(Exception e)
		{
			// TODO Handle the future exceptions
			System.out.println("Error while joining sniffer: "+e.getMessage());;
		}
		
		run(); // read input and parse it until EOF or blank line
			
		/* All input was read, lets prepare to send it */
		ReportRequest obs;
		ReportResponse response;
		Timestamp entryTimestamp;
		Timestamp leaveTimestamp;
	    Date entryDate;
		Date leaveDate;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Integer counter = 0;
		for ( String[] observation : buffer ) {

			// build google Timestamp
			try {
				
		        // create Date objects
		       	entryDate = format.parse( observation[2] );
		       	leaveDate = format.parse( observation[3] );
				
		       	// adapt to Timestamp objects
		       	entryTimestamp = Timestamp.newBuilder().setSeconds( entryDate.getTime()/1000L ).buildPartial();
		        leaveTimestamp = Timestamp.newBuilder().setSeconds( leaveDate.getTime()/1000L ).buildPartial();
		       	
				// build observation object
				obs = ReportRequest.newBuilder().setSnifferName(name).setType( observation[0].equals("infetado") ? PersonType.INFECTED : PersonType.NOT_INFECTED).
					setCitizenId( Integer.parseInt(observation[1]) ).setEnterTime( entryTimestamp  ).setLeaveTime( leaveTimestamp ).
					build();
					
				// send report
				System.out.println("Sending observation ("+(counter++).toString()+")...");
				response = _frontend.report(obs);
			}
			catch (Exception exp) {
				System.out.println("Error while building dates in observation ("
				 + counter.toString() + ")");
				System.out.println(exp.getMessage());
				System.exit(-1);
			}
		}
	}
	
}

