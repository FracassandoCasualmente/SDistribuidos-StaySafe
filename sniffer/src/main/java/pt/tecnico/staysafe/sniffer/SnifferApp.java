package pt.tecnico.staysafe.sniffer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
//import java.sql.Timestamp;
import pt.tecnico.staysafe.dgs.grpc.*;
import pt.tecnico.staysafe.dgs.client.DgsFrontend;

import com.google.protobuf.Timestamp;

import io.grpc.StatusRuntimeException;

public class SnifferApp {

	private static Boolean errorChecking(String [] words)
	{
       //check if we have the correct number of words
		if(!((words.length == 4 && !words[0].equals("sleep")) || (words.length == 2 && words[0].equals("sleep"))))
			return false;
     
		//check if we have valid timestamps
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

		try {
			LocalDateTime.parse(words[2], formatter);
			LocalDateTime.parse(words[3], formatter);
 		} catch (DateTimeParseException dtpe) {
     		return false;
 		}
     
		//check if we have valid person type and citizen id
		if(!((words[0].equals("infetado") || words[0].equals("nao-infetado")) && words[1].matches("[0-9]+")))
			return false;
 
 		return true;
	}

	public static void main(String[] args) {
		System.out.println(SnifferApp.class.getSimpleName());
		
		// check arguments relative to connection
		if (args.length < 4) {
			System.out.println("Argument(s) missing!");
			System.out.printf("Usage: java %s host port%n", SnifferApp.class.getName());
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
 
		//try to connect to server
		
		//try to join sniffer
		try(DgsFrontend frontend = new DgsFrontend(host,port);){
			frontend.snifferJoin(SnifferJoinRequest.newBuilder().setName(name).setAddress(address).build());
			
			// buffer of messages to send
			ArrayList<String[]> buffer = new ArrayList<>();
	 
			try(Scanner scanner = new Scanner(System.in))  { 

				//variables
				String [] words;
				ReportRequest obs;
				Timestamp entryTimestamp;
				Timestamp leaveTimestamp;
			    Date entryDate;
			    Date leaveDate;
			    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Integer counter = 0;
				
				// main cycle
				for ( String input = scanner.nextLine() ; ; input = scanner.nextLine() ) {
					
					//sends whatever is on buffer
					if(input.equals("") && buffer.isEmpty() == false)
					{

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
								frontend.report(obs);
								System.out.println("Report successful");
							}
							catch (Exception exp) {
								System.out.println("ERROR: while building dates in observation ("
								+ counter.toString() + ")");
								System.out.println(exp.getMessage());
								System.exit(-1);
							}
						}
						buffer = new ArrayList<>();
						continue;
					}
					
					//if there is nothing inside buffer, continue
					if(input.equals(""))
						continue;
					
					// check if we are reading a comment
					if ( input.charAt(0) == '#' ) 
						continue;
						
	     
					// split input by function name and args
					words = input.split(",");
	     
					//Removing the whitespaces in the beginning and end of the words
					for(int i = 0; i < words.length; i++)
						words[i] = words[i].trim();
	     
					//check if is to sleep
					if(words.length == 2)
					{
						if(words[1].matches("[0-9]+"))
						{
							try
							{
								Thread.sleep(Integer.parseInt(words[1]));
							} catch(Exception te)
							{
								System.out.println("ERROR: thread sleep exception");
							}
						}
						else
							System.out.println("Invalid Input!");
						continue;
					}
					
					//check if valid observation
					if(errorChecking(words) == false)
					{
						System.out.println("ERROR: Invalid Input!");
						continue;
					}
					
					//if everything is ok
					buffer.add(words);
	             
				} // end of main cycle
	   
			} // close try
			// EOF found, send what we have and close
			catch ( NoSuchElementException nsee ) {
				// found EOF! end of cycle
				frontend.close();
				System.exit(0);
			}
			
			frontend.close();

		} catch(StatusRuntimeException e)
		{
			//If sniffer already exists
			System.out.println(e.getStatus().getDescription());
			System.exit(-1);
		}

		
	}
	
}

