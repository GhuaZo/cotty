package org.ghuazo.cotty.core.service;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.ghuazo.cotty.core.CottyConstant;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.util.DataTypeUtil;

public class CheckService extends CottyService{

	private static final Logger logger = Log.getLogger(CheckService.class);


	public CheckService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		CottyRequest request = new CottyRequest("https://ssl.ptlogin2.qq.com/check")
		.setMethod("GET")
		.addParams("uin", this.cottySession.getStringAttribute("userName"))
		.addParams("appid", CottyConstant.APPID);
		return request ;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		String content = cottyResponse.getContent() ; 
		Pattern pattern = Pattern.compile("ptui_checkVC\\('(.*?)','(.*?)','(.*?)'\\);");  
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			if (matcher.group(1).equals("0")) {
				byte[] keyBytes = DataTypeUtil.string2ByteArray(matcher.group(3));
				this.cottySession.addAttribute("verifyStatus", false)
				.addAttribute("verifyCode", matcher.group(2))
				.addAttribute("verifyKey", keyBytes);
			} else {
				logger.info("need verify!");
				byte[] keyBytes = DataTypeUtil.string2ByteArray(matcher.group(3));
				this.cottySession.addAttribute("verifyStatus", true)
				.addAttribute("verifyImgid", matcher.group(2))
				.addAttribute("verifyKey", keyBytes);
			}
		}else{
			throw new CottyException("check verify return unknown data");
		}
		
		logger.info("onComplete....");
	
	}

}
