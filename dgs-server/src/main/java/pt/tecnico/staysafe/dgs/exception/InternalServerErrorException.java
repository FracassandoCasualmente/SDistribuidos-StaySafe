package pt.tecnico.staysafe.dgs.exception;

public class InternalServerErrorException extends Exception{

private static final long serialVersionUID = 1L;
	
	public InternalServerErrorException() {
		super("ERROR: internal server problem. Server reseted.");
	}
}
