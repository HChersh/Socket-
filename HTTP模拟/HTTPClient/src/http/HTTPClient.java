package http;

//这是浏览器与服务器建立连接后发送给服务器的请求
//GET /D:/socket/http/httpserver/aaa.html HTTP/1.1
//Accept: text/html, application/xhtml+xml, image/jxr, */*
//Accept-Language: zh-CN
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586
//Accept-Encoding: gzip, deflate
//Host: localhost:8888
//Connection: Keep-Alive

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class HTTPClient {
	
   public static void main(String[] args) throws UnknownHostException, IOException {
       //连接服务器	   
	   System.out.println("请输入你想要访问的文件");
	   String filename = new Scanner(System.in).next();
	   Socket s = new Socket("127.0.0.1",8888);
	   PrintStream writer = new PrintStream(s.getOutputStream());
	   //发送请求头
//	   String filename = "aaa.html";
	   writer.println(" GET /D:/" + filename + " HTTP/1.1"
					+ "Accept: text/html, application/xhtml+xml, image/jxr, */*"
					+ " Accept-Language: zh-CN"
					+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586"
					+ "Accept-Encoding: gzip, deflate"
					+ "Host: localhost:8888"
					+ "Connection: Keep-Alive");
	   writer.flush();
	   
	   //发送请求体（GET 方式中，请求数据挂在 URL 后，POST 方式中，请求数据放在请求 体中）
	  
	   //接收响应码-并且保存文件到本地磁盘上
	   DataInputStream reader = new DataInputStream(s.getInputStream());
	   FileOutputStream fileout = new FileOutputStream(new File("D:/socket/http/httpclient/"+filename));
	   byte[] buf = new byte[1024];
	   int length = 0 ; 
       System.out.println(reader.readLine());
	   while((length = reader.read(buf, 0, buf.length)) >= 0){
		    fileout.write(buf, 0, length);
		    fileout.flush();
		    System.out.println(filename+"下载成功");
	   }
	   writer.close();
	   reader.close();
	   fileout.close();
	   s.close();
	   
	   
	   
	   
	   
   }
}
