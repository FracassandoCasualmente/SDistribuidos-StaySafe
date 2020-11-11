package pt.tecnico.staysafe.dgs.client;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;

public class SnifferIT extends BaseIT {
	
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
	public void testPing() {
		
		assertEquals("Server State: UP", _dgsClient.executeCommand("ping"));
	}

	/*
	@Test
    public void testGetDefaultGreeting() {
		//  ( expected, what we are getting)
        assertEquals("Hello", example.getGreeting());
    }*/

}
