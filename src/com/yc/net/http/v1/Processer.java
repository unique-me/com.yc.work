package com.yc.net.http.v1;

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
     
     String suffix=request.getRequestURL().substring(request.getRequestURL().lastIndexOf(".")+1);
     String contentType;
     
  // 从 web.xml 文件中取 contentType， 替代之前的编码判断
  //String contentType = webXmlParser.getContentType(suffix);
     
     switch(suffix){
     case "js":
    	 contentType="application/x-javascript";break;
     case "css":
    	 contentType="text/css";break;
     case "jpg":
    	 contentType="image/jepg";break;
     case "bmp":
    	 contentType="image/bmp";break;
     case "gif":
    	 contentType="image/gif";break;
     case "png":
    	 contentType="image/png";break;
    default:
    	contentType="text/html";
    	
     }
     
     
		//简单输出(helloworld)用响应报文的格式
		String responseStr="HTTP/1.1 200 OK\r\n";
		responseStr+="Content-Type:" +contentType+"\r\n";
	//	responseStr+="Content-Type:text/html\r\n";//是一个页面
		responseStr+="\r\n";// CRLF 空行
	//空行之后就是返回给浏览器要显示的内容
	//	responseStr+="<h1>hello world</h1>";
		
		out.write(responseStr.getBytes());//输出（返回信息给浏览器）
		
		String rootPath="E:/photo";
		String filePath=request.getRequestURL();
		
		//判断访问文件是否存在,不存在跳404
		String diskPath=rootPath+filePath;
		if(new File(diskPath).exists()==false){
			diskPath=rootPath +"/404.html";
		}
		//文件输入流
		FileInputStream fis=new FileInputStream(diskPath);
		
		//向浏览器发送报文
		while((count=fis.read(buf))>0){
			out.write(buf,0,count);
		}
		fis.close();
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

