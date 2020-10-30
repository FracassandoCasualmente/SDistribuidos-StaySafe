package pt.tecnico.staysafe.dgs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import pt.tecnico.staysafe.dgs.grpc.PersonType;

class Observation
{
	private String snifferName;
	private Timestamp insertionTime;
	private PersonType personType;
	private long citizenId;
	private Timestamp enterTime;
	private Timestamp leaveTime;
	
}

public class DgsSystem
{

	//The sniffer structure
	private HashMap<String,String> sniffers;
	
	//The observations structure
	private ArrayList<Observation> obs;
	
	public DgsSystem()
	{
		sniffers = new HashMap<String,String>();
		obs = new ArrayList<Observation>();
	}
}
