package com.yana.PrivateNetWorking.common.comminucation.def;

public enum CommunicationCommand {
	JOIN, JOIN_ACK, 
	UPDATE_MEMBER, 
	REQUEST_COMMONKEY, REQUEST_COMMONKEY_ACK, 
	NO_RESPONSE_USER, 
	REQUSER_COMMON_COM, 
	REQUSET_DIRLIST, REQUSET_DIRLIST_ACK,
	DISCONNECT,
	NONE,
}
