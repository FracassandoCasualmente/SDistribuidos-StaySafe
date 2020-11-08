package pt.tecnico.staysafe.dgs.client;

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

import com.google.protobuf.Timestamp;

public abstract class DgsAbstractClient {
	protected DgsFrontend _frontend;
	protected Boolean _debug = false; // true if wants to print debugs

	protected abstract Boolean errorChecking(String [] words);

	protected abstract void execute(String[] words); // executes a command

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
				String[] words = input.split(",");
     
				//Removing the whitespaces in the beginning and end of the words
				for(int i = 0; i < words.length; i++) {
					words[i] = words[i].trim();
				}
				
				//check if valid input
				if(errorChecking(words) == false)
				{
					System.out.println("Invalid Input!");
					continue;
				}
     
				//if everything is ok
				this.execute(words);
				
             
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

