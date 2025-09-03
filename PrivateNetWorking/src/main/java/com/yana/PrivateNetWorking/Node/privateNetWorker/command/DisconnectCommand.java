package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;

class DisconnectCommand implements ICommand {
	private CommandPrivateNetHelper commandPrivateNetHelper;
	DisconnectCommand(CommandPrivateNetHelper commandPrivateNetHelper) {
		this.commandPrivateNetHelper = commandPrivateNetHelper;
	}

	@Override
	public FutureCommandResult<?> execute(ArgumentWrapper<?> argumentsWrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_DISCONNECT).append(CommunicationDefnition.LINE_SPARATOR);
		commandPrivateNetHelper.sendMessageToRouter(sb.toString());
		return null;
	}

}
