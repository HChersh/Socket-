package core;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;


public class UserThread extends Thread{
	Socket s ;
	ArrayList<User> userlist;
	
   public UserThread(Socket s,ArrayList<User> userlist){    //构造方法
	   this.s = s;
	   this.userlist = userlist;
   }
   
   public void run(){
	   boolean islogin = false;
	   DataInputStream dis = null;
	   DataOutputStream dos = null;
	   String content;
	  try {                                                        //获取用户端请求判断其是否正确连接
		 dis = new DataInputStream(s.getInputStream());
		 dos = new DataOutputStream(s.getOutputStream());
		 content = dis.readUTF();
		 System.out.println("服务器收到请求:"+content);
         if(content.startsWith("login")){
        	 String[] str = content.split(" ");
        	 String username = str[1];
        	 String password = str[2];
        	 islogin = islogin(userlist,username,password);
        	  if(islogin){        //请求Login开头且登陆成功
        		  System.out.println(username+"登陆成功");
        		  addSocket(username,userlist,s);
                  dos.writeUTF("true");
                  dos.flush();
        	  }else{
        		  System.out.println(username+"登陆失败");  
                  dos.writeUTF("false");
                  dos.flush();
              
        	  }
         }
	  } catch (IOException e) {
		e.printStackTrace();
	  }
	  
	   while(islogin){                               //当用户正确登陆的时候获得的请求对应的操作
		    try {
				content = dis.readUTF();
				System.out.println("服务器收到请求:"+content);
				
				if(content.startsWith("getuserlist")){       //发送用户列表给客户端
					String str ="";
					for(int i = 0; i<userlist.size();i++){
						 str += userlist.get(i).username;
						 str += " ";
					}str += "all";
					dos.writeUTF(str);
					dos.flush();
				}
				else if(content.startsWith("sendmassage")){      //将客户端发来的文本消息发给指定的用户
                   String reciever = content.split(",")[2];
                   String detail = content.split(",")[3];
                   System.out.println(detail);
                   
                   if(!reciever.equals("all")){
                   User tempuser = findUser(reciever,userlist);
                   try{
                	   
                	   Socket temps = tempuser.socket;
                	   DataOutputStream doss = new DataOutputStream(temps.getOutputStream());
                	   doss.writeUTF("txt");                              //相当于服务器端发给客户端接收消息类型的响应码
                	   doss.flush();
                	   doss.writeUTF(detail);
                	   doss.flush();
                	   System.out.println("客户端发来的消息已经在服务器端被转发");
                   }catch(Exception e){
                	   e.printStackTrace();
                   }
				   
				  }else{
					  for(int i = 0 ;i < userlist.size()-1;i++){
						User tempUer = findUser(userlist.get(i).username,userlist);
						DataOutputStream doss = new DataOutputStream(tempUer.socket.getOutputStream());
						doss.writeUTF("txt");
	                	doss.flush();
						doss.writeUTF(detail);
	                	doss.flush();
						
					  }
				  }
                }
				
//				else if(content.startsWith("regest")){
//					// "regest" + " " + username + " " + password    注册用户的字符串
//					String username = content.split(" ")[1];
//					String password = content.split(" ")[2];
//					User newUser = new User(username, password);
//					userlist.add(newUser);
//				}
				
				else if(content.startsWith("sendfile")){          //将客户端发来的文件发给指定的用户
					String reciever = content.split(",")[2];
					String filepath = content.split(",")[3];
					System.out.println(filepath);
					String[] files = filepath.split("\\\\");
					String filename = files[files.length-1];
					User tempuser = findUser(reciever,userlist);
					 try{
	                	   Socket temps = tempuser.socket;
	                	   DataOutputStream doss = new DataOutputStream(temps.getOutputStream());
	                	   doss.writeUTF("file");
	                	   doss.flush();
	                	   doss.writeUTF(filename);
	                	   doss.flush();
	                	 //System.out.println("客户端发来的消息已经在服务器端被转发");
	                	   RandomAccessFile outfile = new RandomAccessFile(filepath, "r");
	                	   int bytesRead = 0 ; 
	                	   while(bytesRead!=-1){
	                		   bytesRead = outfile.read();
	                		   doss.write(bytesRead);
	                		   doss.flush();
//	                		   System.out.println(bytesRead);
	                	   }System.out.println("服务器端已经跳出了文件读取发送循环");
	                	   
	                   }catch(Exception e){
	                	   e.printStackTrace();
	                   }
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
	  
   }
   
   public boolean islogin(ArrayList<User> userlist,String username,String password){  //根据用户名和密码判断该用户是否存在判断其登陆情况
	   User tempuser = null;
	   for(int i = 0 ; i < userlist.size() ; i++){
		  tempuser = userlist.get(i);
		  if(tempuser.username.equals(username)&&tempuser.password.equals(password)){
              return true;
          }
	   }
	   return false;
   }
   
   public void addSocket(String username,ArrayList<User> userlist,Socket s){        //某用户与服务器建立连接了后为其初始化Socket
	   for(int i = 0 ; i < userlist.size() ; i ++){
		 if(userlist.get(i).username.equals(username)){
			 userlist.get(i).socket = s;
		 }
	   }
   }
   public User findUser(String username,ArrayList<User> userlist){                  //更具用户名返回一个具体的对象
	   for(int i = 0 ; i < userlist.size() ; i++){
		   if(userlist.get(i).username.equals(username)){
			   return userlist.get(i);
		   }
	   }
	  return null;
   }
   
}
