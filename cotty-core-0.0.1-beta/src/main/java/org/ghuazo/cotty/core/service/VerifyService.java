package org.ghuazo.cotty.core.service;

import java.nio.ByteBuffer;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.util.BufferUtil;
import org.ghuazo.cotty.core.CottyConstant;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

public class VerifyService extends CottyService {

	public VerifyService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		// https://ssl.captcha.qq.com/getimage?aid=1003903&r=0.7630009190179408&uin=ghuazo@qq.com
		CottyRequest request = new CottyRequest("https://ssl.captcha.qq.com/getimage")
		.setMethod("GET")
		.addParams("uin", this.cottySession.getStringAttribute("userName"))
		.addParams("aid", CottyConstant.APPID);
		return request;
	}

	private byte[] verifyImage;
	
	@Override
	public void onContent(Response response, ByteBuffer content) {
		verifyImage = BufferUtil.toArray(content) ; 
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		this.cottySession.addAttribute("verifyImage", verifyImage);
	}
	
}
