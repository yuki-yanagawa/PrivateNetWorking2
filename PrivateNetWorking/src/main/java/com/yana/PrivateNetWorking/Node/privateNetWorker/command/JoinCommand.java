package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import com.yana.PrivateNetWorking.Node.privateNetWorker.CommunicationStore;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;

class JoinCommand implements ICommand {
	private CommandPrivateNetHelper commandPrivateNetHelper;
	JoinCommand(CommandPrivateNetHelper commandPrivateNetHelper) {
		this.commandPrivateNetHelper = commandPrivateNetHelper;
	}
	@Override
	public FutureCommandResult<byte[]> execute(ArgumentWrapper<?> argumentWrapper) {
		if(argumentWrapper == null) {
			FutureCommandResult<byte[]> ret = new FutureCommandResult<>();
			ret.setResult(new byte[0]);
			return ret;
		}
		String userName = (String)argumentWrapper.getArgs();

		FutureCommandResult<byte[]> fture = new FutureCommandResult<>();
		CommunicationStore.setFutureCommandResult(CommunicationCommand.JOIN, fture);
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_JOIN).append(CommunicationDefnition.LINE_SPARATOR)
			.append(CommunicationDefnition.KEY_USER_NAME).append(CommunicationDefnition.KEY_VAL_SEPALATOR)
			.append(userName).append(CommunicationDefnition.LINE_SPARATOR);
		commandPrivateNetHelper.sendMessageToRouter(sb.toString());
		return fture;
	}
}
