package http;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class SwingClient {

	private JFrame frame;
	private JTextField iptextField;
	private JTextField porttextField;
	private JTextField filenametextField;
	private JTextField filepathtextField;
	Socket s;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
					SwingClient window = new SwingClient();
					window.frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public SwingClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String content;
		frame = new JFrame();
		frame.setBounds(100, 100, 502, 462);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel iplabel = new JLabel("ip\u5730\u5740\uFF1A");
		iplabel.setFont(new Font("宋体", Font.PLAIN, 21));
		iplabel.setBounds(27, 32, 102, 25);
		frame.getContentPane().add(iplabel);
		
		JLabel portlabel = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
		portlabel.setFont(new Font("宋体", Font.PLAIN, 21));
		portlabel.setBounds(27, 70, 86, 25);
		frame.getContentPane().add(portlabel);
		
		iptextField = new JTextField("127.0.0.1");
		iptextField.setBounds(141, 35, 141, 24);
		frame.getContentPane().add(iptextField);
		iptextField.setColumns(10);
		
		porttextField = new JTextField("8888");
		porttextField.setBounds(143, 73, 141, 24);
		frame.getContentPane().add(porttextField);
		porttextField.setColumns(10);
		
		final JTextArea textArea = new JTextArea();
		textArea.setBounds(31, 236, 414, 166);
		frame.getContentPane().add(textArea);
		textArea.append("：");
		
		JButton connection = new JButton("\u94FE\u63A5");
		connection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String ip = iptextField.getText();
					int port = Integer.parseInt(porttextField.getText());
					s = new Socket(ip,port);
					textArea.append("链接成功~");
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				     textArea.append("链接失败~");
					e1.printStackTrace();
				}
			}
		});
		connection.setFont(new Font("宋体", Font.PLAIN, 21));
		connection.setBounds(332, 69, 113, 27);
		frame.getContentPane().add(connection);
		
		JLabel lblFilename = new JLabel("\u6587\u4EF6\u540D\u5B57\uFF1A");
		lblFilename.setFont(new Font("宋体", Font.PLAIN, 21));
		lblFilename.setBounds(24, 136, 115, 25);
		frame.getContentPane().add(lblFilename);
		
		JLabel label_1 = new JLabel("\u4E0B\u8F7D\u5730\u5740:");
		label_1.setFont(new Font("宋体", Font.PLAIN, 21));
		label_1.setBounds(24, 174, 105, 25);
		frame.getContentPane().add(label_1);
		
		filenametextField = new JTextField("aaa.html");
		filenametextField.setColumns(10);
		filenametextField.setBounds(141, 142, 141, 24);
		frame.getContentPane().add(filenametextField);
		
		filepathtextField = new JTextField("D:/socket/http/httpclient");
		filepathtextField.setColumns(10);
		filepathtextField.setBounds(141, 177, 141, 24);
		frame.getContentPane().add(filepathtextField);
		
		JButton download = new JButton("\u4E0B\u8F7D");
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 String filename = filenametextField.getText();
				 try {
						PrintStream writer = new PrintStream(s.getOutputStream());
						 writer.println(" GET /D:/" + filename + " HTTP/1.1"
									+ "Accept: text/html, application/xhtml+xml, image/jxr, */*"
									+ " Accept-Language: zh-CN"
									+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586"
									+ "Accept-Encoding: gzip, deflate"
									+ "Host: localhost:8888"
									+ "Connection: Keep-Alive");
					   writer.flush();
					   DataInputStream reader = new DataInputStream(s.getInputStream());
					   FileOutputStream fileout = new FileOutputStream(new File(filepathtextField.getText()+"/"+filename));
					   byte[] buf = new byte[1024];
					   int length = 0 ; 
				       textArea.append(reader.readLine());
					   while((length = reader.read(buf, 0, buf.length)) >= 0){
						    fileout.write(buf, 0, length);
						    fileout.flush();
						    textArea.append("   ");
						    textArea.append("下载成功！"+"请到"+filepathtextField.getText()+"/"+filename+"查看");
					   }
					   writer.close();
					   reader.close();
					   fileout.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					textArea.append("           ");
					textArea.append("下载失败！");
					e1.printStackTrace();
				}
			}
		});
		download.setFont(new Font("宋体", Font.PLAIN, 21));
		download.setBounds(332, 176, 113, 27);
		frame.getContentPane().add(download);
		
	}

}
