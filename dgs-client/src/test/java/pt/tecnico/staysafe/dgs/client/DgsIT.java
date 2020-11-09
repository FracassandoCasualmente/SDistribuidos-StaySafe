package pt.tecnico.staysafe.dgs.client;

import org.junit.jupiter.api.*;

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
	}
	
	@AfterEach
	public void tearDown() {
		_dgsClient = null;
	}
		
	// tests 
	
	@Test
	public void test() {
		
		
	}

}
