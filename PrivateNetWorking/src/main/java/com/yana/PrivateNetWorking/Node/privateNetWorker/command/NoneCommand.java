package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

class NoneCommand implements ICommand {

	@Override
	public FutureCommandResult<Object> execute(ArgumentWrapper<?> argumentWrapper) {
		//None
		return null;
	}
}
