package com.yana.PrivateNetWorking.Node.privateNetWorker.observ;

import java.util.Optional;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;

public class ObserverSelector {
	public static Optional<IObserver> select(CommunicationCommand command) {
		switch(command){
		case UPDATE_MEMBER:
			return Optional.of(new UpdateMemberObserver());
		case REQUEST_FILE_ACK:
			return Optional.of(new AckFileObserver());
		case REQUEST_CALL:
			return Optional.of(new RequestCallObserv());
		default:
			return Optional.empty();
		}
	}
}
