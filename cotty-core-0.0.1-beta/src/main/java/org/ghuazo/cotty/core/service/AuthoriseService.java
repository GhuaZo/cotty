package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.util.RandomPool;

import com.alibaba.fastjson.JSONObject;

public class AuthoriseService extends CottyService {

	public AuthoriseService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		JSONObject jsonObject = new JSONObject() ; 
		jsonObject.put("status", ((CottyStatus) this.cottySession.getObjectAttribute("loginStatus")).getName()) ;
		jsonObject.put("ptwebqq", this.cottySession.getStringAttribute("ptwebqq")) ; 
		Long clientID = RandomPool.getInstance().createClientID();
		jsonObject.put("clientid", clientID);
		this.cottySession.addAttribute("clientid", clientID);
		System.out.println(jsonObject.toJSONString());
		String content = UrlEncoded.encodeString(jsonObject.toJSONString());
		CottyRequest cottyRequest = new CottyRequest("http://d.web2.qq.com/channel/login2") 
		.setMethod("POST")
		.addContent("r", content);
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		
		System.out.println(cottyResponse.getContent());
		//{"retcode":0,"result":{"uin":1940909810,"cip":3658199846,"index":1075,"port":36474,"status":"online","vfwebqq":"4984bc3bd6c943460f3244aa43f8623403310a07b94f5ed8902b47e822e9da8f83ec410e177cb0da","psessionid":"8368046764001d636f6e6e7365727665725f77656271714031302e3133332e34312e383400005e9200000045026e0400f2eeaf736d0000000a40614837314a3056636f6d000000284984bc3bd6c943460f3244aa43f8623403310a07b94f5ed8902b47e822e9da8f83ec410e177cb0da","user_state":0,"f":0}}
		JSONObject jsonObject = JSONObject.parseObject(cottyResponse.getContent());
		if(jsonObject.getInteger("retcode").equals(0)){
			JSONObject resultObject = jsonObject.getJSONObject("result");
			this.cottySession.addAttribute("uin", resultObject.getLong("uin"))
			.addAttribute("account", resultObject.getString("uin"))
			.addAttribute("cip", resultObject.getLong("cip"))
			.addAttribute("index", resultObject.getInteger("index"))
			.addAttribute("port", resultObject.getInteger("port"))
			.addAttribute("loginStatus", CottyStatus.nameOf(resultObject.getString("status")))
			.addAttribute("vfwebqq", resultObject.getString("vfwebqq"))
			.addAttribute("psessionid", resultObject.getString("psessionid"));
			
		}else{
			throw new CottyException("Authorise Error"+jsonObject.getInteger("retcode"));
		}
	}

}
