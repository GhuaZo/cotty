package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

import com.alibaba.fastjson.JSONObject;

public class ApproveService  extends CottyService {

	public ApproveService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		//%7B%22account%22%3A137336521%2C%22gid%22%3A0%2C%22mname%22%3A%22%22%2C%22vfwebqq%22%3A%22545677a57e1c9fc1609e61ea3032d21941cf65b637cff1f8a067032076f8629a5ff8dd903dc2af46%22%7D
		
		//{"account":137336521,"gid":0,"mname":"","vfwebqq":"545677a57e1c9fc1609e61ea3032d21941cf65b637cff1f8a067032076f8629a5ff8dd903dc2af46"}
		
		
		JSONObject jsonObject = new JSONObject() ; 
		jsonObject.put("account", this.cottySession.getStringAttribute("approveAccount"));
		jsonObject.put("gid", 0);
		jsonObject.put("mname", this.cottySession.getStringAttribute("approveMarkName"));
		jsonObject.put("vfwebqq", this.cottySession.getStringAttribute("vfwebqq"));
		String content = UrlEncoded.encodeString(jsonObject.toJSONString());
		CottyRequest cottyRequest = new CottyRequest("http://s.web2.qq.com/api/allow_and_add2")
		.setMethod("POST").addContent("r", content);
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		// {"retcode":0,"result":{"result1":0,"account":137336521,"tuin":5328220,"stat":20}}
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		if(jsonObject.getInteger("retcode").equals(0)){
			JSONObject resultObject = jsonObject.getJSONObject("result") ; 
			if(resultObject.getInteger("result1").equals(0)){
				this.cottySession.addAttribute("approveAccount", resultObject.getString("account"))
				.addAttribute("approveUIN", resultObject.getLong("tuin"))
				.addAttribute("approveStatus", resultObject.getInteger("stat"));
			}else{
				throw new CottyException("approve failed");
			}
		}else{
			throw new CottyException("approve return unknow data");
		}
	}

}
