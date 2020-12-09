package pt.tecnico.staysafe.dgs;

import java.io.IOException;

import pt.tecnico.staysafe.dgs.grpc.*;

import pt.tecnico.staysafe.dgs.update.TimestampVetorial;

public class Update {
	private final TimestampVetorial _tv;
	private com.google.protobuf.GeneratedMessageV3 _request;

	public Boolean happensBefore(Update other) throws IOException {
		return _tv.happensBefore(other._tv);
	}
	
	public Update(TimestampVetorial tv, com.google.protobuf.GeneratedMessageV3 request)
	{
		_tv = tv;

		_request = request;
		adaptTimestamp();
		
	}
	
	public TimestampVetorial getTS()
	{
		return _tv;
	}
	
	public com.google.protobuf.GeneratedMessageV3 getRequest()
	{
		return _request;
	}

	// adapt _request.TV() to our current timestamp object
	// called each time we change the timestamp
	private void adaptTimestamp() {
		//debug("adapt: Type of request= "+_request.getClass().getSimpleName());
		
		// select to which class we are going to cast this and add TV
		
		switch(_request.getClass().getSimpleName().split("Request")[0]) {
			case "SnifferJoin":

				//debug("Going to send a snifferJoin");
				SnifferJoinRequest sjr = (SnifferJoinRequest) _request;
				
				if (!sjr.getTVList().isEmpty()) {
					return;
				}
				_request = sjr.toBuilder().addAllTV(_tv.getTvAsList()).build();
				//debug("adapt: all things in request: "+_request);
				//debug("adapt: ts in request = "+((SnifferJoinRequest)_request).getTVList());
				//debug("adapth: ts in _tv = "+_tv);
				break;
			case "Report":
				ReportRequest rr = (ReportRequest) _request;
				if (!rr.getTVList().isEmpty()) {
					return;
				}
				_request = rr.toBuilder().addAllTV(_tv.getTvAsList()).build();
				break;
			case "Clear":
				ClearRequest cr = (ClearRequest) _request;
				if (!cr.getTVList().isEmpty()) {
					return;
				}
				_request = cr.toBuilder().addAllTV(_tv.getTvAsList()).build();
				
				break;
			case "Init":
				// see if it's a replica message or 
				// add timestamp to Request
				InitRequest ir = (InitRequest) _request;
				if (!ir.getTVList().isEmpty()) {
					return;
				}
				_request = ir.toBuilder().addAllTV(_tv.getTvAsList()).build();

				break;
			default:
				throw new
				Error("Unexpected type of update: "+
				_request.getClass().getSimpleName());
		}
	}

	private void debug(String s) {
		DgsServerApp.debug("(Update): "+s);
	}

	
}