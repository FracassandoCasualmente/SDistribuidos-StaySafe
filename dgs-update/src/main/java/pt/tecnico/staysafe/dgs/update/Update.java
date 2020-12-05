package pt.tecnico.staysafe.dgs.update;

public class Update {
	private TimestampVetorial _tv;
	private com.google.protobuf.GeneratedMessageV3 _request;

	public Boolean happensBefore(Update other) throws IOException {
		_tv.happensBefore(other._tv);
	}

	
}