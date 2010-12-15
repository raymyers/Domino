package com.cadrlife.domino.internal;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.common.collect.Maps;

public class FakeHttpRequest {
	public HttpServlet servlet;
	String method;
	String url;
	String data;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	public void execute() {
		System.err.println("execute " + method + " " + url);
		try {
//			System.out.println("servlet");
			URI uri = new URI(url);
			request = new MockHttpServletRequest();
			request.setQueryString(uri.getQuery());
			request.setMethod(method);
			response = new MockHttpServletResponse();
			servlet.service(request, response);
//			assertEquals("ray", request.getParameter("name"));
			System.out.println(this.getResponseText());
			
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Map<String,String> getResponseHeaders() {
		HashMap<String, String> headerMap = Maps.newHashMap();
		@SuppressWarnings("unchecked")
		Set<String> headerNames = response.getHeaderNames();
		for (String headerName : headerNames) {
			headerMap.put(headerName, (String)response.getHeader(headerName));
			System.out.println(headerName);
		}
		headerMap.put("Content-Type", response.getContentType());
		return headerMap;
	}

	public int getResponseStatus() {
		return response.getStatus();
	}

	public String getResponseText() {
		try {
			return response.getContentAsString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
