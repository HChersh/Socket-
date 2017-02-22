//import java.net.ServerSocket;
//import java.net.Socket;
//
//
//public class FTPServer {
//    String F_DIR;//已经给定了用户的工作目录,之后可以改变
//	public static void main(String[] args) {
//      try{
//    	  ServerSocket ss = new ServerSocket(21); //这里应该出现一个端口号
////    	  Logger.info("Connectiong to server A...");
////    	  longger.info("Connected Successful! Local Port:"+s.getLocalPort()+".Default Directory:'"+F_DIR+"'.");
//    	  
//    	  while(true){
//    		  System.out.println("等待连接");
//    		  Socket client = ss.accept();
//    		  System.out.println("客户端连入");
////    		  创建服务线程对象并且启动服务消息进程
//    		  new ClientThread(client,"F_DIR").start();
//    		  System.out.println("子线程已启动");
//    	  }
//    		  
//         }catch(Exception e){
//    	 logger.error(e.getMessage());
//    	 for(StackTraceElement ste : e.getStackTrace()){
////    		 logger.error(ste.toString());
//    	 }
//      }
//		
//	}
//
//}

import java.net.ServerSocket;
import java.net.Socket;


public class FTPServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 final String root_diro="D:";
		 final int port = 21;
		 try{
			 ServerSocket s= new ServerSocket(port);
//			 System.out.println("启动serversocket");
			 while(true){
//				 System.out.println("等待客户端连接");
				 Socket client=s.accept();
//				 System.out.println("客户端已连接");
				 new ClientThread(client, root_diro).start();
//				 System.out.println("客户线程已启动");
				 
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	}

}

