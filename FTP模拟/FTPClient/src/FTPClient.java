import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class FTPClient{
     
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws UnknownHostException,IOException{
	  //客户端和FTP服务器建立Socket链接
//	  Scanner in = new Scanner(System.in);
//	  String readline;
		String ip = null;
		int port1 = 0;
      String response;
      boolean a = true;
      boolean b = true;
      Socket socket = new Socket("127.0.0.1",21);
      Socket datasocket = null;
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      System.out.println(reader.readLine());
      
      while(a){
	      System.out.println("输入用户名");
	      Scanner in = new Scanner(System.in);
	      writer.println(in.nextLine());
	      writer.flush();
	      System.out.println(reader.readLine());
	      writer.println(in.nextLine());            //将密码输入服务器端
	      writer.flush();
	      String success = reader.readLine();
	      System.out.println(success);
	      if(success.substring(0, 2).equals("23")){
	    	  a=false;
	      }
      }
      
      System.out.println("进入其他操作的选择");
      Scanner inn = new Scanner(System.in);
      String choose;
      while(b){
    	  System.out.println();
    	  System.out.println("请选择你要进行的操作");
    	  System.out.println("1.CWD(改变工作目录)"+"   "+"2.PASV(进入被动模式)"+"   "+"3.QUIT(关闭与服务器的连接)");
    	  System.out.println("4.RETR(下载文件)"+"   "+"5.STOR(上传文件)" + "  "+"6.LIST(列出文件目录)" + "  "+"7.SIZE(返回文件大小)");
    	choose = inn.nextLine();
    	
    	if(choose.equals("1")){                                  //cwd更改文件夹
    		String path = "CWD";
    		System.out.println("请输入你所更改的工作目录");
    		path+=" ";path+=inn.nextLine();
            System.out.println(path);
    		writer.println(path);
    		writer.flush();
    		System.out.println(reader.readLine());
    	}
    	
    	if(choose.equals("3")){                                   //QUIT断开连接
    		try{
    		writer.println("QUIT");
    		writer.flush();
    		System.out.println(reader.readLine());
    		}finally{
    			socket = null;
    			b=false;
    		}
    	}
    	
    	if(choose.equals("7")){
    		System.out.println("请输入需要查找的文件名");
    		String name = new Scanner(System.in).nextLine();
    		writer.println("SIZE "+ name);
    		writer.flush();
    		System.out.println("该文件的大小为:" + reader.readLine());
    	}
    	
    	if(choose.equals("2")){                                  //PASV进入被动模式
    		writer.println("PASV");
    		writer.flush();
    		String pasvstring = reader.readLine();
    		System.out.println(pasvstring);
    		int opening = pasvstring.indexOf("(");
    	    int closing = pasvstring.indexOf(")",opening + 1);
    	    if(closing>0){
    	    	String dataLink = pasvstring.substring(opening+1,closing);
    	    	StringTokenizer tokenizer = new StringTokenizer(dataLink,",");
    	    	try{
    	    		ip = tokenizer.nextToken()+"."+tokenizer.nextToken()+"."+tokenizer.nextToken()+"."+tokenizer.nextToken();
    	    		port1 = Integer.parseInt(tokenizer.nextToken())*256 + Integer.parseInt(tokenizer.nextToken());
    	    	}catch(Exception e){
    	    		throw new IOException("SimpleFTP received bad data link information:"+pasvstring);
    	    	}
    	    }
    	  System.out.println("此时的ip与端口号为："+ip+"  "+port1);
    	  datasocket = new Socket(ip,port1);
      	  System.out.println("已启用被动模式，数据socket链接成功");
    	}
    	
    	if(choose.equals("6")){                                  //list
    		writer.println("LIST");
    		writer.flush();
    		System.out.println(reader.readLine());
    		if(datasocket==null){                          //数据链接是否成功
    			System.out.println("请先选择被动模式,建立数据传输socket");
    		}else{
    		      DataInputStream dis = new DataInputStream(datasocket.getInputStream());
    		      String s = "";
    		      while(( s = dis.readLine()) != null){
    		    	  String l = new String(s.getBytes("ISO-8859-1"),"utf-8");
    		    	  System.out.println(l);
    		      }
    		      dis.close();datasocket.close();
    		}
    	}
    	
    	
       if(choose.equals("4")){                               //RETR文件下载
    		
            if(datasocket==null){
        	    System.out.println("请先选择被动模式,建立数据传输socket");
            }else{
            	System.out.println("请输入你要下载的文件");
        	    @SuppressWarnings("resource")
    			String dl_filename = new Scanner(System.in).nextLine();
        		writer.println("RETR"+" "+dl_filename);
        		writer.flush();
        		System.out.println(reader.readLine());
            	System.out.println("请输入文件下载位置");
                String dl_flielocation = new Scanner(System.in).nextLine();
            	BufferedInputStream input = new BufferedInputStream(datasocket.getInputStream());
            	FileOutputStream output = new FileOutputStream(new File(dl_flielocation+"\\"+dl_filename));
            	int bytesRead = 0; 
            	while((bytesRead = input.read()) != -1){      //read返回下一个数据字节
            	  output.write(bytesRead);
            	  output.flush();
            	}
            	System.out.println("下载成功！");
            	output.flush();
            	output.close();
            	input.close();
               
            }
       }
       
       
       if(choose.equals("5")){          //STOR(上传文件)
    	   System.out.println("请输入你要上传的文件");
    	   String ul_filename = new Scanner(System.in).nextLine();
    	   File file = new File(ul_filename);
    	   String filename = file.getName();
    	   writer.println("STOR"+" " + filename);
    	   writer.flush();
    	   System.out.println(reader.readLine());
    	   
    	   BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
    	   
    	   if(datasocket==null){
       	    System.out.println("请先选择被动模式,建立数据传输socket");
    	   }else if(file.isDirectory()){
    		   throw new IOException("SimpleFTP cannot upload a directory.");
    	   }else{ 
    		 BufferedOutputStream output = new BufferedOutputStream(datasocket.getOutputStream());
    		 int bytesRead = 0; 
    		 while((bytesRead = input.read())!=-1){
    			 output.write(bytesRead);
    			 output.flush();
    		 }
    		 output.flush();
    		 output.close();
    		 
    		 input.close();
    		 System.out.println(reader.readLine());

    	   }
       }
       
    	
    	  
      }
      
    }
	
	

}
