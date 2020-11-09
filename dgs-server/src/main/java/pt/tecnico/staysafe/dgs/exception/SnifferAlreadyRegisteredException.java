package pt.tecnico.staysafe.dgs.exception;

public class SnifferAlreadyRegisteredException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public SnifferAlreadyRegisteredException(String name, String addr, String newAddr)
	{
		super("ERROR: Sniffer already registered with the same name and different address:\n"+
		"name: "+name+"\n"+
		"original address: "+addr+"\n"+
		"failed address: "+newAddr);
	}
}
