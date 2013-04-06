package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class ClientStart extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int PORT = 1;
	private String SERVER_IP = getIpFromUser();//getServerIP();
	
	private static URL url;
	
	private Socket s;
	private Connection cc;
	private JTextArea messages,userList;
	private JTextField out;
	private Container container;
	private String nickName;
	
	public static void main(String[] args){
		new ClientStart().init();
	}
	
	public void init() {
		container = getContentPane();
		buildAndShowUI();
	}
	
	private static String getIpFromUser(){
		return JOptionPane.showInputDialog("Please enter the server IP");
	}

	private static String getServerIP() {
		try {
			url = new URL("http://www.spcss.ca/spss01/TheNash/server.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String result = br.readLine();
			br.close();
			url = null;
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void buildAndShowUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			 	public void run() {
			 		messages = new JTextArea();
			 		messages.setEditable(false);
			 		messages.setLineWrap(true);
			 		messages.setWrapStyleWord(true);
			 		messages.setFocusable(false);
			 		JScrollPane jsp = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			 				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			 		jsp.setAutoscrolls(true);			 		
			 		jsp.setPreferredSize(new Dimension(400,400));
			 		
			 		userList = new JTextArea();
			 		userList.setEditable(false);
			 		userList.setLineWrap(true);
			 		userList.setWrapStyleWord(true);
			 		userList.setFocusable(false);
			 		JScrollPane jsp2 = new JScrollPane(userList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			 				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			 		jsp2.setAutoscrolls(true);			 		
			 		jsp2.setPreferredSize(new Dimension(100,400));
			 		
			 		out = new JTextField();
			 		out.setPreferredSize(new Dimension(500,20));
			 		
			 		container.add(jsp, BorderLayout.WEST);
			 		container.add(jsp2,BorderLayout.EAST);
			 		container.add(out, BorderLayout.SOUTH);

					nickName = JOptionPane.showInputDialog("Please enter a nick name");
					if(nickName == null || nickName.equals("") || nickName.equals(" ")){
						nickName = "Guest";
					}
					if(nickName.length() > 12){
						nickName = nickName.substring(0, 12);
					}
			 		messages.setText("Welcome to the Nash school Messaging service " + nickName + "!");
			 		if(SERVER_IP.equals(null)){
			 			messages.append("\nCould not find server ip, please enter it manually by typing \"//setServerIP\"");
			 		}

					Listener l = new Listener();
					out.addKeyListener(l);
					l.connect();
					setBounds(200, 200, 510, 530);
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					setResizable(false);
					setVisible(true);
			 	}
			}
		);
	}
	
	private class Listener extends KeyAdapter {
		public void keyPressed(KeyEvent ev) {
			int key = ev.getKeyCode();
			if(key == KeyEvent.VK_ENTER){
				if(out.getText().equals("//connect")){
					out.setText("");
					connect();
				}else if(out.getText().equals("//setServerIP")){
					SERVER_IP = JOptionPane.showInputDialog("Please enter a valid IP");
				}else{
					try {
						if(out.getText() != null && !out.getText().equals("")){
							cc.writeUser(out.getText());
							out.setText("");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private synchronized void connect(){
			if(SERVER_IP != null){
				try {
					messages.append("\nAttempting to Connect!");
					s = new Socket(SERVER_IP, PORT);
				} catch (UnknownHostException e) {
					messages.append("\nError: Server unreachable.");
				} catch (IOException e) {
					messages.append("\nError: Timed out.");
				}finally{
					try {
						cc = new Connection(s);
						cc.writeSystem("setUser" + nickName);
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						startListener();							
					}				
				}
			}else{					
				messages.append("\nNo server IP set");
			}
		}
		
		private synchronized void startListener(){
			new Thread(new Runnable(){
				public void run(){
					String temp;
					try {
						while((temp = cc.getReader().readLine()) != null){
							try {
								if(temp.startsWith("@SYS")){
									if(temp.substring(4).equals("marco")){
										cc.writeSystem("polo");										
									}else if(temp.substring(4).startsWith("setUsers")){
										final String temp2[] = temp.substring(12).split(",");
										new Thread(new Runnable(){
											public void run(){
												addMessage("Connected!");
												userList.setText("Users:\n");
												for(int i = 0; i < temp2.length; i++){
													addUser(temp2[i]);
												}
												
											}
										}).start();
									}else if(temp.substring(4).startsWith("addUser")){
										if(!temp.substring(11).equals(nickName)){
											addUser(temp.substring(11));
											addMessage(temp.substring(11) + " connected!");											
										}
									}else if(temp.substring(4).startsWith("removeUser")){
										final String temp2 = temp.substring(14);
										addMessage(temp2 + " disconnected!");
										final String temp3[] = userList.getText().split("\n");
										new Thread(new Runnable(){
											public void run(){
												String finalUserList = "";
												for(int i = 0; i < temp3.length; i++){
													if(!temp3[i].equals(temp2)){
														finalUserList += temp3[i] + "\n";
													}
												}
												userList.setText(finalUserList);
												
											}
										}).start();
									}
								}else if(temp.startsWith("@USR")){
									addMessage(temp.substring(4));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		private void addMessage(String s){
			messages.append("\n" + s);
			scrollToBottom(messages);
		}
		
		private void addUser(String s){
			userList.append(s + "\n");
			scrollToBottom(userList);
		}
		
		private void scrollToBottom(JTextArea jta){
			Document d = jta.getDocument();
			jta.select(d.getLength(), d.getLength());
		}
	}
}
