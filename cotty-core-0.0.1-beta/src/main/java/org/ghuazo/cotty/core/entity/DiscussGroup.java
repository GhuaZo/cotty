package org.ghuazo.cotty.core.entity;

public class DiscussGroup {

	private String name ;
	
	private Long did ;

	public String getName() {
		return name;
	}

	public DiscussGroup setName(String name) {
		this.name = name;
		return this ; 
	}

	public Long getDID() {
		return did;
	}

	public DiscussGroup setDID(Long did) {
		this.did = did;
		return this ;
	}

	
}
