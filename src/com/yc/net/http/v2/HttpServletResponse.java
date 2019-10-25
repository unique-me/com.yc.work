package com.yc.net.http.v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

public class HttpServletResponse {

	//web.xml解析器
	private static WebXmlParser webXmlParser=new WebXmlParser("web.xml");
	private HttpServletRequest request;
	private OutputStream out;
	
	//状态码字段
	private int status=200;
	//状态码的描述信息
	private String message="OK";
	//头域集合
	private HashMap<String,String>headerMap=new HashMap<>();
	
	public HttpServletResponse(HttpServletRequest request,OutputStream out){
		super();
		this.request=request;
		this.out=out;
		
	}
	/**
	 * response.sentContentType（？？？？）;//设置响应类型
	 * response.setStatus(404,"");//设置结果码
	 * response.addHeader("键"，"值");
	 * 
	 */
	
	//提交方法
	public void commit() throws IOException{
		String suffix=request.getRequestURL().substring(
				request.getRequestURL().lastIndexOf(".")+1);
		//从web.xml 文件中取contentype 代替之前的编码
		String contentType = webXmlParser.getContentType(suffix);
		
		//设置响应类型
		setContentType(contentType);
		
		String responseStr="HTTP/1.1 "+status+" "+message+"\r\n";
	//	responseStr+="Content-Type:" +contentType+"\r\n";
	//	responseStr+="Content-Type:text/html\r\n";//是一个页面
		//写头域信息(如：Content-Type:text/html)
		for(Entry<String,String>entry :headerMap.entrySet()){
			responseStr+=entry.getKey()+":"+entry.getValue()+"\r\n";
		}
		responseStr+="\r\n";// CRLF 空行
		out.write(responseStr.getBytes());//输出（返回信息给浏览器）
		
		// 响应重定向不需要写 body 
				if(status < 300 || status > 399){
					String rootPath = "/Tomcat/webapps/photo";
					String filePath = request.getRequestURL();
					// 判断访问文件是否存在			
					String diskPath = rootPath + filePath;
					if(new File(diskPath).exists() == false){
						diskPath = rootPath + "/404.html";
					}
					
					FileInputStream fis = new FileInputStream(diskPath);
					
					byte[] buf = new byte[1024];
					int count;
					// 向浏览器发送报文
					while((count = fis.read(buf))>0){
						out.write(buf, 0, count);
					}
					fis.close();
				}
				
			}

	public void setStatus(int status,String message) {
		
		this.status=status;
		this.message=message;
	}
	/**
	 * 响应重定向
	 * @param webPath
	 */

public void sendRedirect(String webPath) {
		
		/**
		 * 响应结果码： 
		 * 	1xx 接受请求，继续处理
		 * 	2xx	正常响应 200
		 * 	3xx	响应重定向 301 302
		 * 	4xx 浏览器端错误 404  405
		 * 	5xx 服务器端错误
		 */
		this.setStatus(301, "Redirect");
		this.addHeader("Location", webPath);
		
	}
	
public void addHeader(String key, String value){
	this.headerMap.put(key, value);
}

public void setContentType(String contentType){
	this.headerMap.put("Content-Type", contentType);
}



}
