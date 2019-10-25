package com.yc.net.http.v2;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
/**
 * Processer处理器，处理请求给予响应
 * @author GSH
 *
 */
public class Processer {
	
	//定义servlet容器
	private HashMap<String,HttpServlet>servletContainer=new HashMap<>();
	
	//实例块
	{
		//添加一个Servlet
		//响应重定向 地址改变
		//servletContainer.put("/hello.s", new HelloServlet());
		//添加一个servlet实现请求转发
		servletContainer.put("/redirect.s", new RedirectServlet());
		//请求转发 地址不变
		servletContainer.put("/forward.s", new ForwardServlet());
	}
	
	public void process(Socket socket){
	InputStream in;//输入流
	OutputStream out;//输出流
	try{
	    in=socket.getInputStream();
	    out=socket.getOutputStream();
	    //读取请求报文内容
		byte[] buf=new byte[1024];
		int count;
		count=in.read(buf);
		String content=new String(buf,0, count);
		System.out.println(content);
		//parseRequest(content);
		
	//解析请求报文
     HttpServletRequest request=parseRequest(content);	
     HttpServletResponse response=new HttpServletResponse(request,out);
    
     
     /**
      * 静态请求：对应着一个html,js,css...
      * 动态请求：
      * 非法404请求 即没有物理文件也没有虚拟的地址
      * 
      */
     
     //判断物理文件是否存在
        String rootPath="E:/photo/";
		String webPath=request.getRequestURL();
	//判断访问文件是否存在,不存在跳404
		String diskPath=rootPath+webPath;
		if(new File(diskPath).exists()==true){
			//response.commit();
			//静态请求直接commit
		}else if(servletContainer.containsKey(webPath)){
			//127.0.0.1:8080/hello.s
			//判断虚拟路径中有没有该地址（servlet 中去找）
			//动态请求先由servlet处理
			HttpServlet servlet=servletContainer.get(webPath);
			servlet.service(request, response);
			
		}else{
			//404 改写资源文件名404.html
			response.setStatus(404,"Not Found");
			request.setRequestURL("/404.html");
		}
		response.commit();
   
	}catch(IOException e){
		e.printStackTrace();
	}
	try{
		
		socket.close();
	}catch(IOException e){
		e.printStackTrace();
	}
	//解析请求报文
	//给予对应的响应
}
	public HttpServletRequest parseRequest(String content){
		HttpServletRequest request=new HttpServletRequest(content);
		return request;
	}
}

