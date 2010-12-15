package com.cadrlife.domino;


import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;


public class DominoTest {
	private Domino domino;
	private Server server;
	private String baseUrl;
	@Before
	public void beforeEach() throws Exception {
		int port = 5000;
		baseUrl = "http://localhost:" + port;
		domino = new Domino();
		domino.init();
		server = new Server(port);
        Context root = new Context(server,"/",Context.SESSIONS);
        root.addServlet(new ServletHolder(new HelloServlet()), "/hello");
        server.start();
	}
	
	@After
	public void afterEach() throws Exception {
		server.stop();
	}
	
	@Test
	public void canQueryElementsForText() throws Exception {
		domino.navigateTo(baseUrl + "/hello");
		assertEquals("Hello", domino.$("h1").text().toString());
	}
	
	@Test
	public void canQueryElementsForHtml() throws Exception {
		domino.navigateTo(baseUrl + "/hello");
		assertEquals("<b>Hello</b>", domino.$("h1").html().toString());
	}
	
	@Test
	public void canPassUrlParameters() throws Exception {
		domino.navigateTo(baseUrl + "/hello?name=Ray");
		assertEquals("Ray", domino.$("h2").text().toString());
	}
	
	@Test
	public void canUseJQuerySelectors() throws Exception {
		domino.navigateTo(baseUrl + "/hello");
		assertEquals("paragraph with id", domino.$("p#pwi").text().toString());
	}
	
	@Test
	public void canExtractAttributes() throws Exception {
		domino.navigateTo(baseUrl + "/hello");
		assertEquals("pwi", domino.$("p#pwi").attr("id").toString());
	}
	
}
