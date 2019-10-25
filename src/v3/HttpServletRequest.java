package v3;

import java.util.HashMap;

public class HttpServletRequest {

	/*	request.getRequestURL();// 请求的地址
		request.getMethod();	// 请求的方法
		request.getProtocol();	// 请求的协议版本编号
		request.getHeader("Host"); // 返回头域的指定的字段值
	*/
	
	private String method;
	private String requestURL;
	private String protocol;
	private HashMap<String,String> headerMap = new HashMap<>();

	public HttpServletRequest(String content) {
		// 解析请求报文
		String[] lines = content.split("\r\n");
		for(int i=0; i<lines.length; i++){
			if(i==0){
				// 解析投行，以空格为间隔符
				String[] topLines = lines[i].split("\\s");
				method = topLines[0];
				requestURL = topLines[1];
				protocol = topLines[2];
			} else {
				String[] headerLines = lines[i].split(":\\s");
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
	
	public String getProtocol(){
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
	
	private Cookie[] cookies;
	
	public Cookie[] getCookies() {

		String cookieStr=headerMap.get("Cookie");
		
		if(cookieStr!=null){
			String [] cookieItems=cookieStr.split(";");
			cookies=new Cookie[cookieItems.length];
			int i=0;
			for(String cookieItem :cookieItems){
				String[] cookieNameValue=cookieItem.split("=");
				String name=cookieNameValue[0];
				String value=cookieNameValue[1];
				cookies[i]=new Cookie(name,value);
				i++;
						
			}
		}
		return cookies;
	}
	
}
