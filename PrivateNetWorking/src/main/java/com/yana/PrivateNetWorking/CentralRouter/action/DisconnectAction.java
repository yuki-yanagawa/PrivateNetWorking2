package com.yana.PrivateNetWorking.CentralRouter.action;

import java.net.InetSocketAddress;

import com.yana.PrivateNetWorking.CentralRouter.analyzer.MemberCache;

class DisconnectAction implements IAction{
	private InetSocketAddress socketAddr;
	private MemberCache memberCache;
	DisconnectAction(InetSocketAddress socketAddr) {
		this.socketAddr = socketAddr;
		memberCache = MemberCache.getInstance();
	}
	@Override
	public void execute(String[] requetLines) {
		memberCache.removeMember(socketAddr);
	}
}
