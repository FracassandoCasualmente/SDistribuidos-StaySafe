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
		// together 10 min (exp. res = 0.0066 -> 0.007)
		_dgsClient.report("IST","infetado,32,2001-08-20 09:30:00, 2001-08-20 09:40:00");
		_dgsClient.report("IST","nao-infetado,33,2001-08-20 09:30:00, 2001-08-20 10:30:00");
		// together 13 min (exp. res = 0.1192 -> 0.119)
		_dgsClient.report("FCUL","infetado,48,2002-08-20 09:30:00, 2002-08-20 09:43:00");
		_dgsClient.report("FCUL","nao-infetado,49,2002-08-20 09:30:00, 2002-08-20 10:30:00");
	}
	
	@AfterEach
	public void tearDown() {
		_dgsClient = null;
	}
		
	// tests 
	
	@Test
	public void test() {
		
		
	}

	@Test
	public void testSingleProb() {
		assertEquals("0.007", _dgsClient.executeCommand("single_prob 33"));
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
		// does mean_dev of 0.007, 0.119, 1.000, 1.000
		assertEquals("", _dgsClient.executeCommand("mean_dev"));
	}

	/*
	@Test
    public void testGetDefaultGreeting() {
		//  ( expected, what we are getting)
        assertEquals("Hello", example.getGreeting());
    }*/

}
