package pt.tecnico.staysafe.dgs.exception;

public class SnifferAlreadyRegisteredException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public SnifferAlreadyRegisteredException()
	{
		super("ERROR: Sniffer already registered.");
	}
}
