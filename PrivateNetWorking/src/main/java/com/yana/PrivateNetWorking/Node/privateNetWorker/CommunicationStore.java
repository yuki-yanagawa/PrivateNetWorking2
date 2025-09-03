package com.yana.PrivateNetWorking.Node.privateNetWorker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yana.PrivateNetWorking.Node.privateNetWorker.command.FutureCommandResult;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;

public class CommunicationStore {
	private static CommunicationStore communicationStore = new CommunicationStore();
	private Map<CommunicationCommand, FutureCommandResult<?>> commandResultMap = new ConcurrentHashMap<>();
	
	public static FutureCommandResult<?> getFutureCommandResult(CommunicationCommand communicationOperator) {
		synchronized (communicationStore) {
			return communicationStore.commandResultMap.remove(communicationOperator);
		}
	}

	public static void setFutureCommandResult(CommunicationCommand commnuicationCommand, FutureCommandResult<?> futureCommandResult) {
//		if(resulStore.resultMap.containsKey(communicationOperator)) {
//			return false;
//		}
		communicationStore.commandResultMap.put(commnuicationCommand, futureCommandResult);
	} 
}
