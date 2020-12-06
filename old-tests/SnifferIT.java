package pt.tecnico.staysafe.dgs.client;

import org.junit.jupiter.api.*;

import com.google.protobuf.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.grpc.StatusRuntimeException;
import pt.tecnico.staysafe.dgs.grpc.*;

public class SnifferIT extends BaseIT {
	
	// static members
	private static final String VALID_SNIFFER = "Sniffer added: testSnifferJoinName,testSnifferJoinAddress";
	private static final String VALID_SNIFFER_NAME = "testSnifferJoinName";
	private static final String VALID_SNIFFER_ADDRESS = "testSnifferJoinAddress";
	private static final String VALID_SNIFFER_NAME_EXCEPTION = "testSnifferJoinExceptionName";
	private static final String VALID_SNIFFER_ADDRESS_EXCEPTION = "testSnifferJoinExceptionAddress";
	
	private static final String VALID_OBSERVATION_SNIFFER = "sniffer_test";
	private static final String VALID_OBSERVATION_ENTERDATE = "2020-11-12 17:15:00";
	private static final String VALID_OBSERVATION_LEAVEDATE = "2020-11-12 17:30:00";
	private static final String VALID_REPORT = "Observation added: ";
	private static final Long 	VALID_OBSERVATION_CITIZENID = 93L;
	private static final PersonType VALID_OBSERVATION_PERSON = PersonType.INFECTED;


	private static final String PING = "Server State: UP";
	
	
	// one-time initialization and clean-up
	@BeforeAll
	public static void oneTimeSetUp(){
		
	}

	@AfterAll
	public static void oneTimeTearDown() {
		frontend.close();
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
	
	//Getting a sniffer address by name with success
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
	
	//Registering an observation with success
	@Test
	public void testAddReport()
	{
		
		//creating the timestamps format
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // create Date objects
		Date entryDate = null;
		Date leaveDate = null;
		Date insertionDate = null;
		try {
	       	entryDate = format.parse(VALID_OBSERVATION_ENTERDATE);
	       	leaveDate = format.parse(VALID_OBSERVATION_LEAVEDATE);
	       	insertionDate = java.util.Calendar.getInstance().getTime();
		}catch(ParseException pe)
		{
			System.out.println("ERROR: parsing dates failed. Wrong input.");
			System.exit(-1);
		}

		
       	// adapt to Timestamp objects
       	Timestamp entryTimestamp = Timestamp.newBuilder().setSeconds( entryDate.getTime()/1000L ).buildPartial();
     	Timestamp leaveTimestamp = Timestamp.newBuilder().setSeconds( leaveDate.getTime()/1000L ).buildPartial();
     	Timestamp insertionTimestamp = Timestamp.newBuilder().setSeconds( insertionDate.getTime()/1000L ).buildPartial();
     	
     	String valid_observation_insertion_time = format.format(new Date(insertionTimestamp.getSeconds()*1000L));

     	//initializing server so we can find our sniffer
     	frontend.ctrlInit(InitRequest.getDefaultInstance());
     	
		//validation string
		String validation = VALID_REPORT + VALID_OBSERVATION_SNIFFER + ", " + valid_observation_insertion_time + ", " +
				VALID_OBSERVATION_PERSON + ", " + VALID_OBSERVATION_CITIZENID + ", " + VALID_OBSERVATION_ENTERDATE + ", " +
				VALID_OBSERVATION_LEAVEDATE + "\n";
		
		ReportRequest observation = ReportRequest.newBuilder().setSnifferName(VALID_OBSERVATION_SNIFFER).setType( VALID_OBSERVATION_PERSON).
				setCitizenId( VALID_OBSERVATION_CITIZENID ).setEnterTime( entryTimestamp  ).setLeaveTime( leaveTimestamp ).
				build();
		assertEquals(validation,frontend.report(observation).getResult());
	}
	
	//Sniffer doesn't exist
	@Test
	public void testAddReportException()
	{
		//creating the timestamps format
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // create Date objects
		Date entryDate = null;
		Date leaveDate = null;
		Date insertionDate = null;
		try {
	       	entryDate = format.parse(VALID_OBSERVATION_ENTERDATE);
	       	leaveDate = format.parse(VALID_OBSERVATION_LEAVEDATE);
	       	insertionDate = java.util.Calendar.getInstance().getTime();
		}catch(ParseException pe)
		{
			System.out.println("ERROR: parsing dates failed. Wrong input.");
			System.exit(-1);
		}
		
       	// adapt to Timestamp objects
       	Timestamp entryTimestamp = Timestamp.newBuilder().setSeconds( entryDate.getTime()/1000L ).buildPartial();
     	Timestamp leaveTimestamp = Timestamp.newBuilder().setSeconds( leaveDate.getTime()/1000L ).buildPartial();
     	Timestamp insertionTimestamp = Timestamp.newBuilder().setSeconds( insertionDate.getTime()/1000L ).buildPartial();
     	
     	String valid_observation_insertion_time = format.format(new Date(insertionTimestamp.getSeconds()*1000L));
     	
		//validation string
		String validation = VALID_REPORT + VALID_OBSERVATION_SNIFFER + ", " + valid_observation_insertion_time + ", " +
				VALID_OBSERVATION_PERSON + ", " + VALID_OBSERVATION_CITIZENID + ", " + VALID_OBSERVATION_ENTERDATE + ", " +
				VALID_OBSERVATION_LEAVEDATE + "\n";
		
		ReportRequest observation = ReportRequest.newBuilder().setSnifferName(VALID_OBSERVATION_SNIFFER).setType( VALID_OBSERVATION_PERSON).
				setCitizenId( VALID_OBSERVATION_CITIZENID ).setEnterTime( entryTimestamp  ).setLeaveTime( leaveTimestamp ).
				build();
		
		assertThrows(StatusRuntimeException.class, () -> frontend.report(observation).getResult());
		
	}

	//Ping to server
	@Test
	public void testPing() {
		
		assertEquals(PING, frontend.ctrlPing(PingRequest.getDefaultInstance()).getResult());
	}

}
