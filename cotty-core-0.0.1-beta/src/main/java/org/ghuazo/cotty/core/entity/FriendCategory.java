package org.ghuazo.cotty.core.entity;

import java.util.HashMap;
import java.util.Map;

public class FriendCategory {

	private Map<Long,CottyFriend> cottyFriend ;
	private String categoryName;
	private Integer categoryIndex;
	private Integer categoryShort;
	
	public FriendCategory(){
		this.cottyFriend = new HashMap<Long,CottyFriend>() ; 
	}
	
	public FriendCategory addCottyFriend(CottyFriend cottyFriend){
		this.cottyFriend.put(cottyFriend.getUIN(), cottyFriend);
		return this ; 
	}

	public Map<Long,CottyFriend> getCottyFriend(){
		return this.cottyFriend;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public FriendCategory setCategoryName(String categoryName) {
		this.categoryName = categoryName;
		return this ;
	}

	public Integer getCategoryIndex() {
		return categoryIndex;
	}

	public FriendCategory setCategoryIndex(Integer categoryIndex) {
		this.categoryIndex = categoryIndex;
		return this ;
	}

	public Integer getCategoryShort() {
		return categoryShort;
	}

	public FriendCategory setCategoryShort(Integer categoryShort) {
		this.categoryShort = categoryShort;
		return this ; 
	}
	
}
