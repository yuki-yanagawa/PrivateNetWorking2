package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

import java.util.List;

import com.yana.PrivateNetWorking.Node.privateNetWorker.command.result.UpdateMemberCheckCommandResult;

class UpdateMemberCheckCommand implements ICommand{
	@Override
	public FutureCommandResult<List<String>> execute(ArgumentWrapper<?> argumentsWrapper) {
		FutureCommandResult<List<String>> futureCommandResult = new FutureCommandResult<>();
		UpdateMemberCheckCommandResult.setLatestUpdateUsersToFuterCommandResult(futureCommandResult);
		return futureCommandResult;
	}
}
