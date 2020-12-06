package pt.tecnico.staysafe.dgs.client;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;

public class DgsIT extends BaseIT {
	
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
		_dgsClient.close();
		_dgsClient = null;
	}
		
	// tests 
	
	@Test
	public void test() {
		
		
	}

	@Test
	public void testPing() {
		
		assertEquals("Server State: UP", _dgsClient.executeCommand("ctrl_ping"));
	}

	@Test
	public void testInit() {
		
		assertEquals("ctrlInit executed", _dgsClient.executeCommand("ctrl_init"));
	}

	@Test
	public void testClear() {
		
		assertEquals("Server cleared with success!", _dgsClient.executeCommand("ctrl_clear"));
	}

	

	

}
