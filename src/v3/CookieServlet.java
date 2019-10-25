package v3;

import java.io.PrintWriter;

public class CookieServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html;charset=utf-8");
		
		PrintWriter pw=response.getWriter();
		
		pw.print("<h1>测试cookie!</h1>");
		
		Cookie cookie=new Cookie("username","zhangsan");
		response.addCookie(cookie);
		//设置级别
		cookie=new Cookie("level","100");
		cookie.setMaxAge(60*60);//有效时间为1小时
	    response.addCookie(cookie);//响应为set-cookie(当添加了cookie值后，浏览器运行==》检查==》cookie.s==》netWork==》set-cookie:level=100; maxAge=3600;)
	    
	    cookie =new Cookie("page","1");
	    cookie.setPath("/page");
	    response.addCookie(cookie);
	    
	    cookie =new Cookie("user","test");
	    cookie.setPath("/user");
	    response.addCookie(cookie);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request,response);
	}
	
}
