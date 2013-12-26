package org.ghuazo.cotty.core.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.entity.CottyEvent;
import org.ghuazo.cotty.core.entity.CottyMessage;
import org.ghuazo.cotty.core.entity.DiscussMessage;
import org.ghuazo.cotty.core.entity.FriendMessage;
import org.ghuazo.cotty.core.entity.GroupMessage;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.util.RandomPool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PollService extends CottyService {

	private List<Integer> msgidList ;
	public PollService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
		this.msgidList = new ArrayList<Integer>() ; 
	}

	private JSONArray msgID2JSON(){
		JSONArray jsonArray = new JSONArray();
		for(Integer msgid:this.msgidList){
			jsonArray.add(msgid);
		}
		this.msgidList.clear();
		return jsonArray ; 
	}
	@Override
	public CottyRequest onRequest() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ptwebqq",
				this.cottySession.getStringAttribute("ptwebqq"));
		jsonObject.put("clientid", RandomPool.getInstance().createClientID());
		jsonObject.put("psessionid",
				this.cottySession.getStringAttribute("psessionid"));
		jsonObject.put("key", 0);
		jsonObject.put("ids", this.msgID2JSON());
		String content = UrlEncoded.encodeString(jsonObject.toJSONString());
		CottyRequest cottyRequest = new CottyRequest(
				"http://d.web2.qq.com/channel/poll2").setMethod("POST")
				.addContent("r", content);
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		Integer retcode = jsonObject.getInteger("retcode");
		if (retcode.equals(0)) {
			this.cottySession.addAttribute("action", "nornal");
			JSONArray resultArray = jsonObject.getJSONArray("result");
			List<CottyMessage> cottyMessageList = new ArrayList<CottyMessage>();
			List<CottyEvent> cottyEventList = new ArrayList<CottyEvent>() ; 
			for (int i = 0; i < resultArray.size(); i++) {
				JSONObject resultObject = resultArray.getJSONObject(i);
				JSONObject valueObject = resultObject.getJSONObject("value");
				this.msgidList.add(valueObject.getInteger("msg_id"));
				if("input_notify".equals(resultObject
						.getString("poll_type"))){
					Long inputUIN = valueObject.getLong("from_uin");
					CottyEvent cottyEvent = new CottyEvent(CottyEvent.Type.input);
					cottyEvent.addAttribute("inputUIN", inputUIN);
					cottyEventList.add(cottyEvent);
				}else if ("message".equals(resultObject.getString("poll_type"))) {
					FriendMessage friendMessage = new FriendMessage();
					friendMessage.setUIN(valueObject.getLong("from_uin"))
					.setContent(valueObject.getJSONArray("content").getString(1));
					cottyMessageList.add(friendMessage);
				} else if ("group_message".equals(resultObject
						.getString("poll_type"))) {
					GroupMessage groupMessage = new GroupMessage();
					groupMessage.setGroupUIN(valueObject.getLong("from_uin"))
					.setAuthorUIN(valueObject.getLong("send_uin"))
					.setContent(valueObject.getJSONArray("content")
											.getString(1));
					cottyMessageList.add(groupMessage);
				} else if ("discu_message".equals(resultObject
						.getString("poll_type"))) {
					DiscussMessage discussMessage = new DiscussMessage();
					discussMessage.setDID(valueObject.getLong("did"))
					.setAuthorUIN(valueObject.getLong("from_uin"))
					.setContent(valueObject.getJSONArray("content")
											.getString(1));
					cottyMessageList.add(discussMessage);
				}else if("buddies_status_change".equals(resultObject
						.getString("poll_type"))){
					//状态该改变信息
					Long changeUIN = valueObject.getLong("uin");
					String status = valueObject.getString("status");
					CottyStatus changeStatus = CottyStatus.nameOf(status);
					CottyEvent cottyEvent = new CottyEvent(CottyEvent.Type.chenge);
					cottyEvent.addAttribute("changeUIN", changeUIN).addAttribute("changeStatus", changeStatus);
					cottyEventList.add(cottyEvent);
				}else if("system_message".equals(resultObject
						.getString("poll_type"))){
					//系统消息，加好友验证等
					
					if("verify_required".equals(valueObject.getString("type"))){
						String account = valueObject.getString("account");
						Long requestUIN = valueObject.getLong("from_uin");
						String message = valueObject.getString("msg");
						CottyEvent cottyEvent = new CottyEvent(CottyEvent.Type.request);
						cottyEvent.addAttribute("account", account).addAttribute("requestUIN", requestUIN).addAttribute("message", message);
						cottyEventList.add(cottyEvent) ; 
					}
				}else if("sys_g_msg".equals(resultObject
						.getString("poll_type"))){
					if("group_leave".equals(valueObject.getString("type"))){
						System.out.println(valueObject.toJSONString());
						//被群踢出
						CottyEvent cottyEvent = new CottyEvent(CottyEvent.Type.kickout);
						cottyEvent.addAttribute("adminUIN", valueObject.getLong("from_uin"));
						cottyEvent.addAttribute("groupCode", valueObject.getString("t_gcode"));
						cottyEventList.add(cottyEvent) ; 
					}else if("group_admin_op".equals(valueObject.getString("type"))){
						//授权管理员
						if(valueObject.getInteger("op_type").equals(1)){
							//授权管理员
							CottyEvent cottyEvent = new CottyEvent(CottyEvent.Type.approve);
							cottyEvent.addAttribute("adminUIN", valueObject.getLong("from_uin"));
							cottyEvent.addAttribute("groupCode", valueObject.getString("t_gcode"));
							cottyEventList.add(cottyEvent) ; 
						}else if(valueObject.getInteger("op_type").equals(0)){
							CottyEvent cottyEvent = new CottyEvent(CottyEvent.Type.revoke);
							cottyEvent.addAttribute("adminUIN", valueObject.getLong("from_uin"));
							cottyEvent.addAttribute("groupCode", valueObject.getString("t_gcode"));
							cottyEventList.add(cottyEvent) ; 
						}
						
					}
				}else if("kick_message".equals(resultObject
						.getString("poll_type"))){
					//kick_message
					CottyEvent cottyEvent = new CottyEvent(CottyEvent.Type.logout);
					cottyEvent.addAttribute("reason", valueObject.getString("reason"));
					cottyEventList.add(cottyEvent) ; 
				}
				
			}
			this.cottySession.addAttribute("cottyMessage", cottyMessageList);
			this.cottySession.addAttribute("cottyEvent", cottyEventList);

		} else if(retcode.equals(102)){
			this.cottySession.addAttribute("action", "timeout");
		} else if (retcode.equals(116)) {
			this.cottySession.addAttribute("action", "changep");
			this.cottySession
					.addAttribute("ptwebqq", jsonObject.getString("p"));
		} else {
			this.cottySession.addAttribute("action", "error");
			throw new CottyException("poll return data error");
		}
	}

}
