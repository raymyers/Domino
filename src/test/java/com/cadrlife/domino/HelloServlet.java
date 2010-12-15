package com.cadrlife.domino;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html");

		String name = req.getParameter("name");
		PrintWriter out = res.getWriter();
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Hello</TITLE></HEAD>");
		out.println("<BODY>");
		out.println("<h1><b>Hello</b></h1>");
		out.println("<h2>" +name+"</h1>");
		out.println("<p>paragraph without id</p>");
		out.println("<p id='pwi'>paragraph with id</p>");
		out.println("</BODY></HTML>");
	}

	public String getServletInfo() {
		return "A servlet that knows the name of the person to whom it's"
				+ "saying hello";
	}
}