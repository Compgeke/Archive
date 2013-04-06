package snake;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class DrawArea extends Canvas{
	private static final long serialVersionUID = 1L;
	
	public void createBuffer(){
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