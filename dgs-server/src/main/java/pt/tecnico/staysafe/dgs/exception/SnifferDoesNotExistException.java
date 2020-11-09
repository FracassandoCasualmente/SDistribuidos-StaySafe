package pt.tecnico.staysafe.dgs.exception;

public class SnifferDoesNotExistException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public SnifferDoesNotExistException(String snifferName) {
		super("ERROR: Sniffer \""+snifferName+"\" does not exist.");
	}
}
