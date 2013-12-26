package org.ghuazo.cotty.core.entity;

public class CottyGroup {

	private Long gid;

	private Long code;

	private String name;

	public Long getGID() {
		return gid;
	}

	public CottyGroup setGID(Long gid) {
		this.gid = gid;
		return this;
	}

	public Long getCode() {
		return code;
	}

	public CottyGroup setCode(Long code) {
		this.code = code;
		return this;
	}

	public String getName() {
		return name;
	}

	public CottyGroup setName(String name) {
		this.name = name;
		return this;
		}
	}
