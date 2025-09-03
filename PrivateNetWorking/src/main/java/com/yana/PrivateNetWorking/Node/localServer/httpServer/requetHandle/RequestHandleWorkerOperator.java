package com.yana.PrivateNetWorking.Node.localServer.httpServer.requetHandle;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.IHttpServer;

public class RequestHandleWorkerOperator {
	private static final ThreadLocal<RequestHandle> handlerCollection = new ThreadLocal<>();
	private ExecutorService executorService;
	private RequestHandleWrokerLeaderQueue leaderQueue;
	public RequestHandleWorkerOperator(IHttpServer httpServ, int workerCount) {
		this.executorService = Executors.newFixedThreadPool(10);
		List<RequestHandleWorker> routingWorkerList = new CopyOnWriteArrayList<>();
		leaderQueue = new RequestHandleWrokerLeaderQueue(routingWorkerList);
		for(int i = 0; i < workerCount; i++) {
			routingWorkerList.add(new RequestHandleWorker(httpServ, leaderQueue, handlerCollection, i));
		}
		leaderQueue.promteLeader();
		routingWorkerList.stream().forEach(this.executorService::submit);
	}

	public void workerStart() {
		//leaderQueue.promteLeader();
	}
}
