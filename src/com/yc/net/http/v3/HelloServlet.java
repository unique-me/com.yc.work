package com.yc.net.http.v3;

public class HelloServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		/**
		 * servlet:
		 * 1.页面跳转(请求转发，响应重定向)
		 * 
		 * 2.直接输出html 内容或者json数据（ajax请求）
		 */
		response.sendRedirect("index.html");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request,response);
	}

}
