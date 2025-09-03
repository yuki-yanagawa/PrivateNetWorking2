package com.yana.PrivateNetWorking.CentralRouter.analyzer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MemberCache {
	private static class Member {
		private String hostaddress;
		private int port;
		private String name;
		private Member(String hostaddress, int port, String name) {
			this.hostaddress = hostaddress;
			this.port = port;
			this.name = name;
		}

		String identityCode() {
			return this.hostaddress + ":" + String.valueOf(this.port);
		}
		String displayUserCode() {
			return "username:" + this.name + " " +this.hostaddress + ":" + String.valueOf(this.port);
		}
	}

	private static MemberCache memberCache = new MemberCache();

	private List<Member> memberList = new ArrayList<>();
	private MemberCache() {
	}

	public static MemberCache getInstance() {
		return memberCache;
	}

	public void registMember(InetSocketAddress socketAddress, String name) {
		String host = socketAddress.getAddress().getHostAddress();
		int port = socketAddress.getPort();
		Member member = new Member(host, port, name);
		synchronized (memberList) {
			for(Member m : memberList) {
				if(m.identityCode().equals(member.identityCode())) {
					memberList.notifyAll();
					return;
				}
			}
			memberList.add(member);
			memberList.notifyAll();
		}
	}

	public void removeMember(InetSocketAddress socketAddress) {
		String host = socketAddress.getAddress().getHostAddress();
		int port = socketAddress.getPort();
		// !!!!!!!!
	}

	public String getMemberList() {
		StringBuilder sb = new StringBuilder();
		synchronized (memberList) {
			for(Member m : memberList) {
				sb.append(m.identityCode()).append(",");
			}
		}
		return sb.toString();
	}

	public List<String> getDisplayStyleAndIdentityCode() {
		List<String> list = new ArrayList<>();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		synchronized (memberList) {
			for(Member m : memberList) {
				sb1.append(m.identityCode()).append(",");
				sb2.append(m.displayUserCode()).append(",");
			}
		}
		list.add(sb1.toString());
		list.add(sb2.toString());
		return list;
	}
}
