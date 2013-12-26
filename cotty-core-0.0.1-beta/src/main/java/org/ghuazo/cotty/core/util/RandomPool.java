package org.ghuazo.cotty.core.util;

public class RandomPool {

	private static RandomPool instance = new RandomPool(); 
	
	private RandomPool(){
		
	}
	
	public static RandomPool getInstance(){
		return instance ; 
	}
	
	public Long createClientID(){
		return 12998943l;
	}
	private Long x=10000l;
	public Long createMessageID(){
		return x++;
	}
	
}
