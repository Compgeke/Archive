package client;

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
	
	protected Connection(Socket socket) throws IOException{
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
	
	protected void close() throws IOException{
		br.close();
		bw.close();
		socket.close();
	}
}
