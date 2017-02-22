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
	  //�ͻ��˺�FTP����������Socket����
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
	      System.out.println("�����û���");
	      Scanner in = new Scanner(System.in);
	      writer.println(in.nextLine());
	      writer.flush();
	      System.out.println(reader.readLine());
	      writer.println(in.nextLine());            //�����������������
	      writer.flush();
	      String success = reader.readLine();
	      System.out.println(success);
	      if(success.substring(0, 2).equals("23")){
	    	  a=false;
	      }
      }
      
      System.out.println("��������������ѡ��");
      Scanner inn = new Scanner(System.in);
      String choose;
      while(b){
    	  System.out.println();
    	  System.out.println("��ѡ����Ҫ���еĲ���");
    	  System.out.println("1.CWD(�ı乤��Ŀ¼)"+"   "+"2.PASV(���뱻��ģʽ)"+"   "+"3.QUIT(�ر��������������)");
    	  System.out.println("4.RETR(�����ļ�)"+"   "+"5.STOR(�ϴ��ļ�)" + "  "+"6.LIST(�г��ļ�Ŀ¼)" + "  "+"7.SIZE(�����ļ���С)");
    	choose = inn.nextLine();
    	
    	if(choose.equals("1")){                                  //cwd�����ļ���
    		String path = "CWD";
    		System.out.println("�������������ĵĹ���Ŀ¼");
    		path+=" ";path+=inn.nextLine();
            System.out.println(path);
    		writer.println(path);
    		writer.flush();
    		System.out.println(reader.readLine());
    	}
    	
    	if(choose.equals("3")){                                   //QUIT�Ͽ�����
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
    		System.out.println("��������Ҫ���ҵ��ļ���");
    		String name = new Scanner(System.in).nextLine();
    		writer.println("SIZE "+ name);
    		writer.flush();
    		System.out.println("���ļ��Ĵ�СΪ:" + reader.readLine());
    	}
    	
    	if(choose.equals("2")){                                  //PASV���뱻��ģʽ
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
    	  System.out.println("��ʱ��ip��˿ں�Ϊ��"+ip+"  "+port1);
    	  datasocket = new Socket(ip,port1);
      	  System.out.println("�����ñ���ģʽ������socket���ӳɹ�");
    	}
    	
    	if(choose.equals("6")){                                  //list
    		writer.println("LIST");
    		writer.flush();
    		System.out.println(reader.readLine());
    		if(datasocket==null){                          //���������Ƿ�ɹ�
    			System.out.println("����ѡ�񱻶�ģʽ,�������ݴ���socket");
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
    	
    	
       if(choose.equals("4")){                               //RETR�ļ�����
    		
            if(datasocket==null){
        	    System.out.println("����ѡ�񱻶�ģʽ,�������ݴ���socket");
            }else{
            	System.out.println("��������Ҫ���ص��ļ�");
        	    @SuppressWarnings("resource")
    			String dl_filename = new Scanner(System.in).nextLine();
        		writer.println("RETR"+" "+dl_filename);
        		writer.flush();
        		System.out.println(reader.readLine());
            	System.out.println("�������ļ�����λ��");
                String dl_flielocation = new Scanner(System.in).nextLine();
            	BufferedInputStream input = new BufferedInputStream(datasocket.getInputStream());
            	FileOutputStream output = new FileOutputStream(new File(dl_flielocation+"\\"+dl_filename));
            	int bytesRead = 0; 
            	while((bytesRead = input.read()) != -1){      //read������һ�������ֽ�
            	  output.write(bytesRead);
            	  output.flush();
            	}
            	System.out.println("���سɹ���");
            	output.flush();
            	output.close();
            	input.close();
               
            }
       }
       
       
       if(choose.equals("5")){          //STOR(�ϴ��ļ�)
    	   System.out.println("��������Ҫ�ϴ����ļ�");
    	   String ul_filename = new Scanner(System.in).nextLine();
    	   File file = new File(ul_filename);
    	   String filename = file.getName();
    	   writer.println("STOR"+" " + filename);
    	   writer.flush();
    	   System.out.println(reader.readLine());
    	   
    	   BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
    	   
    	   if(datasocket==null){
       	    System.out.println("����ѡ�񱻶�ģʽ,�������ݴ���socket");
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
