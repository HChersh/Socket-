//import java.net.ServerSocket;
//import java.net.Socket;
//
//
//public class FTPServer {
//    String F_DIR;//�Ѿ��������û��Ĺ���Ŀ¼,֮����Ըı�
//	public static void main(String[] args) {
//      try{
//    	  ServerSocket ss = new ServerSocket(21); //����Ӧ�ó���һ���˿ں�
////    	  Logger.info("Connectiong to server A...");
////    	  longger.info("Connected Successful! Local Port:"+s.getLocalPort()+".Default Directory:'"+F_DIR+"'.");
//    	  
//    	  while(true){
//    		  System.out.println("�ȴ�����");
//    		  Socket client = ss.accept();
//    		  System.out.println("�ͻ�������");
////    		  ���������̶߳���������������Ϣ����
//    		  new ClientThread(client,"F_DIR").start();
//    		  System.out.println("���߳�������");
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
//			 System.out.println("����serversocket");
			 while(true){
//				 System.out.println("�ȴ��ͻ�������");
				 Socket client=s.accept();
//				 System.out.println("�ͻ���������");
				 new ClientThread(client, root_diro).start();
//				 System.out.println("�ͻ��߳�������");
				 
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	}

}

