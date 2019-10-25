package v3;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map.Entry;



public class HttpServletResponse {
	
	// web.xml 解析器
	private static WebXmlParser webXmlParser = new WebXmlParser("web.xml");
	
	private HttpServletRequest request;
	private OutputStream out;

	//  状态码字段
	private int status=200;
	//  状态码的描述信息
	private String message="OK";
	//HashMap存放一条头域信息
	private HashMap<String,String> headerMap = new HashMap<>();
	
	public HttpServletResponse(HttpServletRequest request, OutputStream out) {
		super();
		this.request = request;
		this.out = out;
	}


	/*
	 * 	response.setContentType("?????");  // 设置响应类型
		response.setStatus(404,""); // 设置结果码
		response.setHeader("键","值")
	*/	
	// 提交方法
	public void commit() throws IOException{
		
		String suffix = request.getRequestURL().substring(//例如：hello.s最后一位是.但后面还有一位所以+1
			          request.getRequestURL().lastIndexOf(".")+1);
		
		// 从 web.xml 文件中取 contentType， 替代之前的硬编码判断
		//判断是否有设置content-Type
		if(headerMap.containsKey("Content-Type")==false){
		
		String contentType = webXmlParser.getContentType(suffix);
		// 设置 响应类型
		setContentType(contentType);
		}
		String resp = "HTTP/1.1 "+status+" "+message+"\r\n";
		//resp += "Content-Type: "+contentType+"\r\n";
		// 写头域信息
		for(Entry<String, String> entry : headerMap.entrySet()){
			resp += entry.getKey() + ": " + entry.getValue() + "\r\n";
		}
		//写入cookie数据（cookie存在头域里） List<Cookie>cookies=new ArrayList<Cookie>();用for循环输出
				for(Cookie cookie:cookies){
					resp +=cookie + "\r\n";
				}
		
		resp += "\r\n";
		out.write(resp.getBytes());
		
		// 响应重定向不需要写 body 
		if(status < 300 || status > 399){
			
			if(caw.toString().isEmpty()){
				String rootPath = "/photo";
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
			}else{
				out.write(caw.toString().getBytes());
			}
			}
		
		
	}


	public void setStatus(int status, String message) {
		this.status = status;
		this.message = message;
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

	
	/**
	 * 响应输出字符流
	 * 如何定义   PrintWriter ，在commit 要考虑  和 文件输出的配合问题
	 * @return
	 */
	   CharArrayWriter caw=new CharArrayWriter();//字符流
	   PrintWriter pw=new PrintWriter(caw);
	      
	//public class PrintWriter extends Writer将对象的格式表示打印到文本输出流
	public PrintWriter getWriter() {
 
		return pw;
	}
	
	//List集合存放多条cookie
	private List<Cookie>cookies=new ArrayList<Cookie>();
	
	public  void addCookie(Cookie cookie){
		cookies.add(cookie);
	}


}