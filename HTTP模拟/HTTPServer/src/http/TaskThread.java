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
		    //�ӿͻ��˷����������л�ÿͻ�����Ҫ���ļ���         GET /D:/aaa.html HTTP/1.1
		    String line = reader.readLine();
		    String filenameset[] = line.split("/");
		    
		    for(int i = 0 ; i < filenameset.length; i++){
		    	if(filenameset[i].endsWith("HTTP")){
		    		filename = filenameset[i].split(" ")[0];
		    		System.out.println("�������˻�õ������ļ���Ϊ:"+ filename);
		    	}
		    }
		    
		    //��÷������ϸ��ļ���������
		    try{
		    FileInputStream fis = new FileInputStream(root + "/" + filename);
		    System.out.println("�������˵õ��ľ���·��Ϊ");
		    System.out.println(root+"/"+filename);
		    
//		    writer.writeBytes("HTTP/1.1 200 OK" + "    " + "Content-Type:"+   filename.split("\\.")[1]
//		    		+ "   "+"Content-Length:" + fis.available());
		    //��ȡ�ļ������������뵽socket���������
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
              //��ͻ��������Ӧʧ�ܵ���Ӧ��
              System.out.println("404����");
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
