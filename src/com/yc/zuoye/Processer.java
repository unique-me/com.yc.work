package com.yc.zuoye;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/**
 * Processer处理器，处理请求给予响应
 * @author GSH
 *
 */
public class Processer {
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
		
	//解析请求报文（暂未实现）
     HttpServletRequest request=parseRequest(content);	
     
    
     
		//简单输出(helloworld)用响应报文的格式
		String responseStr="HTTP/1.1 200 OK\r\n";
		responseStr+="Content-Type: text/xml\r\n";
	//	responseStr+="Content-Type:text/html\r\n";//是一个页面
		responseStr+="\r\n";// CRLF 空行
	//空行之后就是返回给浏览器要显示的内容
	//	responseStr+="<h1>hello world</h1>";
		
		out.write(responseStr.getBytes());//输出（返回信息给浏览器）
		
		String rootPath="E:/photo";
		String filePath=request.getRequestURL();
		
		
		
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

