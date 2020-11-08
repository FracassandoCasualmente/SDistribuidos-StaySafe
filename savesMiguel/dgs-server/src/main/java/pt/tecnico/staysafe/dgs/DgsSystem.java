package pt.tecnico.staysafe.dgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.lang.Math;

import pt.tecnico.staysafe.dgs.grpc.PersonType;
import pt.tecnico.staysafe.dgs.grpc.Statistic;

import com.google.protobuf.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;


public class DgsSystem
{

	//The sniffer structure
	// keys: Name
	// values: Address
	private HashMap<String,String> _sniffers;
	
	//The observations structure
	private ArrayList<Observation> _obs;
	
  //Structures to facilitate the process of search
	private HashMap<Long, ArrayList<Observation>> _citizenSearch;
  private HashMap<String, ArrayList<Observation>> _snifferSearch;
	
	public DgsSystem()
	{
		_sniffers = new HashMap<String,String>();
		_obs = new ArrayList<Observation>();
		_citizenSearch = new HashMap<>();
    	_snifferSearch = new HashMap<>();
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
		// no problems -> add sniffer to collections
		_sniffers.put(newName, newAddr);
		_snifferSearch.put(newName, new ArrayList<>() );
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
    
    	/* ADD observation to maps */
    	List auxList = _snifferSearch.get( newObs.getSnifferName() ); // list with the observations of that sniffer
    	auxList.add( newObs ); // Add new observation to the list of that sniffer
    
    	// verify if citizen is already registered
    	if ( _citizenSearch.get( newObs.getCitizenId() ) == null ) {
    		// he's not registered, lets regist him
    		_citizenSearch.put( newObs.getCitizenId(), new ArrayList<>() );
    	}
    
    	// add observation to citizenId map
    
    	auxList = _citizenSearch.get( newObs.getCitizenId() ); // list with observations that contain this guy's name
		auxList.add( newObs );
		
		/* TESTE */
		if ( newObs == null) {
			System.out.println("addReport: É NULL!!! ");
		}
		return true;
	}
	/**  
	* @param target citizen's id
	* @return probability of being infected with corona-bixo
	*/
	public Double individualInfectionProbability(long citizenId) {
		
		Long maxTimeTogether = 0L;
		
		//TODO has to throw exception if the citizen doesn't exist
		if(_citizenSearch.get(citizenId) == null )
			return 0.0;
    	// find all places where citizen was
		for (Observation obs : _citizenSearch.get( citizenId ) ) {
			// convert citizen timestamp to seconds
			long mainET = obs.getEnterTime().getSeconds();
			long mainLT = obs.getLeaveTime().getSeconds();
      
			String currentSniffer = obs.getSnifferName();
			
			// find all citizens that were in this same sniffer
			for (Observation otherObs : _snifferSearch.get( currentSniffer ) ) {
				
				// this person was at the same sniffer
    
    		    // is this person infected? If not, skip
        		if ( otherObs.getPersonType() != PersonType.INFECTED ) {
          			continue;
        		}
        
				// get this person timestamps
				long otherET = otherObs.getEnterTime().getSeconds();
				long otherLT = otherObs.getLeaveTime().getSeconds();
				
        		// were they even togeter?
        		if ( mainET >= otherLT || otherET >= mainLT ) {
          			// yes, check next
          			continue;
        		}
        
        		// how long were they together?
        
        		Long begin = Math.max( mainET, otherET);
				Long end = Math.min( mainLT, otherLT );
				Long res = end - begin;
        		Long timeTogether = end - begin;
        
        		// should we add it?
        		if ( timeTogether > maxTimeTogether ) {
        			maxTimeTogether = timeTogether; // yes!
        		}
        
			} // end foreach of people in same sniffer
			
		} // end foreach of sniffers visited
    
		/* use formula */
		// convert from secs to minutes
		Double x = (double)maxTimeTogether/60; 
    	Double res = 1 /( 1 + Math.exp( 15 - x) );
		return res; 
	}
	
