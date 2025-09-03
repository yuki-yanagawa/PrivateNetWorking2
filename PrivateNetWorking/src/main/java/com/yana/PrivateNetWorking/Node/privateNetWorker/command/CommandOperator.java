package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class CommandOperator {
	private static CommandOperator instance;
	private CommandPrivateNetHelper commandPrivateNetHelper;

	private CommandOperator(CommandPrivateNetHelper commandPrivateNetHelper) {
		this.commandPrivateNetHelper = commandPrivateNetHelper;
	}

	private CommandPrivateNetHelper getCommandPrivateNetHelper() {
		return this.commandPrivateNetHelper;
	}

	public static synchronized boolean readyCommandOperator(PrivateNetSocket privateNetSocket) {
		if(instance != null) {
			return true;
		}
		CommandPrivateNetHelper commandPrivateNetHelper = CommandPrivateNetHelper.createNetCommandHelper(privateNetSocket);
		if(commandPrivateNetHelper == null) {
			return false;
		}
		instance = new CommandOperator(commandPrivateNetHelper);
		return true;
	}

	public static ICommand command(CommunicationCommand communication) {
		switch(communication) {
		case JOIN:
			return new JoinCommand(instance.getCommandPrivateNetHelper());
		case UPDATE_MEMBER:
			return new UpdateMemberCheckCommand();
		case REQUEST_COMMONKEY:
			return new RequestCommonKeyCommand(instance.getCommandPrivateNetHelper());
		case REQUSET_DIRLIST:
			return new RequestFileListCommand(instance.getCommandPrivateNetHelper());
		case DISCONNECT:
			return new DisconnectCommand(instance.getCommandPrivateNetHelper());
		default:
			return new NoneCommand();
		}
	}
}
