package org.ghuazo.cotty.core.service;

import org.eclipse.jetty.client.HttpClient;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;

public class RedirectService extends CottyService {

	public RedirectService(HttpClient httpClient, CottySession cottySession) {
		super(httpClient, cottySession);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CottyRequest onRequest() {
		return  new CottyRequest(this.cottySession.getStringAttribute("redirect")).setMethod("GET");
	}

	@Override
	public void onResponse(CottyResponse cottyResponse) {
		
	}

}
