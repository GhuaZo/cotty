package org.ghuazo.cotty.core.entity;

public class CottyMessage {

	public enum Type {
		discussMessage,
		friendMessage,
		groupMessage,
	}
	private String content ;
	
	private Type type;

	public String getContent() {
		return content;
	}

	public CottyMessage setContent(String Content) {
		this.content = Content;
		return this ; 
	}

	public Type getType() {
		return type;
	}

	public CottyMessage setType(Type type) {
		this.type = type;
		return this ;
	} 
	
}
