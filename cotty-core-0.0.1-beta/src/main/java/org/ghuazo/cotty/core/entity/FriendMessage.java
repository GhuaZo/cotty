package org.ghuazo.cotty.core.entity;

public class FriendMessage extends CottyMessage {

	private Long UIN ; 
	
	public FriendMessage(){
		this.setType(CottyMessage.Type.friendMessage);
	}

	public Long getUIN() {
		return UIN;
	}

	public FriendMessage setUIN(Long UIN) {
		this.UIN = UIN;
		return this ; 
	}
}
