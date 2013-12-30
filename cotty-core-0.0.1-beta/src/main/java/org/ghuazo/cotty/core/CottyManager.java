package org.ghuazo.cotty.core;

import java.util.HashMap;
import java.util.Map;

import org.ghuazo.cotty.core.entity.CottyFriend;
import org.ghuazo.cotty.core.entity.CottyGroup;
import org.ghuazo.cotty.core.entity.CottyMessage;
import org.ghuazo.cotty.core.entity.DiscussGroup;
import org.ghuazo.cotty.core.entity.DiscussMessage;
import org.ghuazo.cotty.core.entity.FriendMessage;
import org.ghuazo.cotty.core.entity.GroupMessage;

public class CottyManager {

	
	public interface ManagerCallback {

		public void onNeedSend(CottyManager cottyManager,CottyMessage cottyMessage);
	}

	private String account ; 
	
	private String country ;
	
	private String city ;
	
	private String signature ;
	
	private String nick ;
	
	private CottySession cottySession ; 
	
	private Map<Long,CottyFriend> cottyFriend ;
	
	private Map<Long,CottyGroup> cottyGroup ;
	
	private Map<Long,DiscussGroup> discussGroup ; 
	
	private ManagerCallback managerCallback ; 
	
	
	public CottyManager(CottySession cottySession,ManagerCallback managerCallback) {
		this.cottySession = cottySession;
		this.cottyFriend = new HashMap<Long,CottyFriend>() ; 
		this.cottyGroup = new HashMap<Long,CottyGroup>() ; 
		this.discussGroup = new HashMap<Long,DiscussGroup>() ; 
		this.managerCallback = managerCallback ;
	}

	public String getAccount() {
		return account;
	}

	public CottyManager setAccount(String account) {
		this.account = account;
		return this ; 
	}

	public String getCountry() {
		return country;
	}

	public CottyManager setCountry(String country) {
		this.country = country;
		return this ; 
	}

	public String getCity() {
		return city;
	}

	public CottyManager setCity(String city) {
		this.city = city;
		return this ; 
	}

	public String getSignature() {
		return signature;
	}

	public CottyManager setSignature(String signature) {
		this.signature = signature;
		return this ; 
	}

	public String getNick() {
		return nick;
	}

	public CottyManager setNick(String nick) {
		this.nick = nick;
		return this ; 
	}

	public CottySession getCottySession() {
		return cottySession;
	}

	public Map<Long, CottyGroup> getCottyGroup() {
		return cottyGroup;
	}

	public CottyManager setCottyGroup(Map<Long, CottyGroup> cottyGroup) {
		this.cottyGroup = cottyGroup;
		return this ; 
	}

	public Map<Long, DiscussGroup> getDiscussGroup() {
		return discussGroup;
	}

	public CottyManager setDiscussGroup(Map<Long, DiscussGroup> discussGroup) {
		this.discussGroup = discussGroup;
		return this ; 
	}

	public void setCottySession(CottySession cottySession) {
		this.cottySession = cottySession;
	}

	 
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer() ; 
		stringBuffer.append("QQ:").append(this.account).append("\n")
		.append("昵称:").append(this.nick).append("\n")
		.append("签名:").append(this.signature).append("\n")
		.append("国家:").append(this.country).append("\n")
		.append("城市:").append(this.city).append("\n")
		;
		return stringBuffer.toString();
	}

	public Map<Long,CottyFriend> getCottyFriend() {
		return cottyFriend;
	}

	public CottyManager setCottyFriend(Map<Long,CottyFriend> cottyFriend) {
		this.cottyFriend = cottyFriend;
		return this ; 
	}

	public void sendFriendMessage(FriendMessage friendMessage){
		this.managerCallback.onNeedSend(this,friendMessage);
	}
	public void sendFriendMessage(Long UIN,String message){
		FriendMessage friendMessage = new FriendMessage();
		friendMessage.setUIN(UIN).setContent(message);
		this.managerCallback.onNeedSend(this,friendMessage);
	}
	public void sendGroupMessage(GroupMessage groupMessage){
		this.managerCallback.onNeedSend(this,groupMessage);
	}
	public void sendGroupMessage(Long UIN,String message){
		GroupMessage groupMessage = new GroupMessage();
		groupMessage.setGroupUIN(UIN);
		groupMessage.setContent(message);
	}
	public void sendDiscussMessage(DiscussMessage discussMessage){
		this.managerCallback.onNeedSend(this,discussMessage);
	}
	public void sendDiscussMessage(Long DID , String message){
		DiscussMessage discussMessage = new DiscussMessage() ; 
		discussMessage.setDID(DID);
		discussMessage.setContent(message);
		this.managerCallback.onNeedSend(this,discussMessage);
	}
}
