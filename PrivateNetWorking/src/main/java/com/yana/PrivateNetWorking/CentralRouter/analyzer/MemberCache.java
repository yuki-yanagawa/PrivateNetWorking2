package com.yana.PrivateNetWorking.CentralRouter.analyzer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemberCache {
	private static class Member {
		private String hostaddress;
		private int port;
		private String name;
		private int deactivateCount;
		private Member(String hostaddress, int port, String name) {
			this.hostaddress = hostaddress;
			this.port = port;
			this.name = name;
			this.deactivateCount = 0;
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
					// User name update
					m.name = name;
					memberList.notifyAll();
					return;
				}
			}
			memberList.add(member);
			memberList.notifyAll();
		}
	}

	public void checkedActivateUser(InetSocketAddress socketAddress, String name) {
		String host = socketAddress.getAddress().getHostAddress();
		int port = socketAddress.getPort();
		Member member = new Member(host, port, name);
		synchronized (memberList) {
			for(Member m : memberList) {
				if(m.identityCode().equals(member.identityCode())) {
					m.deactivateCount = 0;
					memberList.notifyAll();
					return;
				}
			}
		}
	}

	public boolean deActivateUser(InetSocketAddress socketAddress, String name) {
		String host = socketAddress.getAddress().getHostAddress();
		int port = socketAddress.getPort();
		synchronized (memberList) {
			Iterator<Member> memberItr = memberCache.memberList.iterator();
			while(memberItr.hasNext()) {
				Member tmpMember = memberItr.next();
				if(tmpMember.hostaddress.equals(host) && tmpMember.port == port) {
					tmpMember.deactivateCount++;
					if(tmpMember.deactivateCount > 5) {
						synchronized (memberCache) {
							memberItr.remove();
						}
						return true;
					}
					break;
				}
			}
			return false;
		}
	}

	public void removeMember(InetSocketAddress socketAddress) {
		String host = socketAddress.getAddress().getHostAddress();
		int port = socketAddress.getPort();
		Iterator<Member> memberItr = memberCache.memberList.iterator();
		while(memberItr.hasNext()) {
			Member tmpMember = memberItr.next();
			if(tmpMember.hostaddress.equals(host) && tmpMember.port == port) {
				synchronized (memberCache) {
					memberItr.remove();
				}
			}
		}
	}

	public synchronized String getMemberList() {
		StringBuilder sb = new StringBuilder();
		synchronized (memberList) {
			for(Member m : memberList) {
				sb.append(m.identityCode()).append(",");
			}
		}
		return sb.toString();
	}

	public synchronized List<String> getDisplayStyleAndIdentityCode() {
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
