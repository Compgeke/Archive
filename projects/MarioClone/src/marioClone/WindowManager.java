/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: WindowManager
 * Description: Creates the window that the will display the game
 */

package marioClone;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class WindowManager extends JFrame{
	private static final long serialVersionUID = 1L;
	protected static final int WIDTH = 640;//640
	protected static final int HEIGHT = 360;//360
	
	
	//CONSTRUCTOR
	public WindowManager(){
		super("WanaB Mario");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH,HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new FlowLayout(0,0,0));
		createBufferStrategy(2);
	}
	
	//Returns the frame as a graphics object
	public Graphics2D getGraphics(){
		try {
			return (Graphics2D)this.getBufferStrategy().getDrawGraphics();
		} catch (Exception ex1) {
			return null;
		}
	}
	
	//updates the window
	public void update(){
		BufferStrategy bs = getBufferStrategy();
		if(!bs.contentsLost()){
			bs.show();
		}
	}
}