package org.ghuazo.cotty.core;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.internal.runtime.ConsString;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

/**
QQ登录以后获取好友列表的hash值获取方法，由于此算法经常变，一般2-3周换一次，所以不给大家翻译成java代码java实现采用的java执行javascript代码的方式
本实例在2013年12月11日能用，获取最新算法WEBQQ协议相关软件请加群93772282
作者GhuaZo
**/
public class TestJavaScript {
	
	public static void main(String args[]) throws Exception{
		String script = "P=function(b,i){for(var a=[],s=0;s<i.length;s++)a[s%4]^=i.charCodeAt(s);var j=[\"EC\",\"OK\"],d=[];d[0]=b>>24&255^j[0].charCodeAt(0);d[1]=b>>16&255^j[0].charCodeAt(1);d[2]=b>>8&255^j[1].charCodeAt(0);d[3]=b&255^j[1].charCodeAt(1);j=[];for(s=0;s<8;s++)j[s]=s%2==0?a[s>>1]:d[s>>1];a=[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"A\",\"B\",\"C\",\"D\",\"E\",\"F\"];d=\"\";for(s=0;s<j.length;s++)d+=a[j[s]>>4&15],d+=a[j[s]&15];return d}";
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager
				.getEngineByMimeType("application/javascript");
		engine.eval(script);
		Invocable invocable = (Invocable) engine;
		String result = invocable.invokeFunction("P","1940909810","5db57783ca7961b93267e7a9baf0450a98fa0492dd4e2d67208ba7e499797387").toString();
		System.out.println(result);	
	}
}
