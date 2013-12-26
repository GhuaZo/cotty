package org.ghuazo.cotty.core.entity;

public class GroupMessage extends CottyMessage {

	
	
	private Long groupUIN ; 
	
	private Long authorUIN ; 
	
	public GroupMessage(){
		this.setType(CottyMessage.Type.groupMessage);
	}

	public Long getGroupUIN() {
		return groupUIN;
	}

	public GroupMessage setGroupUIN(Long groupUIN) {
		this.groupUIN = groupUIN;
		return this ;
	}

	public Long getAuthorUIN() {
		return authorUIN;
	}

	public GroupMessage setAuthorUIN(Long authorUIN) {
		this.authorUIN = authorUIN;
		return this ; 
	}
	
	

}
