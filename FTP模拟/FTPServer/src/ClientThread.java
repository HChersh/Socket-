//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//
//public class ClientThread extends Thread{
//   public static int connection = 0;
//   public Socket client;
//   public String F_DIR;
//   public boolean login = false;
//   private static final String NAME = "H";
//   private static final String PASSWORD = "h";
//   //构造函数
//    public ClientThread(Socket client,String F_DIR){
//	   this.client = client;
//       this.F_DIR = F_DIR;
//       
//   }
//   
//    public void run(){
//   	 try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//			PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
//			writer.println("连接正确了");
//			writer.flush();
//		 } catch (IOException e) {
//			e.printStackTrace();
//		 }
//	    
//  }
//
//}


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

public class ClientThread extends Thread {
	private Socket socketClient;//客户端socket
	private String dir = "";//绝对路径
	private String pdir = "D:";//相对路径
	private Random generator=new Random();//随机数
	private int restint = 0;//偏移数
	public ClientThread(Socket client, String F_DIR){
		this.socketClient = client;
		this.dir = F_DIR;
	}
	public void run(){
		InputStream is=null;
		OutputStream os = null;
		try{
			is=socketClient.getInputStream();
			os=socketClient.getOutputStream();
		}catch(Exception e){
			e.printStackTrace();
		}
		BufferedReader br=new BufferedReader(new InputStreamReader(is,Charset.forName("utf-8")));
		PrintWriter pw = new PrintWriter(os);
		String clientInetAddress=socketClient.getInetAddress().toString();
		System.out.println(clientInetAddress);
		String clientIp=clientInetAddress.substring(1);
//		System.out.println("客户端的IP是："+clientIp);
		pw.println("Connect Sucessfully!");
		pw.flush();
		String command = "";//命令
		String UserName= "NEU";//用户名
		String PassWords="NEU";//密码
		String str="";//命令内容
		boolean b = true;
		boolean loginstuts=false;
		int port_high;
		int port_low;
		Socket tempsocket = null;           
		while(b){
			try{
				command = br.readLine();
				System.out.println("客户端发来的指令："+command);
				if(command==null){
					System.out.println("客户端连接断开");
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
				pw.println("331 服务器要求密码");
				pw.flush();
				loginstuts=false;
				b = false;
			}
			//命令user
			if(command.toUpperCase().startsWith("USER")){   //使用默认语言环境的规则将此 String 中的所有字符都转换为大写且判断字符串的前几行
				
				UserName=command.substring(4).trim();//截取第四位以后的内容，去掉获得内容两端的空格
				//判断用户名是否为空
				if(UserName==null||UserName.equals("")){
					System.out.println("用户名为空");
					pw.println("501 参数错误 用户名不能为空");
					pw.flush();
				}else{
					pw.println("331 需要输入密码，用户名为： " + UserName);
					pw.flush();
				}
				
			}//命令user结束
			//命令 pass
			else if(command.toUpperCase().startsWith("PASS")){
				PassWords = command.substring(4).trim();//截取第四位以后的内容，去掉获得内容两端的空格
				if(null==PassWords||PassWords.equals("")){
					System.out.println("密码为空");
					pw.println("501 参数错误 密码不能为空");
					pw.flush();
				}else{
					//校验密码是否正确，正确允许登陆，错误时阻止登陆
					if(PassWords.equals("NEU")){
						System.out.println("LoginSucessfully");
						loginstuts=true;
						pw.println("230 登陆成功");
						pw.flush();
					}else{
						loginstuts=false;
						System.out.println("密码错误，登陆失败");
						UserName="";
						pw.println("530 登陆失败，未登陆网络");
						pw.flush();
					}
				}
			}//命令pass结束
			// QUIT命令
			else if(command.toUpperCase().startsWith("QUIT")){
				System.out.println("("+UserName+") ("+clientIp+")> "+command);
				b = false;
				pw.println("221 Goodbye");
				pw.flush();
				System.out.println("("+UserName+") ("+clientIp+")> 221 Goodbye");
				try {
					Thread.currentThread();//获取当前线程
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} //end QUIT
			//命令cwd   dir就是传进来的F_DIR 为d:        pdir就是相对路径   
			else if(command.toUpperCase().startsWith("CWD")){
				System.out.println("("+UserName+") ("+clientIp+")> "+command);
				if(loginstuts){
					str = command.substring(3).trim();
					if("".equals(str)){
						//pw.println("250 Broken client detected, missing argument to CWD. /""+pdir+"/" is current directory.");
						pw.println("250 文件行为完成, 没有参数的CWD指令 "+pdir+" 为当前目录");
						pw.flush();
						//System.out.println("("+username+") ("+clientIp+")> 250 Broken client detected, missing argument to CWD. /""+pdir+"/" is current directory.");
					}
					else{
						//判断目录是否存在
						String tmpDir = dir +"\\"+ str;
						File file = new File(tmpDir);
						if(file.exists()){//目录存在
							dir = dir + "\\" + str;
							if("\\".equals(pdir)){
								pdir = pdir + str;
							}
							else{
								pdir = pdir + "\\" + str;
							}
//							System.out.println("用户"+clientIp+"："+username+"执行CWD命令");
							//pw.println("250 CWD successful. /""+pdir+"/" is current directory");
							pw.println("250 CWD successful. "+pdir+" 已设为当前目录");
							pw.flush();
							//System.out.println("("+username+") ("+clientIp+")> 250 CWD successful. /""+pdir+"/" is current directory");
						}
						else{//目录不存在
							//pw.println("550 CWD failed. /""+pdir+"/": directory not found.");
							pw.println("550 CWD failed. "+pdir+": 目录未找到");
							pw.flush();
							//System.out.println("("+username+") ("+clientIp+")> 550 CWD failed. /""+pdir+"/": directory not found.");
						}
					}
				}
				else{
					pw.println("530 未登录网络");
					pw.flush();
					System.out.println("("+UserName+") ("+clientIp+")> "+"530 未登录网络");
				}
			} //end CWD
			//RETR命令   下载文件
			else if(command.toUpperCase().startsWith("RETR")){
				System.out.println("("+UserName+") ("+clientIp+")> "+command);
				if(loginstuts){
					str = command.substring(4).trim();         //客户端指定的需要下载的文件
					if("".equals(str)){
						pw.println("501 参数错误");
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 501 参数错误");
					}
					else {
						try {
							pw.println("150 打开连接");
							pw.flush();
							System.out.println("("+UserName+") ("+clientIp+")> 150 打开连接");
							RandomAccessFile outfile = null;
							OutputStream outsocket = null;
							try {
								//创建从中读取和向其中写入（可选）的随机访问文件流，该文件具有指定名称
								System.out.println(dir+"\\"+str);
								outfile = new RandomAccessFile(dir+"\\"+str,"r");
								System.out.println(dir+"\\"+str);
								outsocket = tempsocket.getOutputStream();
								byte bytebuffer[]= new byte[1024]; 
								int length; 
								try{ 
									while((length = outfile.read(bytebuffer)) != -1){ 
										outsocket.write(bytebuffer, 0, length); 
									} 
									outsocket.close(); 
									outfile.close(); 
									tempsocket.close(); 
								} 
								catch(IOException e){
									e.printStackTrace();
								}
								pw.println("226 结束数据连接");
								pw.flush();
								System.out.println("("+UserName+") ("+clientIp+")> 226 结束数据连接");
							} catch (FileNotFoundException e) { 
								e.printStackTrace();
								pw.println("503 找不到指定的文件");
								pw.flush();
							} catch (IOException e) {
								e.printStackTrace();
							} 
							
						} catch (Exception e){
							pw.println("503 错误指令序列");
							pw.flush();
							System.out.println("("+UserName+") ("+clientIp+")> 503错误指令序列");
							e.printStackTrace();
						}
					}
				}
				else{
					pw.println("530 未登录网络");
					pw.flush();
					System.out.println("("+UserName+") ("+clientIp+")> "+"530 未登录网络");
				}
			}//end RETR
			//rest命令
			else if(command.toUpperCase().startsWith("REST")){
				if(loginstuts){
					str = command.substring(4).trim();
					restint=Integer.parseInt(str);
					pw.println("250 偏移量设置完成，为保证文件传输完整，本服务器不会使用偏移量");
					pw.flush();
				}else{
					pw.println("530 未登录网络");
					pw.flush();
					System.out.println("("+UserName+") ("+clientIp+")> "+"530 未登录网络");
				}
			}
			//REST结束
			//STOR命令
			else if(command.toUpperCase().startsWith("STOR")){ 
				System.out.println("("+UserName+") ("+clientIp+")> "+command);
				if(loginstuts){
					str = command.substring(4).trim();
					if("".equals(str)){
						pw.println("501 Syntax error");
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 501 Syntax error");
					}
					else {
						try {
							pw.println("150 Opening data channel for file transfer."); 
							pw.flush();
							System.out.println("("+UserName+") ("+clientIp+")> 150 Opening data channel for file transfer.");
							RandomAccessFile infile = null;
							InputStream insocket = null;
							try {
								
								insocket = tempsocket.getInputStream(); 
							} catch (FileNotFoundException e) { 
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} 
							 
							int length=0; 
							try{
								FileOutputStream output = new FileOutputStream(new File(dir+"\\"+str));
								while((length = insocket.read() )!= -1){ 
									output.write(length);
									output.flush();
									
								}
								insocket.close(); 
								output.close(); 
								tempsocket.close(); 
								pw.println("上传成功");
							} 
							catch(IOException e){
								e.printStackTrace();
							}
							
//							System.out.println("用户"+clientIp+"："+username+"执行STOR命令");
							pw.println("226 Transfer OK");
							pw.flush();
							System.out.println("("+UserName+") ("+clientIp+")> 226 Transfer OK");
						} catch (Exception e){
							pw.println("503 Bad sequence of commands.");
							pw.flush();
							System.out.println("("+UserName+") ("+clientIp+")> 503 Bad sequence of commands.");
							e.printStackTrace();
						}
					}
				} else {
					pw.println("530 未登录网络");
					pw.flush();
					System.out.println("("+UserName+") ("+clientIp+")> "+"530 未登录网络");
				}
			} //end STOR
			
			//NLST命令
			else if(command.toUpperCase().startsWith("NLST")) { 
				System.out.println("("+UserName+") ("+clientIp+")> "+command);
				if(loginstuts){
					try {
						pw.println("150 Opening data channel for directory list."); 
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 150 Opening data channel for directory list.");
						PrintWriter pwr = null;
						try {
							pwr= new PrintWriter(tempsocket.getOutputStream(),true);
						} catch (IOException e1) {
							e1.printStackTrace();
						} 
						File file = new File(dir); 
						String[] dirstructure = new String[10]; 
						dirstructure= file.list(); 
						for(int i=0;i<dirstructure.length;i++){
							pwr.println(dirstructure[i]); 
						}
						try {
							tempsocket.close();
							pwr.close();
						} catch (IOException e) {
							e.printStackTrace();
						} 
//						System.out.println("用户"+clientIp+"："+username+"执行NLST命令");
						pw.println("226 Transfer OK"); 
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 226 Transfer OK");
					} catch (Exception e){
						pw.println("503 Bad sequence of commands.");
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 503 Bad sequence of commands.");
						e.printStackTrace();
					}
				}else{
					pw.println("530 未登录网络");
					pw.flush();
					System.out.println("("+UserName+") ("+clientIp+")> "+"530 未登录网络");
				}
			} //end NLST
			
			//LIST命令
			else if(command.toUpperCase().startsWith("LIST")) { 
				System.out.println("("+UserName+") ("+clientIp+")> "+command);
				if(loginstuts){
					try{
						System.out.println(tempsocket.isConnected());
						pw.println("150 Opening data channel for directory list."); 
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 150 Opening data channel for directory list.");
						PrintWriter pwr = null;
						try {
							pwr= new PrintWriter(tempsocket.getOutputStream(),true);
						} catch (IOException e) {
							e.printStackTrace();
						} 
					
						getDetailList(pwr, dir);  
					
						try {
							tempsocket.close();
							pwr.close();
						} catch (IOException e) {
							e.printStackTrace();
						} 
//						System.out.println("用户"+clientIp+"："+username+"执行LIST命令");
						pw.println("226 Transfer OK"); 
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 226 Transfer OK");
					} catch (Exception e){
						pw.println("503 Bad sequence of commands.");
						pw.flush();
						System.out.println("("+UserName+") ("+clientIp+")> 503 Bad sequence of commands.");
						e.printStackTrace();
					}
				} else {
					pw.println("530 未登录网络");
					pw.flush();
					System.out.println("("+UserName+") ("+clientIp+")> "+"530 未登录网络");
				}
			} //end LIST
			//命令pasv
			
			else if(command.toUpperCase().startsWith("SIZE")){
			    if(loginstuts){
			    	String name = command.substring(4).trim();
			    	File file = new File(dir+"\\"+name);
			    	System.out.println("file的路径为：" + dir+"\\"+name);
			    	pw.println(file.length());
			    	pw.flush();
			    }
				
			}
			
			else if(command.toUpperCase().startsWith("PASV")){
				if(loginstuts){
					ServerSocket ss = null;
					while( true ){
						//随机获取服务器空闲端口
						port_high = 1 + generator.nextInt(20);//0到20之间的一个数
						port_low = 100 + generator.nextInt(1000);
						try {
							//服务器绑定端口
							ss = new ServerSocket(port_high * 256 + port_low);
							break;
						} catch (IOException e) {
							System.out.println("捕获了一个IO异常，进行下次循环");
							continue;
						}
					}
					InetAddress i = null;
					try {
						i = InetAddress.getLocalHost();                         //返回本地主机
					} catch (UnknownHostException e1) {
						System.out.println("服务器网络信息出错");
						e1.printStackTrace();
					}
//					pw.println("227 Entering Passive Mode ("+i.getHostAddress().replace(".", ",")+","+port_high+","+port_low+")");
					pw.println("228 Entering Passive Mode ("+i.getHostAddress().replace(".", ",")+","+port_high+","+port_low+")");
					pw.flush();
					System.out.println("服务器端发送信息成功");
					System.out.println("("+UserName+") ("+clientIp+")> 227 Entering Passive Mode ("+i.getHostAddress().replace(".", ",")+","+port_high+","+port_low+")");
					try {
						//被动模式下的socket
						tempsocket = ss.accept();
						ss.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				else{               //如果网络没有登陆if(loginstuts)不满足
					pw.println("530 未登录网络");
					pw.flush();
					System.out.println("("+UserName+") ("+clientIp+")> "+"530 未登录网络");
				}			
			}
			
			else{                                        //表示不是输入的指定的指令
				System.out.println("错误的指令");
				pw.println("500 命令错误/不支持的命令");
				pw.flush();
			}
		}
		//用户连接断开
		System.out.println("("+UserName+") ("+clientIp+")> disconnected.");
		//释放所使用的系统资源
		try {
			br.close();
			socketClient.close();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("资源释放失败");
		}

	}
	
	
	public static void getDetailList(PrintWriter pw,String path){              //进行打印目录的函数
		 File dir = new File(path);
		 if(!dir.isDirectory()){
			 pw.println("500 No such file directory./r/n");
		 }
		 File[] files = dir.listFiles();
		 String modifyDate;
		 for(int i = 0 ; i< files.length;i++){
			 modifyDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date(files[i].lastModified()));
			 if(files[i].isDirectory()){
				 pw.println("drwxr-wr-x ftp       ftp          0"+ modifyDate + " " + files[i].getName());
			 }else{
				 pw.println("-rw-r-r--1  ftp      ftp      "
						    + files[i].length() + " " + modifyDate + " "
						    + files[i].getName());
			 }
			 pw.flush();
		 }
		 pw.println("total:" + files.length);
		 
	 }
	
}

