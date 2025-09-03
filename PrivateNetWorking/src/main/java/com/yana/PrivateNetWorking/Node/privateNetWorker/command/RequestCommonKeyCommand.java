package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import java.security.PublicKey;
import java.util.Base64;

import com.yana.PrivateNetWorking.Node.keyStore.NodeKeyStore;
import com.yana.PrivateNetWorking.Node.privateNetWorker.CommunicationStore;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationCommand;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;

class RequestCommonKeyCommand implements ICommand {
	private CommandPrivateNetHelper commandPrivateNetHelper;
	RequestCommonKeyCommand(CommandPrivateNetHelper commandPrivateNetHelper) {
		this.commandPrivateNetHelper = commandPrivateNetHelper;
	}
	@Override
	public FutureCommandResult<?> execute(ArgumentWrapper<?> argumentsWrapper) {
		FutureCommandResult<byte[]> fture = new FutureCommandResult<>();
		CommunicationStore.setFutureCommandResult(CommunicationCommand.REQUEST_COMMONKEY, fture);
		PublicKey pubk = NodeKeyStore.getNodePublicKey();
		String pubKString = Base64.getEncoder().encodeToString(pubk.getEncoded());
		StringBuilder sb = new StringBuilder();
		sb.append(CommunicationDefnition.SUBJ_REQUEST_COMMONKEY).append(CommunicationDefnition.LINE_SPARATOR)
			.append(CommunicationDefnition.KEY_MYPUB).append(CommunicationDefnition.KEY_VAL_SEPALATOR)
			.append(pubKString).append(CommunicationDefnition.LINE_SPARATOR);
		commandPrivateNetHelper.sendMessageToRouter(sb.toString());
		return fture;
	}

}
