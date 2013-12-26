package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.entity.CottyMessage;
import org.ghuazo.cotty.core.entity.DiscussMessage;
import org.ghuazo.cotty.core.entity.FriendMessage;
import org.ghuazo.cotty.core.entity.GroupMessage;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.util.RandomPool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MessageService extends CottyService {

	private CottyMessage cottyMessage;

	public MessageService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		CottyRequest cottyRequest = null;
		JSONObject jsonObject = new JSONObject();
		if (this.cottyMessage instanceof FriendMessage) {
			cottyRequest = new CottyRequest(
					"https://d.web2.qq.com/channel/send_buddy_msg2");
			FriendMessage friendMessage = (FriendMessage) this.cottyMessage;
			jsonObject.put("to", friendMessage.getUIN());
			jsonObject.put("face", cottySession.getIntegerAttribute("face"));
		} else if (this.cottyMessage instanceof GroupMessage) {
			cottyRequest = new CottyRequest(
					"http://d.web2.qq.com/channel/send_qun_msg2");
			GroupMessage groupMessage = (GroupMessage) this.cottyMessage;
			jsonObject.put("group_uin", groupMessage.getGroupUIN());
		} else if (this.cottyMessage instanceof DiscussMessage) {
			cottyRequest = new CottyRequest(
					"http://d.web2.qq.com/channel/send_discu_msg2");
			DiscussMessage discussMessage = (DiscussMessage) this.cottyMessage;
			jsonObject.put("did", discussMessage.getDID());
		}
		jsonObject.put("content",
				this.creageContent(this.cottyMessage.getContent()));
		jsonObject.put("msg_id", RandomPool.getInstance().createMessageID());
		jsonObject.put("clientid",
				this.cottySession.getLongAttribute("clientid"));
		jsonObject.put("psessionid",
				this.cottySession.getStringAttribute("psessionid"));

		String content = UrlEncoded.encodeString(jsonObject.toJSONString());

		cottyRequest.setMethod("POST").addContent("r", content);

		return cottyRequest;
	}

	private String creageContent(String content) {
		/*
		 * [\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",[\"font \
		 * ",{\"name\":\"宋体\",\"size\":\"10\",\"style\":[0,0,0],\"color\":\"000000\"}]]
		 */
		JSONArray styleArray = new JSONArray();
		styleArray.add(0);
		styleArray.add(0);
		styleArray.add(0);

		JSONObject fontObject = new JSONObject();
		fontObject.put("name", "宋体");
		fontObject.put("size", 10);
		fontObject.put("style", styleArray);
		fontObject.put("color", "000000");

		JSONArray fontArray = new JSONArray();
		fontArray.add("font");
		fontArray.add(fontObject);

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(content);
		jsonArray.add(fontArray);
		return jsonArray.toJSONString();
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		// {"retcode":0,"result":"ok"}
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		if (jsonObject.getInteger("retcode").equals(0)) {
			// 发送成功
		} else {
			throw new CottyException("send Message error");
		}
	}

	public MessageService setCottyMessage(CottyMessage cottyMessage) {
		this.cottyMessage = cottyMessage;
		return this;
	}

}
