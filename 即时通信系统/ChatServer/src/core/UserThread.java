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
	
   public UserThread(Socket s,ArrayList<User> userlist){    //���췽��
	   this.s = s;
	   this.userlist = userlist;
   }
   
   public void run(){
	   boolean islogin = false;
	   DataInputStream dis = null;
	   DataOutputStream dos = null;
	   String content;
	  try {                                                        //��ȡ�û��������ж����Ƿ���ȷ����
		 dis = new DataInputStream(s.getInputStream());
		 dos = new DataOutputStream(s.getOutputStream());
		 content = dis.readUTF();
		 System.out.println("�������յ�����:"+content);
         if(content.startsWith("login")){
        	 String[] str = content.split(" ");
        	 String username = str[1];
        	 String password = str[2];
        	 islogin = islogin(userlist,username,password);
        	  if(islogin){        //����Login��ͷ�ҵ�½�ɹ�
        		  System.out.println(username+"��½�ɹ�");
        		  addSocket(username,userlist,s);
                  dos.writeUTF("true");
                  dos.flush();
        	  }else{
        		  System.out.println(username+"��½ʧ��");  
                  dos.writeUTF("false");
                  dos.flush();
              
        	  }
         }
	  } catch (IOException e) {
		e.printStackTrace();
	  }
	  
	   while(islogin){                               //���û���ȷ��½��ʱ���õ������Ӧ�Ĳ���
		    try {
				content = dis.readUTF();
				System.out.println("�������յ�����:"+content);
				
				if(content.startsWith("getuserlist")){       //�����û��б���ͻ���
					String str ="";
					for(int i = 0; i<userlist.size();i++){
						 str += userlist.get(i).username;
						 str += " ";
					}str += "all";
					dos.writeUTF(str);
					dos.flush();
				}
				else if(content.startsWith("sendmassage")){      //���ͻ��˷������ı���Ϣ����ָ�����û�
                   String reciever = content.split(",")[2];
                   String detail = content.split(",")[3];
                   System.out.println(detail);
                   
                   if(!reciever.equals("all")){
                   User tempuser = findUser(reciever,userlist);
                   try{
                	   
                	   Socket temps = tempuser.socket;
                	   DataOutputStream doss = new DataOutputStream(temps.getOutputStream());
                	   doss.writeUTF("txt");                              //�൱�ڷ������˷����ͻ��˽�����Ϣ���͵���Ӧ��
                	   doss.flush();
                	   doss.writeUTF(detail);
                	   doss.flush();
                	   System.out.println("�ͻ��˷�������Ϣ�Ѿ��ڷ������˱�ת��");
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
//					// "regest" + " " + username + " " + password    ע���û����ַ���
//					String username = content.split(" ")[1];
//					String password = content.split(" ")[2];
//					User newUser = new User(username, password);
//					userlist.add(newUser);
//				}
				
				else if(content.startsWith("sendfile")){          //���ͻ��˷������ļ�����ָ�����û�
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
	                	 //System.out.println("�ͻ��˷�������Ϣ�Ѿ��ڷ������˱�ת��");
	                	   RandomAccessFile outfile = new RandomAccessFile(filepath, "r");
	                	   int bytesRead = 0 ; 
	                	   while(bytesRead!=-1){
	                		   bytesRead = outfile.read();
	                		   doss.write(bytesRead);
	                		   doss.flush();
//	                		   System.out.println(bytesRead);
	                	   }System.out.println("���������Ѿ��������ļ���ȡ����ѭ��");
	                	   
	                   }catch(Exception e){
	                	   e.printStackTrace();
	                   }
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
	  
   }
   
   public boolean islogin(ArrayList<User> userlist,String username,String password){  //�����û����������жϸ��û��Ƿ�����ж����½���
	   User tempuser = null;
	   for(int i = 0 ; i < userlist.size() ; i++){
		  tempuser = userlist.get(i);
		  if(tempuser.username.equals(username)&&tempuser.password.equals(password)){
              return true;
          }
	   }
	   return false;
   }
   
   public void addSocket(String username,ArrayList<User> userlist,Socket s){        //ĳ�û�����������������˺�Ϊ���ʼ��Socket
	   for(int i = 0 ; i < userlist.size() ; i ++){
		 if(userlist.get(i).username.equals(username)){
			 userlist.get(i).socket = s;
		 }
	   }
   }
   public User findUser(String username,ArrayList<User> userlist){                  //�����û�������һ������Ķ���
	   for(int i = 0 ; i < userlist.size() ; i++){
		   if(userlist.get(i).username.equals(username)){
			   return userlist.get(i);
		   }
	   }
	  return null;
   }
   
}
