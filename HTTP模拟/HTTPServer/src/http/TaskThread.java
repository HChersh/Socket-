package http;
//GET /D:/socket/http/httpserver/aaa.html HTTP/1.1
//Accept: text/html, application/xhtml+xml, image/jxr, */*
//Accept-Language: zh-CN
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586
//Accept-Encoding: gzip, deflate
//Host: localhost:8888
//Connection: Keep-Alive

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class TaskThread extends Thread {
   private Socket s ;
   String root;
   String requestPath;
   public TaskThread(Socket s,String root){
	   this.s = s; 
	   this.root = root;
	   this.requestPath = null;
   }
   public void run(){
	   try {
		      String filename = null;
		      BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		      DataOutputStream writer = new DataOutputStream(s.getOutputStream());
//		      PrintWriter pw = new PrintWriter(s.getOutputStream());
		    //从客户端发来的请求中获得客户端需要的文件名         GET /D:/aaa.html HTTP/1.1
		    String line = reader.readLine();
		    String filenameset[] = line.split("/");
		    
		    for(int i = 0 ; i < filenameset.length; i++){
		    	if(filenameset[i].endsWith("HTTP")){
		    		filename = filenameset[i].split(" ")[0];
		    		System.out.println("服务器端获得的请求文件名为:"+ filename);
		    	}
		    }
		    
		    //获得服务器上该文件的输入流
		    try{
		    FileInputStream fis = new FileInputStream(root + "/" + filename);
		    System.out.println("服务器端得到的绝对路径为");
		    System.out.println(root+"/"+filename);
		    
//		    writer.writeBytes("HTTP/1.1 200 OK" + "    " + "Content-Type:"+   filename.split("\\.")[1]
//		    		+ "   "+"Content-Length:" + fis.available());
		    //读取文件并将内容输入到socket的输出流中
		    byte[] buf = new byte[1024];
		    int length = 0;
		    
		    while((length = fis.read(buf, 0, buf.length))>=0){
		    	writer.write(buf, 0, length);
		    	writer.flush();
		    }
		    
		    fis.close();
            reader.close();
            writer.close();
//            s.close();
		    }catch(Exception e){
              //向客户端输出响应失败的响应码
              System.out.println("404出错");
              e.printStackTrace();
              writer.writeBytes("404notfund");
              reader.close();
              writer.close();
//              s.close();
              
		    }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   }
}