	/**  
	* @param statistic
	* @return aggregate infection probability
	*/
	public String aggregateInfectionProbability(Statistic stat)
	{
	  int mean = 0;
	  int count = 0;
	  double dev = 0;
	  double percentil50 = 0;
	  double percentil25 = 0;
	  double percentil75 = 0;
	  int index = 0;
	  int size = 0;
	  double gt = 0;
	  double gte = 0;
	  double wa = 0;
	  
	  if(stat == Statistic.MEAN_DEV)
	  {
		//calculate the mean probability of group infection
		for(long citizen_id: _citizenSearch.keySet())
		{
			mean += individualInfectionProbability(citizen_id);
			count++;
		}
		//ensure we don't divide by zero
		mean = (count == 0) ? mean : mean / count;
		
		//calculate the dev of group infection
		for(long citizen_id: _citizenSearch.keySet())
		{
			//(xi - mean)^2
			dev += Math.pow(individualInfectionProbability(citizen_id)-mean,2);
		}
		
		//ensure we dont divide by zero
		dev = (count == 0) ? dev : Math.sqrt(dev / count);
		
		//return the response
		return mean + "," + dev;
		
	  }
	  
	  //stat == PERCENTILES
	  else
	  {
		ArrayList<Double> prob = new ArrayList<Double>();
		
		for(long citizen_id: _citizenSearch.keySet())
		{
			prob.add(individualInfectionProbability(citizen_id));
		}
		//sort the probabilities
		Collections.sort(prob);
		
		//getting the size of prob
		size = prob.size();
		
		//we need a minimum of 3 observations
		if(size < 3)
		  return "0,0,0";
		
		//percentil-50
		index = (int) Math.round(0.50 * size);
		index++;
		gt = prob.get(index);
		gte =  (prob.get(--index) + gt) / 2;
		wa = (gt + gte) / 2;  
		percentil50 = wa;
		
		//percentil-25
		index = (int) Math.round(0.25 * size);
		index++;
		gt = prob.get(index);
		gte =  (prob.get(--index) + gt) / 2;
		wa = (gt + gte) / 2;  
		percentil25 = wa;
		
		//percentil-75
		index = (int) Math.round(0.75 * size);
		index++;
		gt = prob.get(index);
		gte =  (prob.get(--index) + gt) / 2;
		wa = (gt + gte) / 2;  
		percentil75 = wa;
		
		return percentil50 + "," + percentil25 + "," + percentil75;
		
	  
	  }
	}

	// TESTE debug the observations
	public void debugObservations() {
		System.out.println("Let's debug the List");
		for ( Observation obs : _obs ) {
			obs.debugFields();
		}
		System.out.println("Let's debug the citizenSearch");
		for ( List<Observation> obsList : _citizenSearch.values() ) {
			System.out.println("Debuging a new citizen list...");
			for ( Observation obs : obsList ) {
				obs.debugFields();
			}
		}
		
		
	}


}


class Observation {

	private final String _snifferName;
	private final com.google.protobuf.Timestamp _insertionTime;
	private final PersonType _personType;
	private final long _citizenId;
	private final com.google.protobuf.Timestamp _enterTime;
	private final com.google.protobuf.Timestamp _leaveTime;
	
	public Observation(String name, com.google.protobuf.Timestamp timestamp, PersonType pt,
		long citizenId, com.google.protobuf.Timestamp timestamp2, com.google.protobuf.Timestamp timestamp3) {
		
		_snifferName = name;
		_insertionTime = timestamp;
		_personType = pt;
		_citizenId = citizenId;
		_enterTime = timestamp2;
		_leaveTime = timestamp3;

	}
	
	public String getSnifferName() { return _snifferName; }
	public com.google.protobuf.Timestamp getInsertionTime() { return _insertionTime; }
	public PersonType getPersonType() { return _personType; }
	public long getCitizenId() { return _citizenId; }
	public com.google.protobuf.Timestamp getEnterTime() { return _enterTime; }
	public com.google.protobuf.Timestamp getLeaveTime() { return _leaveTime; }
	
	//Returns the observation in a string format
	public String toString()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String obs = "";
		
		obs += _snifferName + ", " + format.format(new Date(_insertionTime.getSeconds()*1000L)) + ", " + _personType.toString() + 
				", " + _citizenId + ", " + format.format(new Date(_enterTime.getSeconds()*1000L)) + ", " + 
				format.format(new Date(_leaveTime.getSeconds()*1000L));
		
		return obs;
	}
	
	// TESTE run a debug to detect corrupted input
	// returns a debug message
	public String debugFields() {
		System.out.println("snifferName = "+"\""+_snifferName+"\"");
		System.out.println("citizenId = "+"\""+_citizenId+"\"");
		return "";
	}
}