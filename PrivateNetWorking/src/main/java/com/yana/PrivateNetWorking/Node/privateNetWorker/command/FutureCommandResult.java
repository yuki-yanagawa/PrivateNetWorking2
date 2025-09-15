package com.yana.PrivateNetWorking.Node.privateNetWorker.command;

public class FutureCommandResult<T> {
	private static final long WAIT_LIMIT_TIME_MSEC = 3000;
	private T result;
	private long startTime;
	private long timeWaitMsec;
	public FutureCommandResult() {
		this.startTime = System.currentTimeMillis();
		this.timeWaitMsec = -1;
	}

	public FutureCommandResult(long timeWaitMsec) {
		this.startTime = System.currentTimeMillis();
		this.timeWaitMsec = timeWaitMsec;
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

	private long getLimitTime() {
		return this.timeWaitMsec == -1 ? WAIT_LIMIT_TIME_MSEC : this.timeWaitMsec; 
	}

	private boolean limitTimeOver() {
		if(System.currentTimeMillis() - startTime > getLimitTime()) {
			return true;
		}
		return false;
	}
}
