package pt.tecnico.staysafe.dgs.client;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import io.grpc.StatusRuntimeException;
import pt.tecnico.staysafe.dgs.grpc.*;

public class SnifferIT extends BaseIT {
	
	// static members
	private static final String VALID_SNIFFER = "Sniffer added: testSnifferJoinName,testSnifferJoinAddress";
	private static final String VALID_SNIFFER_NAME = "testSnifferJoinName";
	private static final String VALID_SNIFFER_ADDRESS = "testSnifferJoinAddress";
	private static final String VALID_SNIFFER_NAME_EXCEPTION = "testSnifferJoinExceptionName";
	private static final String VALID_SNIFFER_ADDRESS_EXCEPTION = "testSnifferJoinExceptionAddress";
	private static final String PING = "Server State: UP";
	
	
	// one-time initialization and clean-up
	@BeforeAll
	public static void oneTimeSetUp(){
		
	}

	@AfterAll
	public static void oneTimeTearDown() {
		
	}
	
	// initialization and clean-up for each test
	
	@BeforeEach
	public void setUp() {
		
		//clean the server state
		frontend.ctrlClear(ClearRequest.getDefaultInstance());
	}
	
	@AfterEach
	public void tearDown() {
	
	}
		
	// TESTS
	
	//Sniffer added with success
	@Test
	public void testSnifferJoin() {
		assertEquals(VALID_SNIFFER, frontend.snifferJoin(SnifferJoinRequest.newBuilder().
				setName(VALID_SNIFFER_NAME).setAddress(VALID_SNIFFER_ADDRESS).build()).getResult());
	}
	
	//Sniffer already registered
	@Test
	public void testSnifferJoinException()
	{
		//sample sniffer for this test
		frontend.snifferJoin(SnifferJoinRequest.newBuilder().
				setName(VALID_SNIFFER_NAME_EXCEPTION).setAddress(VALID_SNIFFER_ADDRESS_EXCEPTION).build()).getResult();
        
		assertThrows(StatusRuntimeException.class, () -> frontend.snifferJoin(SnifferJoinRequest.newBuilder().
				setName(VALID_SNIFFER_NAME_EXCEPTION).setAddress(VALID_SNIFFER_ADDRESS).build()).getResult());
	}
	
	//Getting a sniffer address by name
	@Test
	public void testSnifferInfo()
	{
		//registering a test sniffer
		frontend.snifferJoin(SnifferJoinRequest.newBuilder().
				setName(VALID_SNIFFER_NAME).setAddress(VALID_SNIFFER_ADDRESS).build());
		
		//getting it's address
		String address = frontend.snifferInfo(SnifferInfoRequest.newBuilder().
				setName(VALID_SNIFFER_NAME).build()).getAddress();
		
		assertEquals(VALID_SNIFFER_ADDRESS,address);
		
	}
	
	//Sniffer doesn't exist
	@Test
	public void testSnifferInfoException()
	{
		
		assertThrows(StatusRuntimeException.class, () -> frontend.snifferInfo(SnifferInfoRequest.newBuilder().
				setName(VALID_SNIFFER_NAME).build()).getAddress());
	}

	//Ping to server
	@Test
	public void testPing() {
		
		assertEquals(PING, frontend.ctrlPing(PingRequest.getDefaultInstance()).getResult());
	}

}
