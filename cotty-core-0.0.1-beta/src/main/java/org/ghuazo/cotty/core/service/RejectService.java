package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

import com.alibaba.fastjson.JSONObject;

public class RejectService extends CottyService {

	public RejectService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		//%7B%22account%22%3A137336521%2C%22msg%22%3A%22ggggggggggg%22%2C%22vfwebqq%22%3A%22545677a57e1c9fc1609e61ea3032d21941cf65b637cff1f8a067032076f8629a5ff8dd903dc2af46%22%7D
		//{"account":137336521,"msg":"ggggggggggg","vfwebqq":"545677a57e1c9fc1609e61ea3032d21941cf65b637cff1f8a067032076f8629a5ff8dd903dc2af46"}
		JSONObject jsonObject = new JSONObject() ; 
		jsonObject.put("account", this.cottySession.getStringAttribute("rejectAccount"));
		jsonObject.put("msg", this.cottySession.getStringAttribute("rejectMessage"));
		jsonObject.put("vfwebqq", this.cottySession.getStringAttribute("vfwebqq"));
		String content = UrlEncoded.encodeString(jsonObject.toJSONString());
		
		
		CottyRequest cottyRequest = new CottyRequest("http://s.web2.qq.com/api/deny_added_request2")
		.setMethod("POST").addContent("r", content);
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		//{"retcode":0,"result":{"result":0}}
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		if(jsonObject.getInteger("retcode").equals(0)){
			JSONObject resultObject = jsonObject.getJSONObject("result") ; 
			if(resultObject.getInteger("result").equals(0)){
				//拒绝成功
			}else{
				throw new CottyException("reject failed");
			}
		}else{
			throw new CottyException("reject return unknow data");
		}
	}

}
