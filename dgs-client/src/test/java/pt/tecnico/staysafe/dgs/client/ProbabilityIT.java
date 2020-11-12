package pt.tecnico.staysafe.dgs.client;

import org.junit.jupiter.api.*;

//import pt.tecnico.staysafe.dgs.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProbabilityIT extends BaseIT {
	
	// static members

	
	
	// one-time initialization and clean-up
	@BeforeAll
	public static void oneTimeSetUp(){
		
	}

	@AfterAll
	public static void oneTimeTearDown() {
		
	}
	

	// non-static members
	private DgsClientApp _dgsClient;
	// initialization and clean-up for each test
	
	@BeforeEach
	public void setUp() {
		_dgsClient = new DgsClientApp("localhost", 8080);
		_dgsClient.executeCommand("ctrl_clear");
		_dgsClient.executeCommand("sniffer_join IST,Taguspark");
		_dgsClient.executeCommand("sniffer_join FCUL,lá_ao_pé_do_Sporting");
		// together 10 min (exp. res = 0.0066 -> 0.007)
		_dgsClient.report("IST","infetado,32,2001-08-20 09:30:00, 2001-08-20 09:40:00");
		_dgsClient.report("IST","nao-infetado,33,2001-08-20 09:30:00, 2001-08-20 10:30:00");
		// together 13 min (exp. res = 0.1192 -> 0.119)
		_dgsClient.report("FCUL","infetado,48,2002-08-20 09:30:00, 2002-08-20 09:43:00");
		_dgsClient.report("FCUL","nao-infetado,49,2002-08-20 09:30:00, 2002-08-20 10:30:00");
		// together 14 min (exp. res = 0.268941 -> 0.269)
		_dgsClient.report("FCUL","infetado,23,2003-08-20 09:30:00, 2003-08-20 09:44:00");
		_dgsClient.report("FCUL","nao-infetado,24,2003-08-20 09:30:00, 2003-08-20 10:30:00");
		// together 15 min (exp. res = 0.5 -> 0.500)
		_dgsClient.report("IST","infetado,15,2004-08-20 09:30:00, 2004-08-20 09:45:00");
		_dgsClient.report("IST","nao-infetado,16,2004-08-20 09:30:00, 2004-08-20 10:30:00");


	}
	
	@AfterEach
	public void tearDown() {
		_dgsClient.close();
		_dgsClient = null;
	}
		
	// tests 
	
	@Test
	public void test() {
		
		
	}

	@Test
	public void testSingleProbNotInfectedCitizen() {
		assertEquals("0.007", _dgsClient.executeCommand("single_prob 33"));
	}

	@Test
	public void testSingleProbInfectedCitizen() {
		// 32 is infected, prob should be 1.000
		assertEquals("1.000", _dgsClient.executeCommand("single_prob 32"));
	}

	@Test
	public void testSingleProbNoCitizen() {
		// tests probabiliy of citizen that doesnt exist
		Long citizenId = 100L; // 100 is a citizen that does not exist
		assertEquals("Error from server: "+"ERROR: Citizen \""+citizenId+
		"\" does not exist.", _dgsClient.executeCommand("single_prob "+citizenId));
	}

	@Test
	public void testMeanDev() {
		// does mean_dev of 0.007, 0.119, 0.269, 0.5
		// (0.007+0.119+0.269+0.5)/4 = 0.22375 -> 0.224
		// standart dev is aprox. 0.185 via calculator
		assertEquals("0.224,0.185", _dgsClient.executeCommand("mean_dev"));
	}
	@Test
	public void testPercentiles() {
		// calculates percentiles-50,25 and 75 of 0.007, 0.119, 0.269, 0.5
		// (weighted average)
		assertEquals("0.232,0.091,0.442", _dgsClient.executeCommand("percentiles"));
	}

}
