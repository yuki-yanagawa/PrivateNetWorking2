package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

@FunctionalInterface
public interface ArgumentWrapper<T> {
	public T getArgs();
}
