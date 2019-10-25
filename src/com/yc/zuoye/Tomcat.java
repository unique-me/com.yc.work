package com.yc.zuoye;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import com.yc.net.http.v1.Processer;



public class Tomcat {
	
	public static void main(String[] args) throws IOException {
		Tomcat tomcat=new Tomcat();
		tomcat.startup();
	}

	//启动命令
	public void startup() throws IOException{
		//创建套接字服务器
	ServerSocket server=new ServerSocket(8080);
		
	boolean running=true;
	while(running){
	//当前线程进入阻塞状态(相当于等待)
	Socket socket=server.accept();
	//创建线程处理业务		
    new Thread(){
	   public void run(){
		   //调用Processer对象的process方法
	  new Processer().process(socket);
			}
			}.start();
		}
			
			
			server.close();
			
	}
	//关闭服务器
	public void shutdown(){
		
	}
}
