package com.yana.PrivateNetWorking.Node.privateNetWorker.accept;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.yana.PrivateNetWorking.Node.privateNetWorker.observ.IObserver;
import com.yana.PrivateNetWorking.Node.privateNetWorker.observ.ObserverSelector;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;

public class Acceptor {
	private static Acceptor acceptor = new Acceptor();
	private Map<CommunicationCommand, IObserver> obServerMap = new HashMap<>();
	private Map<CommunicationCommand, byte[]>  tmpStore = new HashMap<>();
	private Acceptor() {
	}

	public static Acceptor getInstance() {
		return acceptor;
	}

	public void addResponseData(CommunicationCommand command, byte[] responseData) {
		synchronized (obServerMap) {
			if(!obServerMap.containsKey(command)) {
				tmpStore.put(command, responseData);
				return;
			}
			IObserver observer = obServerMap.get(command);
			observer.update(responseData);
		}
	}

	public void addObserver(CommunicationCommand command) {
		Optional<IObserver> observerOpt = ObserverSelector.select(command);
		if(!observerOpt.isPresent()) {
			return;
		}
		IObserver observer = observerOpt.get();
		synchronized (obServerMap) {
			obServerMap.put(command, observer);
		}
		if(tmpStore.containsKey(command)) {
			observer.update(tmpStore.remove(command));
		}
	}
}
