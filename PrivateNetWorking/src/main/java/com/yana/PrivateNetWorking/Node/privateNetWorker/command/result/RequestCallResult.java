package com.yana.PrivateNetWorking.Node.privateNetWorker.command.result;

import com.yana.PrivateNetWorking.Node.privateNetWorker.CommunicationStore;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.FutureCommandResult;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;

class RequestCallResult implements ICommandResult {

	@SuppressWarnings("unchecked")
	@Override
	public boolean settigResopnseData(byte[] reseponsePacketData) {
		FutureCommandResult<?> tmp = CommunicationStore.getFutureCommandResult(CommunicationCommand.REQUEST_CALL_ACK);
		if(tmp == null) {
			return false;
		}
		FutureCommandResult<byte[]> commandResult = (FutureCommandResult<byte[]>)tmp;
		commandResult.setResult(reseponsePacketData);
		return true;
	}
	
}
