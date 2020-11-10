package pt.tecnico.staysafe.dgs.exception;

public class CitizenDoesNotExistException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public CitizenDoesNotExistException(Long citizenId) {
		super("ERROR: Citizen \""+citizenId+"\" does not exist.");
	}
}
