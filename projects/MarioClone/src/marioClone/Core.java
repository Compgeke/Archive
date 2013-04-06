/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: Core
 * Description: Abstract class that will be used to update the screen
 * 					This is a generic game loop class.
 */

package marioClone;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class Core {
	private boolean running;
	protected WindowManager wm;
	
	//stop the game (user exited/fatal error)
	public void stop(){
		running = false;
	}
	
	public void start(){
		startGame();
	}
	
	//restarting
	public void resume(){
		running = true;
		startGame();
	}
	
	//start the game loop on its own thread
	public void startGame(){
		new Thread(new Runnable(){
			public void run(){
				gameLoop();
			}
		}).start();
	}
	
	//create the window, make it visible
	public void createWindow(){
		running = true;
		wm = new WindowManager();
	}
	
	//main game loop
	public void gameLoop(){
		long startTime = System.currentTimeMillis();
		long cumTime = startTime;
		
		while(running){
			long timePassed = System.currentTimeMillis() - cumTime;
			cumTime += timePassed;
			
			update(timePassed);				
			Graphics2D g = wm.getGraphics();
			if(g != null){
				draw(g);
			}
			g.dispose();
			wm.update();
			try {
				Thread.sleep(20);
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}
	
	//shorter than writing out System.out etc
	public static void debug(String s){
		System.out.println(s);
	}
	
	//error
	public static void showError(String err){
		JScrollPane errorPane = new JScrollPane(new JTextArea(err));
		errorPane.setPreferredSize(new Dimension(300, 150));
		JOptionPane.showMessageDialog(null, errorPane, "Error", -1);
	 }
	

	// This method returns a buffered image with the contents of an image
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }

	    // This code ensures that all the pixels in the image are loaded
	    image = new ImageIcon(image).getImage();

	    // Determine if the image has transparent pixels
	    boolean hasAlpha = true;

	    // Create a buffered image with a format that's compatible with the screen
	    BufferedImage bimage = null;
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    try {
	        // Determine the type of transparency of the new buffered image
	        int transparency = Transparency.OPAQUE;
	        if (hasAlpha) {
	            transparency = Transparency.BITMASK;
	        }

	        // Create the buffered image
	        GraphicsDevice gs = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gs.getDefaultConfiguration();
	        bimage = gc.createCompatibleImage(
	            image.getWidth(null), image.getHeight(null), transparency);
	    } catch (HeadlessException e) {
	        // The system does not have a screen
	    }

	    if (bimage == null) {
	        // Create a buffered image using the default color model
	        int type = BufferedImage.TYPE_INT_RGB;
	        if (hasAlpha) {
	            type = BufferedImage.TYPE_INT_ARGB;
	        }
	        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	    }

	    // Copy image to buffered image
	    Graphics g = bimage.createGraphics();

	    // Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return bimage;
	}
	
	//update animation
	public abstract void update(long timePassed);
	
	//draws to screen
	public abstract void draw(Graphics2D g);
}