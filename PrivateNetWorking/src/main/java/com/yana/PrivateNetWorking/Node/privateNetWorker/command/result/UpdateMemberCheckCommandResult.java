package com.yana.PrivateNetWorking.Node.privateNetWorker.command.result;

import java.util.ArrayList;
import java.util.List;

import com.yana.PrivateNetWorking.Node.privateNetWorker.command.FutureCommandResult;
import com.yana.PrivateNetWorking.common.comminucation.def.CommunicationDefnition;
import com.yana.PrivateNetWorking.common.util.CharsetUtil;

public class UpdateMemberCheckCommandResult {
	private static UpdateMemberCheckCommandResult updateCommandResult = new UpdateMemberCheckCommandResult();
	private List<String> latestUpdateUsers = new ArrayList<>();
	private UpdateMemberCheckCommandResult() {
	}

	public static void setLatestUpdateUsers(byte[] responseData) {
		String tmp = new String(responseData, CharsetUtil.charSet());
		String[] tmps = tmp.split(CommunicationDefnition.LINE_SPARATOR);
		if(!tmps[0].trim().equals(CommunicationDefnition.SUBJ_UPDATE_MEMBER)) {
			return;
		}
		if(!tmps[1].startsWith(CommunicationDefnition.KEY_MEMBERLIST)){
			return;
		}
		String value = tmps[1].split(CommunicationDefnition.KEY_VAL_SEPALATOR)[1].trim();
		synchronized (updateCommandResult) {
			updateCommandResult.latestUpdateUsers.clear();
			for(String s : value.split(",")) {
				updateCommandResult.latestUpdateUsers.add(s.trim());
			}
		}
	}

	public static List<String> getUpdateUserList() {
		synchronized (updateCommandResult) {
			return updateCommandResult.latestUpdateUsers;
		}
	}

	public static void setLatestUpdateUsersToFuterCommandResult(FutureCommandResult<List<String>> futureCommandResult) {
		synchronized (updateCommandResult) {
			futureCommandResult.setResult(updateCommandResult.latestUpdateUsers);
		}
	}
}
