package http;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class HTTPServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try {
               String WEB_ROOT = "D:/socket/http/httpserver"; 	
			   ServerSocket ss = new ServerSocket(8888);
		       while(true){
		    	   Socket s = ss.accept();
		    	   TaskThread tt = new TaskThread(s,WEB_ROOT);
		    	   tt.start();
		       }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

}
