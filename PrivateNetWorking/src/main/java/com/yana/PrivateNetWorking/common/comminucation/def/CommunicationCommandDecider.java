package com.yana.PrivateNetWorking.common.comminucation.def;

public class CommunicationCommandDecider {
	public static CommunicationCommand dicede(String request) {
		for(CommunicationCommand r : CommunicationCommand.values()) {
			if(r.name().equals(request)) {
				return r;
			}
		}
		return CommunicationCommand.NONE;
	}
}
