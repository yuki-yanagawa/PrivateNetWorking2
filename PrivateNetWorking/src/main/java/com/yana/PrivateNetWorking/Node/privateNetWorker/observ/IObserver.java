package com.yana.PrivateNetWorking.Node.privateNetWorker.observ;

import com.yana.PrivateNetWorking.Node.privateNetWorker.WrapAcceptPrivateNetPacketData;

public interface IObserver {
	public void update(WrapAcceptPrivateNetPacketData data);
}
