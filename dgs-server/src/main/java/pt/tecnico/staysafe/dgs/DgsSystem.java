package pt.tecnico.staysafe.dgs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import pt.tecnico.staysafe.dgs.grpc.PersonType;


public class DgsSystem
{

	//The sniffer structure
	// keys: Name
	// values: Address
	private HashMap<String,String> _sniffers;
	
	//The observations structure
	private ArrayList<Observation> _obs;
	
	public DgsSystem()
	{
		_sniffers = new HashMap<String,String>();
		_obs = new ArrayList<Observation>();
	}
	
	/**  
	* @param None
	* @return String with all the observations in the system
	*/
	public String observationsToString()
	{
		String obsString = "";
		
		if(_obs.isEmpty())
			return "Empty\n";
			
		for(Observation obs: _obs)
		{
			obsString += obs.toString();
		}
		
		return obsString;
	}

	// --- Sniffer related stuff ---
	/**  
	* @param Name of the sniffer
	* @return Address of sniffer, null if it doesnt exist
	*/
	
	public String getSnifferAddress(String name) {

		return _sniffers.getOrDefault(name, null);
	}
	
	/**  
	* @param Name of the new sniffer
	* @return true if success (the name does not exist with different address),
	 false otherwise
	*/
	
	public Boolean joinSniffer(String newName, String newAddr) {
		// points to address of the sniffer with same name
		String conflictingSnifferAddr = _sniffers.get(newName);
		
		if ( conflictingSnifferAddr != null ) {
			// if conflictingSniffer has different address
			if ( !conflictingSnifferAddr.equals(newAddr) ) {
				return false;
			}
		}
		// no problems -> add sniffer to collection
		_sniffers.put(newName, newAddr);
		return true;
	}
	/**  
	* @param pretended Observation
	* @return true if success (the sniffer exists),
	 false otherwise
	*/
	public Boolean addReport(Observation newObs) {
		if ( !_sniffers.containsKey(newObs.getSnifferName()) ) {
			// the sniffer doesnt exist :(
			return false;
		}
		// the sniffer exists, lets add his observation
		_obs.add(newObs);
		return true;
	}
	/**  
	* @param target citizen's id
	* @return probability of being infected with corona-bixo
	*/
	public float individualInfectionProbability(long citizenId) {
		// find all places where citizen was
		float res = 0;
		for (Observation obs : _obs ) {
			if ( obs.getCitizenId() == citizenId ) {
				// convert citizen timestamp to seconds
				long mainET = obs.getLeaveTime().getTime()/1000;
				long mainLT = obs.getLeaveTime().getTime()/1000;
				// find all infected citizens that were with him
				for (Observation otherObs : _obs ) {
					if ( otherObs.getCitizenId() != citizenId &&
					  otherObs.getSnifferName() == obs.getSnifferName() ) {

						// this person was at the same place
						// lets check how much time they were together
						long otherET = otherObs.getLeaveTime().getTime()/1000;
						long otherEL = otherObs.getLeaveTime().getTime()/1000;
						// *********** NEEDS TO BE COMPLETED *****

					  }
				}
			}
		}
		return -1; // CHANGE IT! THIS IS JUST TO COMPILE
	}


}


class Observation {

	private final String _snifferName;
	private final Timestamp _insertionTime;
	private final PersonType _personType;
	private final long _citizenId;
	private final Timestamp _enterTime;
	private final Timestamp _leaveTime;
	
	public Observation(String name, Timestamp insertionTime, PersonType pt,
		long citizenId, Timestamp enterTime, Timestamp leaveTime) {
		
		_snifferName = name;
		_insertionTime = insertionTime;
		_personType = pt;
		_citizenId = citizenId;
		_enterTime = enterTime;
		_leaveTime = leaveTime;

	}
	
	public String getSnifferName() { return _snifferName; }
	public Timestamp getInsertionTime() { return _insertionTime; }
	public PersonType getPersonType() { return _personType; }
	public long getCitizenId() { return _citizenId; }
	public Timestamp getEnterTime() { return _enterTime; }
	public Timestamp getLeaveTime() { return _leaveTime; }
	
	//Returns the observation in a string format
	public String toString()
	{
		String obs = "";
		obs += _snifferName + ", " + _insertionTime.toString() + ", " + _personType.toString() + 
				", " + _citizenId + ", " + _enterTime.toString() + ", " + _leaveTime.toString() + "\n";
		
		return obs;
	}
	
}
