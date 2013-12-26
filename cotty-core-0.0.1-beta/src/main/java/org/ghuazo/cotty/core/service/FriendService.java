package org.ghuazo.cotty.core.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.entity.CottyFriend;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.util.FriendHashUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FriendService extends CottyService {

	public FriendService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("vfwebqq",
				this.cottySession.getStringAttribute("vfwebqq"));
		jsonObject.put("hash", FriendHashUtil.hash(
				this.cottySession.getLongAttribute("uin"),
				this.cottySession.getStringAttribute("ptwebqq")));
		String content = UrlEncoded.encodeString(jsonObject.toJSONString());
		CottyRequest cottyRequest = new CottyRequest(
				"http://s.web2.qq.com/api/get_user_friends2").setMethod("POST")
				.addContent("r", content);
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		JSONObject jsonObject = JSONObject.parseObject(cottyResponse
				.getContent());
		Map<Long, CottyFriend> cottyFriendMap = new HashMap<Long, CottyFriend>();
		
		if (jsonObject.getInteger("retcode").equals(0)) {
			JSONObject resultObject = jsonObject.getJSONObject("result");
			JSONArray friendArray = resultObject.getJSONArray("friends");
			JSONArray infoArray = resultObject.getJSONArray("info");
			JSONArray marknameArray = resultObject.getJSONArray("marknames");
			JSONArray vipArray = resultObject.getJSONArray("vipinfo");
			
			for (int i = 0; i < friendArray.size(); i++) {
				JSONObject friendObject = friendArray.getJSONObject(i);
				CottyFriend cottyFriend = new CottyFriend()
				.setUIN(friendObject.getLong("uin"));
				cottyFriendMap.put(friendObject.getLong("uin"), cottyFriend);
			}
			for(int i=0 ; i<infoArray.size() ; i++){
				JSONObject infoObject = infoArray.getJSONObject(i);
				CottyFriend cottyFriend = cottyFriendMap.get(infoObject.getLong("uin"));
				if(cottyFriend != null){
					cottyFriend.setFaceCode(infoObject.getInteger("face"))
					.setNick(infoObject.getString("nick"));
				}
				
			}
			
			for(int i=0 ; i<marknameArray.size() ; i++){
				JSONObject markObject = marknameArray.getJSONObject(i);
				CottyFriend cottyFriend = cottyFriendMap.get(markObject.getLong("uin"));
				if(cottyFriend != null){
					cottyFriend.setMarkName(markObject.getString("markname"));
				}
			}
			
			for(int i=0 ; i<vipArray.size();i++){
				JSONObject vipObject = vipArray.getJSONObject(i);
				CottyFriend cottyFriend = cottyFriendMap.get(vipObject.getLong("u"));
				if(cottyFriend != null){
					cottyFriend.setVIP(vipObject.getInteger("is_vip").equals(1)?true:false)
					.setVIPLevel(vipObject.getInteger("vip_level"));
				}
				
			}
			this.cottySession.addAttribute("cottyFriend", cottyFriendMap);
		} else {
			throw new CottyException("friend return unknow data...");
		}

	}

}
