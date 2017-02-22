package http;

//�����������������������Ӻ��͸�������������
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
       //���ӷ�����	   
	   System.out.println("����������Ҫ���ʵ��ļ�");
	   String filename = new Scanner(System.in).next();
	   Socket s = new Socket("127.0.0.1",8888);
	   PrintStream writer = new PrintStream(s.getOutputStream());
	   //��������ͷ
//	   String filename = "aaa.html";
	   writer.println(" GET /D:/" + filename + " HTTP/1.1"
					+ "Accept: text/html, application/xhtml+xml, image/jxr, */*"
					+ " Accept-Language: zh-CN"
					+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586"
					+ "Accept-Encoding: gzip, deflate"
					+ "Host: localhost:8888"
					+ "Connection: Keep-Alive");
	   writer.flush();
	   
	   //���������壨GET ��ʽ�У��������ݹ��� URL ��POST ��ʽ�У��������ݷ������� ���У�
	  
	   //������Ӧ��-���ұ����ļ������ش�����
	   DataInputStream reader = new DataInputStream(s.getInputStream());
	   FileOutputStream fileout = new FileOutputStream(new File("D:/socket/http/httpclient/"+filename));
	   byte[] buf = new byte[1024];
	   int length = 0 ; 
       System.out.println(reader.readLine());
	   while((length = reader.read(buf, 0, buf.length)) >= 0){
		    fileout.write(buf, 0, length);
		    fileout.flush();
		    System.out.println(filename+"���سɹ�");
	   }
	   writer.close();
	   reader.close();
	   fileout.close();
	   s.close();
	   
	   
	   
	   
	   
   }
}
