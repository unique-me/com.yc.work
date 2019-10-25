package Bank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * 银行存取款业务
 * cs架构的程序
 * @author GSH
 *
 */
public class Server {
public static void main(String[] args) throws IOException {
	
	new Server().start();
}
private BankDAO dao=new BankDAO();
private DataOutputStream dos;
private void start() throws IOException {
	//创建套接字服务器
		ServerSocket server=new ServerSocket(8888);//1025-65535
		//启动服务器
		System.out.println("服务器启动完成监听端口：8888");
		
		boolean running =true;
		while(running){
			//当前线程进入阻塞状态(相当于等待)
			Socket client =server.accept();
			//创建线程处理业务
			new Thread(){
				
				public void run(){
					//获取网络地址对象
					InetAddress addr=client.getInetAddress();
					
					System.out.println("客户端的主机地址："+addr.getHostAddress());
					System.out.println("客户端Ip地址： "+Arrays.toString(addr.getAddress()));//返回的是数组形式tostring接收
					try{
						InputStream in=client.getInputStream();
						OutputStream out=client.getOutputStream();
						dos=new DataOutputStream(out);
						boolean running=true;
						while(running){
							
							/**
							 * 业务约定==》协议
							 * 如果客户发端发送一个命令：dipsoit,接受该命令所需要的参数
							 */
							DataInputStream dis=new DataInputStream(in);
							try{
								
								String command=dis.readUTF();
								
								switch(command){
								case "register":
								case "diposit":
									diposit(dis.readUTF(),dis.readFloat());
									 break;
								case "withdraw":
								case "transfer":
								}
								
//								System.out.println(command);
//								String cardno=dis.readUTF();
//								System.out.println(cardno);
//								float money=dis.readFloat();
//								System.out.println(money);
							}catch(EOFException e){
								break;
							}
							
						}
						
					}catch(IOException e){}
				}
				
			}.start();
			}
		}
//开户
public void register(String cardno,String passward){
	
}

//存款
public void diposit(String cardno,float money) throws IOException{
	//System.out.println(cardno+" "+money);
	dao.update(cardno, money);
	dos.writeUTF("存款成功！");
	dos.flush();
}
//取款
public void withdraw(String cardno,float money){
	
}
//转账
public void transfer(String cardno1,String card2,float money){
	
}
}
