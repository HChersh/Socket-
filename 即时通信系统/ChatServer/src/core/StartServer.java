package core;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class StartServer {

	public static void main(String[] args) {
		 //static修饰
		 ArrayList<User> userlist = new ArrayList<User>();              //服务器上用来储存用户列表的集合
		 User aa = new User("aa","aa");User bb = new User("bb","bb");User cc = new User("cc","cc");User dd = new User("dd","dd");
		 
		 userlist.add(aa);userlist.add(bb);userlist.add(cc);userlist.add(dd);
		 
		 try {
			 ServerSocket ss = new ServerSocket(9999);
			 System.out.println("服务器已启动...");
			 while(true){
				 Socket s = ss.accept();
				 UserThread user = new UserThread(s,userlist);
				 System.out.println("有一个新的客户端建立了连接");
				 user.start();
			 }
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 
	
	}

}
