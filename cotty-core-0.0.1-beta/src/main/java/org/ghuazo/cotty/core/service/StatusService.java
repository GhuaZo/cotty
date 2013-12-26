package org.ghuazo.cotty.core.service;

import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.entity.CottyFriend;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StatusService extends CottyService {

	public StatusService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		/**
		 * http://d.web2.qq.com/channel/get_online_buddies2?vfwebqq=ba695fbbea182a56227b0e31f571d9da486e6387335161e77254337c5cf7962ea320f46a1a55ca5f&clientid=53999199&psessionid=8368046764001d636f6e6e7365727665725f77656271714031302e3133392e372e313630000054da000000a0036e0400c9962f086d0000000a405351356446504433636d00000028514e50e6cd97ba0615c83a4d73449073c522003e14d6a1526ee1652b113fcfc2370df4078ca269ad&t=1386911107076
		 */
		CottyRequest request = new CottyRequest("http://d.web2.qq.com/channel/get_online_buddies2")
		.setMethod("GET")
		.addParams("vfwebqq", this.cottySession.getStringAttribute("vfwebqq"))
		.addParams("clientid", String.valueOf(this.cottySession.getLongAttribute("clientid")))
		.addParams("psessionid",this.cottySession.getStringAttribute("psessionid"));
		return request;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		Map<Long, CottyFriend> cottyFriendMap = (Map<Long, CottyFriend>) this.cottySession
				.getObjectAttribute("cottyFriend");
		if(jsonObject.getInteger("retcode").equals(0)){
			JSONArray resultArray = jsonObject.getJSONArray("result");
			for(int i=0 ; i<resultArray.size() ; i++){
				JSONObject statusObject = resultArray.getJSONObject(i);
				CottyFriend cottyFriend = cottyFriendMap.get(statusObject.getLong("uin"));
				cottyFriend.setCottyStatus(CottyStatus.nameOf(statusObject.getString("status")));
			}
		}else{
			throw new CottyException("group return unknow data...");
		}
	}

}
