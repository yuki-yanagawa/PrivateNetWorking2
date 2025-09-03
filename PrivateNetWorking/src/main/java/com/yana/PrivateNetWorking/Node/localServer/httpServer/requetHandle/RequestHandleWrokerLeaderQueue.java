package com.yana.PrivateNetWorking.Node.localServer.httpServer.requetHandle;

import java.util.List;

class RequestHandleWrokerLeaderQueue {
	private List<RequestHandleWorker> routingWorkerList;
	private RequestHandleWorker leader;
	RequestHandleWrokerLeaderQueue(List<RequestHandleWorker> routingWorkerList) {
		this.routingWorkerList = routingWorkerList;
	}

	synchronized void promteLeader() {
		try {
			RequestHandleWorker leader = null;
			if(!routingWorkerList.isEmpty()) {
				leader = routingWorkerList.get(0);
			}
			this.leader = leader;
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.notifyAll();
	}

	RequestHandleWorker getLeader() {
		return this.leader;
	}

	synchronized void remove(RequestHandleWorker routingWorker) {
		routingWorkerList.remove(routingWorker);
	}

	synchronized void add(RequestHandleWorker routingWorker) {
		routingWorkerList.add(routingWorker);
	}
}
