package org.ghuazo.cotty.core.type;

import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;

public class CottyRequest{
	private String url;

	private String method;

	private Map<String, String> content;

	private Map<String, String> params;

	private CookieStore cookieStore;

	public CottyRequest(String url) {
		this.url = url;
		this.content = new HashMap<String,String>() ; 
		this.params = new HashMap<String,String>() ; 
	}

	public String getUrl() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	public CottyRequest setMethod(String method) {
		this.method = method;
		return this;
	}

	public Map<String, String> getContent() {
		return content;
	}

	public CottyRequest addContent(String key,String value) {
		this.content.put(key, value);
		return this;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public CottyRequest addParams(String key , String value) {
		this.params.put(key, value);
		return this;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public CottyRequest setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
		return this;
	}

}
