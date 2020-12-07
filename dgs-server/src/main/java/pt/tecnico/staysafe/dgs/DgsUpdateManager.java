package pt.tecnico.staysafe.dgs;

import java.util.LinkedList;

import pt.tecnico.staysafe.dgs.update.DgsDebugger;
import pt.tecnico.staysafe.dgs.update.TimestampVetorial;
import pt.tecnico.staysafe.dgs.update.Update;

// class that is initialized in serverImpl to manage updates
public class DgsUpdateManager {
	private TimestampVetorial _currentTV;
	private Integer _replicaId;
	private LinkedList<Update> _executedUpdates;
	
	public DgsUpdateManager(Integer replicaId)
	{
		_currentTV = new TimestampVetorial();
		_replicaId = replicaId;
	}
	
	// updates the current TV, adds the update to the list and returns the new TV to client
	public TimestampVetorial update(com.google.protobuf.GeneratedMessageV3 request) {
		_currentTV.setPos(_replicaId, _currentTV.getPos(_replicaId) + 1);
		_executedUpdates.add(new Update(_currentTV,request));
		DgsDebugger.debug("Update added: " + _currentTV.toString());
		return _currentTV;
	}
	
	// returns the current replica TV
	public TimestampVetorial getCurrentTV()
	{
		return _currentTV;
	}
}