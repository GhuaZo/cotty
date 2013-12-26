package org.ghuazo.cotty.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CottySession {

	public static final String QQUID = "qquin";

	public static final String VFWQQ = "vfwqq";

	public static final String PSNID = "psnid";

	public static final String STATUS = "status";

	private Map<String, Object> attribute ;
	
	public CottySession(){
		this.attribute = new HashMap<String,Object>() ; 
	}
	
	public Map<String, Object> getAttribute() {
		return attribute;
	}

	
	public CottySession addAttribute(String key , Object value){
		this.attribute.put(key,value);
		return this ; 
	}
	
	public Boolean getBooleanAttribute(String key){
		Boolean value = (Boolean) this.attribute.get(key);
		return value;
		
	}
	
	public String getStringAttribute(String key){
		String value = (String) this.attribute.get(key);
		return value;
		
	}
	
	public byte[] getBytesAttribute(String key){
		byte[] value = (byte[]) this.attribute.get(key);
		return value;
		
	}
	public Object getObjectAttribute(String key){
		Object value = this.attribute.get(key);
		return value;
		
	}
	public Integer getIntegerAttribute(String key){
		Integer value = (Integer) this.attribute.get(key);
		return value;
		
	}
	public Date getDateAttribute(String key){
		Date value = (Date) this.attribute.get(key);
		return value;
		
	}
	public Long getLongAttribute(String key){
		Long value = (Long) this.attribute.get(key);
		return value;
		
	}
	
}
