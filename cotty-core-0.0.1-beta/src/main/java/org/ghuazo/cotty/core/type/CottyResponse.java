package org.ghuazo.cotty.core.type;

import java.net.CookieStore;

public class CottyResponse {
	
	public enum Status{SUCCESS,FAILURE};
	
	private Status status ; 
	
	private String content ;
	
	private CookieStore cookieStore ;
	
	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	
}
