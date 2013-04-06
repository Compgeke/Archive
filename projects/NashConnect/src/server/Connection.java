package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection {
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	private boolean heartBeatReceived,connected;
	private String userName;
	
	protected Connection(Socket socket) throws IOException{
		connected = true;
		this.socket = socket;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	protected Socket getSocket(){
		return socket;
	}
	
	protected BufferedReader getReader(){
		return br;
	}
	
	protected BufferedWriter getWriter(){
		return bw;
	}
	
	protected void writeSystem(String s) throws IOException{
		bw.write("@SYS" + s);
		bw.newLine();
		bw.flush();
	}
	
	protected void writeUser(String s) throws IOException{
		bw.write("@USR" + s);
		bw.newLine();
		bw.flush();
	}
	
	protected void setUserName(String s){
		userName = s;
	}
	
	protected String getUserName(){
		return userName;
	}
	
	protected void setHeartBeatReceived(boolean b){
		heartBeatReceived = b;
	}
	
	protected boolean heartBeatReceived(){
		return heartBeatReceived;
	}
	
	protected boolean isConnected(){
		return connected;
	}
	
	protected void setConnected(boolean connected){
		this.connected = connected;
	}
	
	protected void close() throws IOException{
		connected = false;
		br.close();
		bw.close();
		socket.close();
	}
}
