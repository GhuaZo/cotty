package org.ghuazo.cotty.core.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.client.HttpClient;
import org.ghuazo.cotty.core.CottyConstant;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.util.PassWordUtil;

public class LoginService extends CottyService {

	public LoginService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		String cipher = PassWordUtil.encrypt(this.cottySession.getBytesAttribute("verifyKey"), this.cottySession.getStringAttribute("passWord"), this.cottySession.getStringAttribute("verifyCode"));
		CottyStatus loginStatus = ((CottyStatus)this.cottySession.getObjectAttribute("loginStatus"));
		//https://ssl.ptlogin2.qq.com/login?u=ghuazo@qq.com&p=73B331FA268963F9201CA93A21AACE87&verifycode=xbks&webqq_type=10&remember_uin=1&login2qq=1&aid=1003903&u1=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10&h=1&ptredirect=0&ptlang=2052&daid=164&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=10-32-3128910&mibao_css=m_webqq&t=1&g=1&js_type=0&js_ver=10059&login_sig=niZE6mAuEBf7sWT3IOKYAdZRs2bd0meLEHPTni6Zy7u*nNnelVuLPX0JBhGytbcT
		CottyRequest request = new CottyRequest("https://ssl.ptlogin2.qq.com/login?remember_uin=1&login2qq=1&u1=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10&h=1&ptredirect=0&ptlang=2052&daid=164&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=10-32-3128910&mibao_css=m_webqq&t=1&g=1&js_type=0&js_ver=10059&login_sig=niZE6mAuEBf7sWT3IOKYAdZRs2bd0meLEHPTni6Zy7u*nNnelVuLPX0JBhGytbcT")
		.setMethod("GET")
		.addParams("u", this.cottySession.getStringAttribute("userName"))
		.addParams("p", cipher)
		.addParams("webqq_type",String.valueOf(loginStatus.getIndex()))
		.addParams("verifycode", this.cottySession.getStringAttribute("verifyCode"))
		.addParams("aid", CottyConstant.APPID);
		return request;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		//ptuiCB('0','0','http://ptlogin4.web2.qq.com/check_sig?pttype=1&uin=1940909810&service=login&nodirect=0&ptsig=MW*obMeM4ZbHzWIQxxxQnboRabUpf76PB0IIO89AmUE_&s_url=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10&f_url=&ptlang=2052&ptredirect=100&aid=1003903&daid=164&j_later=0&low_login_hour=0&regmaster=0&pt_login_type=1&pt_aid=0&pt_aaid=0&pt_light=0','0','登录成功！', 'Studio');
		String content = cottyResponse.getContent() ; 
		System.out.println(content);
		Pattern pattern = Pattern.compile("ptuiCB\\('(\\d+)','(\\d+)','(.*?)','(\\d+)','(.*?)', '(.*?)'\\);");  
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			this.cottySession.addAttribute("redirect", matcher.group(3))
			.addAttribute("nick", matcher.group(6)).addAttribute("message", matcher.group(5));
		}else{
			throw new CottyException("login return unknown data");
		}
		
	}

}
