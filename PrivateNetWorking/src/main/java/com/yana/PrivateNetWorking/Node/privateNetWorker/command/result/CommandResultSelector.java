package com.yana.PrivateNetWorking.Node.privateNetWorker.command.result;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;

public class CommandResultSelector {
	public static ICommandResult selectCommandResult(CommunicationCommand communicationOperator) {
		switch(communicationOperator){
		case JOIN:
			return new JoinCommandResult();
		case REQUEST_COMMONKEY:
			return new RequestCommonKeyResult();
		case REQUSET_DIRLIST:
			return new RequestDirListResult();
		case REQUEST_CALL:
			return new RequestCallResult();
		default:
			return null;
		}
	}
}
