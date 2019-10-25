package com.yc.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLDemo {
public static void main(String[] args) throws IOException {
	//URL:统一资源定位器
	//格式：协议：//主机地址：端口/资源路径？请求参数
	//Http:   http://192.168.1.12.:8080/demo/page/index.jsp
	
	//构建URL
	URL url=new URL("http://www.hyycinfo.com");
	
	//资源文件名
	System.out.println("url.getFile() ="+url.getFile());
	//主机地址
	System.out.println("url.getHost() ="+url.getHost());
	//协议
	System.out.println("url.getProtocol ="+url.getProtocol());
	//端口
	System.out.println("url.getPort ="+url.getPort());
	//查询参数
	System.out.println("url.getQuery() ="+url.getQuery());
	
	//连接目标资源
	URLConnection conn=url.openConnection();
	
	//获取连接的输入流
     InputStream in=conn.getInputStream();
     
     //定义一个字符流 读取字节流（理解为字节流转为字符流）
     InputStreamReader isr=new InputStreamReader(in,"utf-8");
     
     //定义字节数组，作为缓冲
    // byte []buffer=new byte[1024];
     
     //字符数组
     char[] buffer=new char[1024];
     
     //定义读取的字节数
     int count;
     
    // while((  count=in.read(buffer))>0){//字节输出会有乱码
     
     while((  count=isr.read(buffer))>0){//字符输出解决乱码
    	 //该网址里的内容为文本文件，转换为字符串输出（内容为图片则不同）
    	 String string=new String(buffer,0,count);
    	 System.out.println(string);
     }
	
	
}
}
