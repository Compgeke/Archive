package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

public class ConnectionManager {
	protected static final int MAX_CONNECTIONS = 100;
	protected static final int PORT = 1;
	protected static final int HEARTBEAT_TIMEOUT = 3000;
	private Stack<Connection> connections;
	private ServerSocket serverSocket;
	private int guestTally = 1;
	private Stack<String> users;
	
	protected ConnectionManager() throws IOException{
		users = new Stack<String>();
		connections = new Stack<Connection>();
		serverSocket = new ServerSocket(PORT);
	}
	
	protected synchronized void openConnections(){
		new Thread(new Runnable(){
				public void run(){
					try {
						while(true){
							if(connections.size() < MAX_CONNECTIONS){								
								connections.push(new Connection(serverSocket.accept()));
								startListener(connections.peek());
								startHeart(connections.peek());
							}
							Thread.sleep(20);
						}
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		).start();
	}
	
	private synchronized void startListener(final Connection c){
		new Thread(new Runnable(){
				public void run(){
					try {
						String receivedMessage;
						String command;
						while((receivedMessage = c.getReader().readLine()) != null){
							if(receivedMessage.startsWith("@SYS")){
								command = receivedMessage.substring(4);
								if(command.equals("polo")){
									c.setHeartBeatReceived(true);
								}else if(command.startsWith("setUser")){
									String userName = command.substring(7);
									if(userName.equals("Guest")){
										userName += guestTally;
										guestTally++;
									}
									users.push(userName);
									c.setUserName(userName);
									registerIP(c);
									forwardUserList(c);
									forwardUserConnected(userName);
								}
							}else if(receivedMessage.startsWith("@USR")){
								forwardMessage(c.getUserName() + ": " + receivedMessage.substring(4));
							}
						}
						c.setConnected(false);
						c.setHeartBeatReceived(false);
					} catch (Exception ex) {
						c.setConnected(false);
						c.setHeartBeatReceived(false);
					}
				}				
			}
		).start();
	}
	
	private synchronized void forwardMessage(final String message) throws IOException {
		new Thread(new Runnable(){
			public void run(){
				for(int i = 0; i < connections.size(); i++){
					if(connections.get(i).isConnected()){
						try {
							connections.get(i).writeUser(message);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	
	private synchronized void forwardUserConnected(final String user) throws IOException {
		new Thread(new Runnable(){
			public void run(){
				for(int i = 0; i < connections.size(); i++){
					if(connections.get(i).isConnected()){
						try {
							connections.get(i).writeSystem("addUser" + user);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	
	private synchronized void forwardUserDisconnected(final String user) throws IOException {
		new Thread(new Runnable(){
			public void run(){
				for(int i = 0; i < connections.size(); i++){
					if(connections.get(i).isConnected()){
						try {
							connections.get(i).writeSystem("removeUser" + user);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	
	private synchronized void forwardUserList(final Connection c) throws IOException {
		new Thread(new Runnable(){
			public void run(){
				String userList = "";
				for(int i = 0; i < users.size(); i++){
					userList += users.get(i) + (i == users.size()-1?"":",");
				}
				try {
					c.writeSystem("setUsers" + userList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private synchronized void startHeart(final Connection c){
		new Thread(new Runnable(){
				public void run(){
					try {
						do{
							c.setHeartBeatReceived(false);
							c.writeSystem("marco");
							Thread.sleep(HEARTBEAT_TIMEOUT);
						}while(c.heartBeatReceived() && c.isConnected());
						forwardUserDisconnected(c.getUserName());
						users.remove(c.getUserName());
						c.setConnected(false);
						c.close();
						System.out.println(c.getSocket().getInetAddress() + "\t\t" + c.getUserName() + " (Disconnect)");
						connections.remove(c);
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		).start();
	}
	
	private synchronized void registerIP(final Connection c){
		Socket temp = c.getSocket();
		System.out.println(temp.getInetAddress() + "\t\t" + c.getUserName() + " (Connect)");
	}
}
