package org.ghuazo.cotty.core.service;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Response.CompleteListener;
import org.eclipse.jetty.client.api.Response.ContentListener;
import org.eclipse.jetty.client.api.Response.FailureListener;
import org.eclipse.jetty.client.api.Response.SuccessListener;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.Utf8StringBuilder;
import org.ghuazo.cotty.core.CottySession;
import org.ghuazo.cotty.core.type.CottyRequest;
import org.ghuazo.cotty.core.type.CottyResponse;
import org.ghuazo.cotty.core.type.CottyResponse.Status;

public abstract class CottyService implements SuccessListener, FailureListener,
		CompleteListener, ContentListener {

	public interface ServiceCallback {
		public void onServiceComplete(CottySession cottySession);
	}

	private HttpClient httpClient;

	private ServiceCallback serviceCallback;

	private CottyResponse cottyResponse;
	
	protected CottySession cottySession ;
	
	private Utf8StringBuilder stringBuilder ;
	

	public CottyService(HttpClient httpClient,CottySession cottySession) {
		this.httpClient = httpClient ;
		this.cottySession = cottySession; 
		this.cottyResponse = new CottyResponse();
		this.stringBuilder = new Utf8StringBuilder();
		
	}

	public void onSuccess(Response response) {
		this.cottyResponse.setStatus(Status.SUCCESS);
		this.cottyResponse.setContent(this.stringBuilder.toString());
		this.stringBuilder.reset();
		this.cottyResponse.setCookieStore(this.httpClient.getCookieStore());
	}

	public void onFailure(Response response, Throwable failure) {
		this.cottyResponse.setStatus(Status.FAILURE);

	}

	public void onComplete(Result result) {
		this.onResponse(this.cottyResponse);
		this.cottySession.addAttribute("focusService", this);	
		this.serviceCallback.onServiceComplete(cottySession);
	}

	public void onContent(Response response, ByteBuffer content) {
		this.stringBuilder.append(content);
		
	}

	public abstract CottyRequest onRequest();

	public abstract void onResponse(CottyResponse cottyResponse);

	public void execute(ServiceCallback serviceCallback) {
		this.serviceCallback = serviceCallback;
		CottyRequest cottyRequest = this.onRequest();
		Request request = this.httpClient.newRequest(cottyRequest.getUrl())
				.method(cottyRequest.getMethod());
		Map<String, String> cottyParams = cottyRequest.getParams();
		Iterator<Entry<String, String>> paramsIterator = cottyParams.entrySet()
				.iterator();
		while (paramsIterator.hasNext()) {
			Entry<String, String> params = paramsIterator.next();
			request.param(params.getKey(), params.getValue());
		}
		
		Map<String, String> cottyContent = cottyRequest.getContent();
		Iterator<Entry<String, String>> contentIterator = cottyContent
				.entrySet().iterator();
		StringBuffer stringBuffer = new StringBuffer();
		while (contentIterator.hasNext()) {
			Entry<String, String> content = contentIterator.next();
			stringBuffer.append(content.getKey()).append("=")
					.append(content.getValue());
		}
		request.content(new StringContentProvider(stringBuffer.toString()));
		request.header("Content-Type", "application/x-www-form-urlencoded");
		request.header("Referer","http://d.web2.qq.com/proxy.html");
		request.header("Origin", "http://d.web2.qq.com"); 
		request.header("Connection", "keep-alive");
		request.send(this);
	}

}
