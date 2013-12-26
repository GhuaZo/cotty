package org.ghuazo.cotty.core;

import org.eclipse.jetty.util.UrlEncoded;

public class URLEncoder {

	public static void main(String[] args) {
		String data = "%7B%22clientid%22%3A12998943%2C%22content%22%3A%22%5B%5C%22%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A+%5C%22%2C%5B%5C%22font%5C%22%2C%7B%5C%22color%5C%22%3A%5C%22000000%5C%22%2C%5C%22name%5C%22%3A%5C%22%E5%AE%8B%E4%BD%93%5C%22%2C%5C%22size%5C%22%3A10%2C%5C%22style%5C%22%3A%5B0%2C0%2C0%5D%7D%5D%5D%22%2C%22group%5Fuin%22%3A2575472604%2C%22msg%5Fid%22%3A12998213%2C%22psessionid%22%3A%228368046764001d636f6e6e7365727665725f77656271714031302e3133332e34312e38340000068a00000177026e0400f2eeaf736d0000000a404f355759636e63496c6d000000286b9dd6d5ef94e51c08e9af11479806a20006030fc88141f9eb709d3d9c8b22d06c8298c189e05c5a%22%7D";
		data = decode(data);
		System.out.println(data);
	}

	private static String decode(String data){
		data = UrlEncoded.decodeString(data, 0, data.length(), null);
		return data ; 
	}
	
}
