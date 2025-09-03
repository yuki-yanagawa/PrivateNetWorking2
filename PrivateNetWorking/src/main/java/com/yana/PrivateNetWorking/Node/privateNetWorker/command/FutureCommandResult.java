package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

public class FutureCommandResult<T> {
	private static final long WAIT_LIMIT_TIME_MSEC = 3000;
	private T result;
	private long startTime;
	public FutureCommandResult() {
		startTime = System.currentTimeMillis();
	}
	public void setResult(T result) {
		synchronized(this) {
			this.result = result;
			this.notifyAll();
		}
	}

	public T getResult() {
		boolean isContinueWait = true;
		long waitTime = 1000L;
		synchronized (this) {
			while(result == null && isContinueWait) {
				try  {
					this.wait(waitTime);
				} catch(InterruptedException e) {
					if(limitTimeOver()) {
						isContinueWait = false;
					}
				}
			}
			return result;
		}
	}

	private boolean limitTimeOver() {
		if(System.currentTimeMillis() - startTime > WAIT_LIMIT_TIME_MSEC) {
			return true;
		}
		return false;
	}
}
