package com.yana.PrivateNetWorking.Node.localServer.httpServer.requetHandle;

import java.io.IOException;
import java.net.Socket;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.IHttpServer;
import com.yana.PrivateNetWorking.Node.localServer.socket.WrapperSocket;

class RequestHandleWorker implements Runnable {
	private ThreadLocal<RequestHandle> handlerCollection;
	private IHttpServer httpServ;
	private RequestHandleWrokerLeaderQueue center;
	private boolean isRunnning;
	int no;
	RequestHandleWorker(IHttpServer httpServ, RequestHandleWrokerLeaderQueue center, ThreadLocal<RequestHandle> handlerCollection, int no) {
		isRunnning = true;
		this.httpServ = httpServ;
		this.center = center;
		this.handlerCollection = handlerCollection;
		this.no = no;
	}

	@Override
	public void run() {
		while(isRunnning) {
			RequestHandleWorker leader = center.getLeader();
			synchronized (center) {
				if(leader != null && !leader.equals(this)) {
					try {
						center.wait();
					} catch(InterruptedException e) {
						
					}
					continue;
				}
			}
			WrapperSocket sock;
			try {
				sock = this.httpServ.await();
			} catch(IOException e) {
				e.printStackTrace();
				break;
			} finally {
				center.remove(this);
				center.promteLeader();
			}
			RequestHandle requestHandle = handlerCollection.get();
			if(requestHandle == null) {
				requestHandle = new RequestHandle(Thread.currentThread().getName());
				handlerCollection.set(requestHandle);
			}
			requestHandle.response(sock);
			center.add(this);
		} 
	}
}
