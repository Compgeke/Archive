package pong;

import javax.swing.JApplet;

public class Pong extends JApplet{
	private static final long serialVersionUID = 1L;
	protected static final int WIDTH = 640;//640
	protected static final int HEIGHT = 360;//360

	public void init(){
		Engine e = new Engine();
		add(e.getCanvas());
		e.start();
	}
}
