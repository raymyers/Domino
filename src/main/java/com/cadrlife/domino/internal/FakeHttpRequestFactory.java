package com.cadrlife.domino.internal;

public class FakeHttpRequestFactory {
	public FakeHttpRequest newInstance() {
		FakeHttpRequest fakeHttpRequest = new FakeHttpRequest();
//		Server server = new Server(8080);
//        Context root = new Context(server,"/",Context.SESSIONS);
//        root.addServlet(new ServletHolder(new HelloServlet()), "/hello");
		return fakeHttpRequest;
	}
}
