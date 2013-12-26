package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

import com.alibaba.fastjson.JSONObject;

public class InitializeService extends CottyService {

	public InitializeService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		return new CottyRequest("http://s.web2.qq.com/api/get_self_info2")
				.setMethod("GET");
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		if (jsonObject.getInteger("retcode").equals(0)) {
			JSONObject resultOBject = jsonObject.getJSONObject("result");
			cottySession.addAttribute("country", resultOBject.getString("country"))
			.addAttribute("city", resultOBject.getString("city"))
			.addAttribute("nick", resultOBject.getString("nick"))
			.addAttribute("signature", resultOBject.getString("lnick"));
			//.addAttribute("account", resultOBject.getString("account"));
		} else {
			throw new CottyException("initialize return unknow data.");
		}
	}

}
