package com.yc.net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

//客户端
public class Client {
public static void main(String[] args) throws IOException, InterruptedException {
	//屏幕录入对象
	Scanner scanner=new Scanner(System.in);
	
	
	//创建套接字服务器（服务器端）
	//ServerSocket server=new ServerSocket(8888);//1025-65535
	
	//创建Socket对象提供IP和端口号才进行连接
	Socket server=new Socket("172.20.80.199",8888);
	System.out.println("成功连接服务器！");
	
	//获取网络地址对象
	InetAddress addr=server.getInetAddress();
	
	System.out.println("服务端的主机地址："+addr.getHostAddress());
	System.out.println("服务端ip地址： "+Arrays.toString(addr.getAddress()));
	
	InputStream in=server.getInputStream();
	OutputStream out=server.getOutputStream();
//	out.write("hello 张三！".getBytes());//字符转为字节流
	 
//	byte []buf=new byte[1024];
//	int count=in.read(buf);
//	System.out.println("服务器说:"+new String(buf,0,count));
//	
	
	

	Thread t1=new Thread(){
		public void run(){
			boolean running=true;
			while(running){
				System.out.println("我说：");
				String msg=scanner.nextLine();
				try {
					out.write(msg.getBytes());
					
					//文件：e:/a.txt
					if(msg.startsWith("文件：")){
						String filename=msg.substring("文件：".length());
						sendFile(out,filename);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	t1.start();
	
	
	
	
	
	Thread t2=new Thread(){
		public void run(){
			boolean running=true;
			while(running){
				
				try {
					//接收来自于客户端的消息
					byte []buf=new byte[1024];
					int count=in.read(buf);
					String msg=new String(buf,0,count);
					
					//文件：e:/a.txt
					if(msg.startsWith("文件：")){
						String filename=msg.substring("文件：".length());
					    filename=msg.substring(filename.lastIndexOf("/")+1);
						saveFile(in,filename);
					}else{
					
					System.out.println("服务器说："+msg);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	t2.start();
	
	t1.join();
	t2.join();
	
	scanner.close();
	server.close();
	
	
}
static void saveFile(InputStream in,String filename) throws IOException{
	FileOutputStream fos=new FileOutputStream("d:/"+filename);
	try{
	byte []buf=new byte[1024];
	int count;
	while((count=in.read(buf))>0){
		fos.write(buf,0,count);
	}
	}finally{
	fos.close();
}
	System.out.println("文件保存成功："+"d:/"+filename);
}

static void sendFile(OutputStream out,String filename) throws IOException{
	FileInputStream fis=new FileInputStream(filename);
	try{
		
		byte[] buf=new byte[1024];
		int count;
		while((count=fis.read(buf))>0){
			out.write(buf,0,count);
		}
		
	}finally{
		fis.close();
	}
	System.out.println("文件发送成功："+"d:/"+filename);
}
}
