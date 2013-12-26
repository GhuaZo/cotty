package org.ghuazo.cotty.core.entity;

import org.ghuazo.cotty.core.CottyConstant.CottyStatus;

public class CottyFriend {

	private Long UIN;
	private Integer flag;
	private boolean isVIP;
	private Integer VIPLevel;
	private Integer FaceCode;
	private String nick;
	private String markName;
	private CottyStatus cottyStatus ; 

	public Long getUIN() {
		return UIN;
	}

	public CottyFriend setUIN(Long uIN) {
		UIN = uIN;
		return this;
	}

	public Integer getFlag() {
		return flag;
	}

	public CottyFriend setFlag(Integer flag) {
		this.flag = flag;
		return this;
	}

	public boolean isVIP() {
		return isVIP;
	}

	public CottyFriend setVIP(boolean isVIP) {
		this.isVIP = isVIP;
		return this;
	}

	public Integer getVIPLevel() {
		return VIPLevel;
	}

	public CottyFriend setVIPLevel(Integer vIPLevel) {
		VIPLevel = vIPLevel;
		return this;
	}

	public Integer getFaceCode() {
		return FaceCode;
	}

	public CottyFriend setFaceCode(Integer faceCode) {
		FaceCode = faceCode;
		return this;
	}

	public String getNick() {
		return nick;
	}

	public CottyFriend setNick(String nick) {
		this.nick = nick;
		return this;
	}

	public String getMarkName() {
		return markName;
	}

	public CottyFriend setMarkName(String markName) {
		this.markName = markName;
		return this;
	}

	public CottyStatus getCottyStatus() {
		return cottyStatus;
	}

	public CottyFriend setCottyStatus(CottyStatus cottyStatus) {
		this.cottyStatus = cottyStatus;
		return this;
	}

	
}
