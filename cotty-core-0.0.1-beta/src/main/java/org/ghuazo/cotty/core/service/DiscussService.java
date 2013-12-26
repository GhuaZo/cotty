package org.ghuazo.cotty.core.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.ghuazo.cotty.core.CottyException;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.entity.DiscussGroup;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DiscussService extends CottyService {

	public DiscussService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
	}

	@Override
	public CottyRequest onRequest() {
		CottyRequest cottyRequest = new CottyRequest("http://s.web2.qq.com/api/get_discus_list")
		.setMethod("GET")
		.addParams("clientid", String.valueOf(this.cottySession.getLongAttribute("clientid")))
		.addParams("psessionid",this.cottySession.getStringAttribute("psessionid"))
		.addParams("vfwebqq", this.cottySession.getStringAttribute("vfwebqq"));
		
		return cottyRequest;
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		String content = cottyResponse.getContent();
		JSONObject jsonObject = JSONObject.parseObject(content);
		Map<Long,DiscussGroup> discussGroupMap = new HashMap<Long,DiscussGroup>();
		if(jsonObject.getInteger("retcode").equals(0)){
			JSONObject resultObject = jsonObject.getJSONObject("result");
			JSONArray discussArray = resultObject.getJSONArray("dnamelist");
			for(int i=0 ; i<discussArray.size() ; i++){
				JSONObject discussObject = discussArray.getJSONObject(i);
				DiscussGroup discussGroup = new DiscussGroup()
				.setDID(discussObject.getLong("did"))
				.setName(discussObject.getString("name"));
				discussGroupMap.put(discussObject.getLong("did"), discussGroup);
			}
			this.cottySession.addAttribute("discussGroup", discussGroupMap);
		}else{
			throw new CottyException("discuss return unknow data...");
		}
	}

}
