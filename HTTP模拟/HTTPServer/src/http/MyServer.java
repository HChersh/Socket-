package http;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

//这是浏览器与服务器建立连接后发送给服务器的请求
//GET /D:/socket/http/httpserver/aaa.html HTTP/1.1
//Accept: text/html, application/xhtml+xml, image/jxr, */*
//Accept-Language: zh-CN
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586
//Accept-Encoding: gzip, deflate
//Host: localhost:8888
//Connection: Keep-Alive

public class MyServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
        ServerSocket ss = new ServerSocket(8888);
        while(true){
            Socket s = ss.accept();
        	byte[] buf = new byte[1024*1024];  
            InputStream in = s.getInputStream();  
            int byteRead = in.read(buf, 0, 1024*1024);  
            String dataString = new String(buf, 0, byteRead);  
            System.out.println(dataString);  
            in.close();  
           	s.close(); 
        }
        	
    }
}





