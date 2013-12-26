package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

import com.alibaba.fastjson.JSONObject;

public class VfwebqqService extends CottyService {

	public VfwebqqService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CottyRequest onRequest() {
		CottyRequest cottyRequest = new CottyRequest("http://s.web2.qq.com/api/getvfwebqq")
		.setMethod("GET")
		.addParams("ptwebqq", this.cottySession.getStringAttribute("ptwebqq"))
		.addParams("clientid", String.valueOf(this.cottySession.getLongAttribute("clientid")));
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		String content = cottyResponse.getContent() ; 
		JSONObject jsonObject = JSONObject.parseObject(content);
		if(jsonObject.getInteger("retcode").equals(0)){
			String vfwebqq = jsonObject.getJSONObject("result").getString("vfwebqq");
			this.cottySession.addAttribute("vfwebqq", vfwebqq);
		}else{
			throw new CottyException("Vfwebqq return unknow data.");
		}
	}

}
