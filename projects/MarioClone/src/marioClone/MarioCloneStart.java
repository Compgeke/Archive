/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: MarioCloneStart
 * Description: Main class, controls pausing, cinematics, menus, level selection! (when implemented)
 */

package marioClone;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import marioClone.Engine.DrawImage;

public class MarioCloneStart implements KeyListener{
	public static void main(String args[]) throws IOException{		
		MarioCloneStart mcs = new MarioCloneStart();
		mcs.init();
	}
	
	private Engine e;
	private boolean playVideo,paused;
	private Graphics2D g;
	//TODO convert these to menu screens, not animations!
	private Animation intro,pause;
	
	public void init() throws IOException{
		//build all the maps at run time
		Map maps[] = new Map[4];
		maps[0] = new Map("map1.csv",false);
		maps[1] = new Map("map2.csv",false);
		maps[2] = new Map("test.csv",false);
		maps[3] = new Map("win.csv",false);
		
		//create game engine, add this as a listener, get the graphics for the window
		e = new Engine(maps);
		e.createWindow();
		g = e.wm.getGraphics();	
		e.wm.addKeyListener(this);
		
		//build intro animation
		intro = new Animation(false);
		intro.addScene(ImageIO.read(getClass().getResourceAsStream("/Images/splash.png")),200);
		intro.addScene(ImageIO.read(getClass().getResourceAsStream("/Images/splash2.png")),200);
		
		//build pause screen animation
		pause = new Animation(false);
		pause.addScene(ImageIO.read(getClass().getResourceAsStream("/Images/pause.png")),200);
		pause.addScene(ImageIO.read(getClass().getResourceAsStream("/Images/pause2.png")),200);
		
		//play intro animation
		playVideo(intro);
	}
	
	public void loadMaps(){
		//TODO load all maps in file
		// (More maps, more efficiently)
	}
	
	public synchronized void playVideo(final Animation vid){
		playVideo = true;
		new Thread(new Runnable(){
			public void run(){
				long startTime = System.currentTimeMillis();
				long cumTime = startTime;
				while(playVideo){
					long timePassed = System.currentTimeMillis() - cumTime;
					cumTime += timePassed;
					vid.update(timePassed);
					if(paused){
						e.createScreen();
						Stack<DrawImage> screenImages = e.getScreen();
						for(int i = 0; i < screenImages.size(); i++){
							DrawImage di = screenImages.get(i);
							g.drawImage(di.getImage(),di.getX(),di.getY(),null);
						}
					}
					g.drawImage(vid.getImage(),0,0,null);
					e.wm.update();
					try {
						Thread.sleep(20);
					} catch (Exception ex1) {
						Core.showError(ex1.getMessage());
					}
				}
			}
		}).start();
	}
	
	public void keyPressed(KeyEvent ev) {
		if(ev.getKeyCode() == KeyEvent.VK_M){
			if(playVideo && !paused){
				playVideo = false;
				e.createPlayer(Player.MARIO);
				e.start();
			}
		}else if(ev.getKeyCode() == KeyEvent.VK_L){
			if(playVideo && !paused){
				playVideo = false;
				e.createPlayer(Player.LUIGI);
				e.start();
			}
		}else if(ev.getKeyCode() == KeyEvent.VK_P){
			if(paused){
				paused = false;
				playVideo = false;
				e.wm.addKeyListener(e);
				e.resume();
			}else{
				paused = true;
				playVideo = true;
				e.stop();
				e.wm.removeKeyListener(e);
				playVideo(pause);
			}
		}else if(ev.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}
	public void keyReleased(KeyEvent ev) {
		
	}
	public void keyTyped(KeyEvent arg0) {
	}
}