package org.ghuazo.cotty.core.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.entity.CottyGroup;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GroupService extends CottyService {

	public GroupService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
		
	}

	@Override
	public CottyRequest onRequest() {
		JSONObject jsonObject = new JSONObject() ; 
		jsonObject.put("vfwebqq", this.cottySession.getStringAttribute("vfwebqq"));
		String content = UrlEncoded.encodeString(jsonObject.toJSONString());
		CottyRequest cottyRequest = new CottyRequest("http://s.web2.qq.com/api/get_group_name_list_mask2")
		.setMethod("POST")
		.addContent("r", content);
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		Map<Long,CottyGroup> cottyGroupMap = new HashMap<Long,CottyGroup>();
		if(jsonObject.getInteger("retcode").equals(0)){
			JSONObject resultObject = jsonObject.getJSONObject("result");
			JSONArray groupArray = resultObject.getJSONArray("gnamelist");
			for(int i=0;i<groupArray.size();i++){
				JSONObject groupObject = groupArray.getJSONObject(i);
				CottyGroup cottyGroup = new CottyGroup()
				.setGID(groupObject.getLong("gid"))
				.setCode(groupObject.getLong("code"))
				.setName(groupObject.getString("name"));
				cottyGroupMap.put(groupObject.getLong("code"), cottyGroup);
			}
			this.cottySession.addAttribute("cottyGroup", cottyGroupMap);
		}else{
			throw new CottyException("group return unknow data...");
		}
	}

}
