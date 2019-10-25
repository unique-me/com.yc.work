package com.yc.net.http.v3;

import java.util.HashMap;

public class HttpServletRequest {
	
	/**
	 * GET /indecaacx.html HTTP/1.1
	 * request.getRequeatURL();//请求的地址   如：indecaacx.html
	 * request.getMethod();//请求的方法     如：GET 
	 * request.getProtocol();//请求的协议版本编号 如： 1.1
	 * request.getHeader("Host");//返回头域的指定字段值
	 * 
	 */

	
//get请求没有body不用解析   \s空格符
	
private String requestURL;
private String method;
private String protocol;
private HashMap<String,String>headerMap=new HashMap<>();
//构造方法里解析
public HttpServletRequest(String content) {
	//解析请求文件
		String[] lines=content.split("\r\n");
		for(int i=0;i<lines.length;i++){
			if(i==0){
				//解析头行，以空格为间隔符
				String[] topLines=lines[i].split("\\s");
				method=topLines[0];
				requestURL=topLines[1];
				protocol=topLines[2];
			}else{
				//解析头域
				String[] headerLines=lines[i].split(":\\s");
				headerMap.put(headerLines[0], headerLines[1]);
			}
		}
}


	
public String getRequestURL(){
		
		return requestURL;
	}
	
public String getMethod(){
		
		return method;
	}
public String getProcol(){
		
		return protocol;
	}
public String getHeader(String header){
	
	return headerMap.get(header);
}


// 设置 URL
public void setRequestURL(String requestURL) {
	this.requestURL = requestURL;
}

public RequestDispatcher getRequestDispatcher(String webPath) {
	return new RequestDispatcher(webPath);
}

}
