package org.ghuazo.cotty.core.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SystemMessage extends CottyMessage {

	public static enum EventType {
		chenge, input, request, kickout, approve,revoke
	}

	private EventType eventType;

	private Map<String, Object> attribute;

	public SystemMessage() {
		this.attribute = new HashMap<String, Object>();
	}

	public SystemMessage(EventType eventType) {
		this.eventType = eventType;
		this.attribute = new HashMap<String, Object>();

	}

	public SystemMessage addAttribute(String key, Object value) {
		this.attribute.put(key, value);
		return this;
	}

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public SystemMessage setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
		return this;
	}

	
	public Boolean getBooleanAttribute(String key) {
		Boolean value = (Boolean) this.attribute.get(key);
		return value;

	}

	public String getStringAttribute(String key) {
		String value = (String) this.attribute.get(key);
		return value;

	}

	public byte[] getBytesAttribute(String key) {
		byte[] value = (byte[]) this.attribute.get(key);
		return value;

	}

	public Object getObjectAttribute(String key) {
		Object value = this.attribute.get(key);
		return value;

	}

	public Integer getIntegerAttribute(String key) {
		Integer value = (Integer) this.attribute.get(key);
		return value;

	}

	public Date getDateAttribute(String key) {
		Date value = (Date) this.attribute.get(key);
		return value;

	}

	public Long getLongAttribute(String key) {
		Long value = (Long) this.attribute.get(key);
		return value;

	}

	public EventType getEventType() {
		return eventType;
	}

}
