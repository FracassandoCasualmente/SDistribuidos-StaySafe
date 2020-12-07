package pt.tecnico.staysafe.dgs.update;

import java.io.IOException;

public class Update {
	private TimestampVetorial _tv;
	private com.google.protobuf.GeneratedMessageV3 _request;

	public Boolean happensBefore(Update other) throws IOException {
		return _tv.happensBefore(other._tv);
	}
	
	public Update(TimestampVetorial tv, com.google.protobuf.GeneratedMessageV3 request)
	{
		_tv = tv;
		_request = request;
	}
	
	public TimestampVetorial getTS()
	{
		return _tv;
	}
	
	public com.google.protobuf.GeneratedMessageV3 getRequest()
	{
		return _request;
	}

	
}