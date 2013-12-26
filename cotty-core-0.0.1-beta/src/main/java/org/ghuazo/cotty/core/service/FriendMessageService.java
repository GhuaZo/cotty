package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.entity.FriendMessage;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.util.RandomPool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FriendMessageService extends CottyService {

	public FriendMessageService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
		// TODO Auto-generated constructor stub
	}
	
	private FriendMessage friendMessage ; 

	@Override
	public CottyRequest onRequest() {
		// %7B%22to%22%3A5328220%2C%22face%22%3A534%2C%22content%22%3A%22%5B%5C%22aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa%5C%22%2C%5B%5C%22font%5C%22%2C%7B%5C%22name%5C%22%3A%5C%22%E5%AE%8B%E4%BD%93%5C%22%2C%5C%22size%5C%22%3A%5C%2210%5C%22%2C%5C%22style%5C%22%3A%5B0%2C0%2C0%5D%2C%5C%22color%5C%22%3A%5C%22000000%5C%22%7D%5D%5D%22%2C%22msg_id%22%3A27230003%2C%22clientid%22%3A%2249723222%22%2C%22psessionid%22%3A%228368046764001d636f6e6e7365727665725f77656271714031302e3133332e34312e38340000200500000164026e0400f2eeaf736d0000000a405748504f6a675643756d000000289880680218f1bf187a4c50d23dcfdb2cb54f96ce068ecbb965580b619316801a2414b54e9cee8e64%22%7D&clientid=49723222&psessionid=8368046764001d636f6e6e7365727665725f77656271714031302e3133332e34312e38340000200500000164026e0400f2eeaf736d0000000a405748504f6a675643756d000000289880680218f1bf187a4c50d23dcfdb2cb54f96ce068ecbb965580b619316801a2414b54e9cee8e64
		/*
		 * {"to":5328220,"face":534,"content":"[\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",[\"font
\",{\"name\":\"宋体\",\"size\":\"10\",\"style\":[0,0,0],\"color\":\"000000\"}]]","
msg_id":27230003,"clientid":"49723222","psessionid":"8368046764001d636f6e6e73657
27665725f77656271714031302e3133332e34312e38340000200500000164026e0400f2eeaf736d0
000000a405748504f6a675643756d000000289880680218f1bf187a4c50d23dcfdb2cb54f96ce068
ecbb965580b619316801a2414b54e9cee8e64"}&clientid=49723222&psessionid=83680467640
01d636f6e6e7365727665725f77656271714031302e3133332e34312e38340000200500000164026
e0400f2eeaf736d0000000a405748504f6a675643756d000000289880680218f1bf187a4c50d23dc
fdb2cb54f96ce068ecbb965580b619316801a2414b54e9cee8e64
		 * */
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("to", this.friendMessage.getUIN());
		jsonObject.put("face", this.cottySession.getIntegerAttribute("face"));
		jsonObject.put("content", this.creageContent());
		jsonObject.put("msg_id", RandomPool.getInstance().createMessageID());
		jsonObject.put("clientid", this.cottySession.getLongAttribute("clientid"));
		jsonObject.put("psessionid", this.cottySession.getStringAttribute("psessionid"));
		
		String content = UrlEncoded.encodeString(jsonObject.toJSONString()) ; 
		
		CottyRequest cottyRequest = new CottyRequest("https://d.web2.qq.com/channel/send_buddy_msg2")
		.setMethod("POST").addContent("r", content) ; 
		
		return cottyRequest;
	}
	
	private String creageContent(){
		/*
		 * [\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",[\"font
\",{\"name\":\"宋体\",\"size\":\"10\",\"style\":[0,0,0],\"color\":\"000000\"}]]
		 */
		JSONArray styleArray = new JSONArray();
		styleArray.add(0);
		styleArray.add(0);
		styleArray.add(0);
		
		JSONObject fontObject = new  JSONObject();
		fontObject.put("name", "宋体");
		fontObject.put("size", 10);
		fontObject.put("style", styleArray);
		fontObject.put("color", "000000");
		
		JSONArray fontArray = new JSONArray();
		fontArray.add("font");
		fontArray.add(fontObject);
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(this.friendMessage.getContent());
		jsonArray.add(fontArray);
		return jsonArray.toJSONString();
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		//{"retcode":0,"result":"ok"}
		String content = cottyResponse.getContent();
		System.out.println(content);
		JSONObject jsonObject = JSONObject.parseObject(content);
		if(jsonObject.getInteger("retcode").equals(0)){
			//发送成功
		}else{
			throw new CottyException("send friend error");
		}

	}

	public FriendMessage getFriendMessage() {
		return friendMessage;
	}

	public FriendMessageService setFriendMessage(FriendMessage friendMessage) {
		this.friendMessage = friendMessage;
		return this ; 
	}

}
