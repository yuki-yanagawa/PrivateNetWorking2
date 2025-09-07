package com.yana.PrivateNetWorking.common.comminucation.def;

public class CommunicationDefnition {
	public static final String LINE_SPARATOR = "\r\n";

	//SubJect Communicaiton
	public static final String SUBJ_JOIN = "JOIN";
	public static final String SUBJ_JOIN_ACK = "JOIN_ACK";
	public static final String SUBJ_UPDATE_MEMBER = "UPDATE_MEMBER";
	public static final String SUBJ_REQUEST_COMMONKEY = "REQUEST_COMMONKEY";
	public static final String SUBJ_REQUEST_COMMONKEY_ACK = "REQUEST_COMMONKEY_ACK";
	public static final String SUBJ_NO_RESPONSE_USER = "NO_RESPONSE_USER";
	public static final String SUBJ_REQUSER_COMMON_COM = "REQUSER_COMMON_COM";
	public static final String SUBJ_REQUSET_DIRLIST = "REQUSET_DIRLIST";
	public static final String SUBJ_REQUSET_DIRLIST_ACK = "REQUSET_DIRLIST_ACK";
	public static final String SUBJ_REQUSET_FILE = "REQUEST_FILE";
	public static final String SUBJ_REQUSET_FILE_ACK = "REQUEST_FILE_ACK";
	public static final String SUBJ_NONE = "NONE";
	public static final String SUBJ_DISCONNECT = "DISCONNECT";
	public static final String SUBJ_ACTIVATE = "ACTIVATE";
	public static final String SUBJ_ACTIVATE_ACK = "ACTIVATE_ACK";

	//KEY_VAL
	public static final String KEY_USER_NAME = "USER_NAME";
	public static final String KEY_SERVER_CERT = "SERVER_CERT";
	public static final String KEY_MEMBERLIST = "MEMBERLIST";
	public static final String KEY_MYPUB = "MYPUB";
	public static final String KEY_COMK = "COMK";
	public static final String KEY_FILENAME = "FILENAME";
	public static final String KEY_FILEDATA = "FILEDATA";
	
	public static final String KEY_VAL_SEPALATOR = "=";

	public static final String MEMBER_SEPALATOR = ",";
	public static final String IP_PORT_SEPALATOR = ":";
}
