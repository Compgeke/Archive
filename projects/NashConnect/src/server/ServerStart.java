package server;

import java.io.IOException;

public class ServerStart {
	private ConnectionManager cm;
	
	public static void main(String args[]) throws IOException{
		ServerStart s = new ServerStart();
		s.start();
	}
	
	//TODO UI!
	
	public void start() throws IOException{
		cm = new ConnectionManager();
		cm.openConnections();
	}
}
