package pt.tecnico.staysafe.dgs.client.exceptions;

import java.io.IOException;
//import java.lang.RuntimeException;


public class OutdatedReadException extends IOException {
	
	private static final long serialVersionUID = 1L;

	public OutdatedReadException() {
		super("No new updates");
	}
}