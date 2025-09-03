package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

public interface ICommand {
	public FutureCommandResult<?> execute(ArgumentWrapper<?> argumentsWrapper);
}
