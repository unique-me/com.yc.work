package com.yc.net.http.v3;

import java.io.PrintWriter;

public class CookieServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
	
		response.setContentType("text/html;charset=utf-8");
		
		PrintWriter pw=response.getWriter();
		
		pw.print("<h1>测试cookie!</h1>");
		
		Cookie cookie=new Cookie("username","zhangsan");
		
		response.addCookie(cookie);
		
		cookie=new Cookie("level","100");
		cookie.setMaxAge(60*60);
		
		response.addCookie(cookie);
		
		
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request,response);
	}

}
