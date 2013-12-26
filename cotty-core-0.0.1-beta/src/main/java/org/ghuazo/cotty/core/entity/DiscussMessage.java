package org.ghuazo.cotty.core.entity;

public class DiscussMessage extends CottyMessage {

	private Long DID ; 
	
	private Long authorUIN ; 
	
	public DiscussMessage(){
		this.setType(CottyMessage.Type.discussMessage);
	}

	public Long getDID() {
		return DID;
	}

	public DiscussMessage setDID(Long dID) {
		DID = dID;
		return this ;
	}

	public Long getAuthorUIN() {
		return authorUIN;
	}

	public DiscussMessage setAuthorUIN(Long authorUIN) {
		this.authorUIN = authorUIN;
		return this ;
	}
	
	
}
