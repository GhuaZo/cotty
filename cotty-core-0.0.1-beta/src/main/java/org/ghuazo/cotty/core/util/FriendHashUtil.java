package org.ghuazo.cotty.core.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class FriendHashUtil {
	public static String hash(Long uin, String ptwebqq) {
		String username = String.valueOf(uin);
		//String script = "P=function(b,i){for(var a=[],s=0;s<i.length;s++)a[s%4]^=i.charCodeAt(s);var j=[\"EC\",\"OK\"],d=[];d[0]=b>>24&255^j[0].charCodeAt(0);d[1]=b>>16&255^j[0].charCodeAt(1);d[2]=b>>8&255^j[1].charCodeAt(0);d[3]=b&255^j[1].charCodeAt(1);j=[];for(s=0;s<8;s++)j[s]=s%2==0?a[s>>1]:d[s>>1];a=[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"A\",\"B\",\"C\",\"D\",\"E\",\"F\"];d=\"\";for(s=0;s<j.length;s++)d+=a[j[s]>>4&15],d+=a[j[s]&15];return d}";
		//2014-01-12更新
		String script = "P=function(b,i){for(var a=[],s=0;s<i.length;s++)a[s%4]^=i.charCodeAt(s);var j=[\"EC\",\"OK\"],d=[];d[0]=b>>24&255^j[0].charCodeAt(0);d[1]=b>>16&255^j[0].charCodeAt(1);d[2]=b>>8&255^j[1].charCodeAt(0);d[3]=b&255^j[1].charCodeAt(1);j=[];for(s=0;s<8;s++)j[s]=s%2==0?a[s>>1]:d[s>>1];a=[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"A\",\"B\",\"C\",\"D\",\"E\",\"F\"];d=\"\";for(s=0;s<j.length;s++)d+=a[j[s]>>4&15],d+=a[j[s]&15];return d}";
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager
				.getEngineByMimeType("application/javascript");
		String result=null;
		try {
			engine.eval(script);

			Invocable invocable = (Invocable) engine;
			
			result = invocable.invokeFunction("P", username, ptwebqq)
					.toString();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return result;

	}
}
