package com.yana.PrivateNetWorking.Node.localServer.model.responseDataAnalyze;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.DefaultJsonObject;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.response.json.JsonObject;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class DirListResponseDataAnalyzer {
	private byte[] responseData;
	private JsonObject responseJsonData;
	public DirListResponseDataAnalyzer(byte[] responseData) {
		this.responseData = responseData;
	}

	//filename=XXXX:filesize=YYY, filename=XXXX:filesize=YYY
	public boolean analyze() {
		String responseDataStr = new String(responseData, CharsetUtil.charSet());
		String[] responseDataLines = responseDataStr.split(CommunicationDefnition.LINE_SPARATOR);
		if(!responseDataLines[0].trim().equals(CommunicationDefnition.SUBJ_REQUSET_DIRLIST_ACK)) {
			return false;
		}
		if(responseDataLines.length != 2) {
			return false;
		}
//		String[] fileLists = responseDataLines[1].split(",");
//		Map<String, Object> fileMap = new HashMap<>();
//		fileMap.put("fileList", fileLists);
		responseJsonData = new DefaultJsonObject(responseDataLines[1]);
		return true;
	}

	public JsonObject getResponseJsonData() {
		return responseJsonData;
	}

	
}
