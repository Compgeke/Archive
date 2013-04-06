/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: Engine
 * Description: Extends Core, will handle the game functions, and user input
 */

package marioClone;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Engine extends Core implements KeyListener{
	
	private static float startX, startY;
	private Stack<Map> maps;
	private Image bg;
	private Player p;
	private Platform pf[];
	private boolean jumping,falling,scrollableLeft,scrollableRight,moving;
	private float movementSpeed;
	private int scroll,oldScroll,trackMap,currentMap;
	private int paddingLeft, paddingRight,maxRight,maxLeft;//distance from edge of screen scrolling starts
	private Stack<DrawImage> drawImages;
	private JFileChooser mapChooser;
	private File directory;
	
	//TODO implement enemies.
	
	public Engine(Map map[]){
		maps = new Stack<Map>();
		for(Map p: map){
			maps.push(p);
		}
		currentMap = 0;
		bg = maps.get(currentMap).getBackground();
		pf = maps.get(currentMap).getPlatforms();
		
		mapChooser = new JFileChooser();
		mapChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		directory = new File(System.getProperty("user.home") + "/Desktop/MarioMaps/map.csv");
		mapChooser.setSelectedFile(directory);
	}
	
	public void start(){
		super.start();
		wm.addKeyListener(this);
	}
	
	public void createPlayer(int character){
		p = new Player(character);
		startX = wm.getWidth()/2 - p.getWidth()/2;
		startY = wm.getHeight() - p.getHeight() - 32;
		p.setX(startX);
		p.setY(startY);
		
		trackMap = 1;
		
		paddingLeft = 300;
		paddingRight = wm.getWidth() - 300;
		movementSpeed = 0.3f;
		oldScroll = 0;		
	}
	
	public void createWindow(){
		super.createWindow();
		drawImages = new Stack<DrawImage>();
	}
	
	//if you die, restart, if you get to the next map, reset position
	public synchronized void reset(){
		p.setVelocityX(0);
		p.setVelocityY(0);
		p.setIdle();
		p.setX(startX);
		p.setY(startY);
		scroll = 0;
	}
	
	//change map
	public synchronized void changeMap(int mapIndex){
		currentMap = mapIndex;
		bg = maps.get(currentMap).getBackground();
		pf = maps.get(currentMap).getPlatforms();
		reset();
	}
	
	//create list of images to draw
	public synchronized void createScreen(){
		drawImages.clear();
		//draw background first
		for(int i = 0; i <= maps.get(currentMap).getMapSize(); i++){
			drawImages.push(new DrawImage(bg, -scroll + i * WindowManager.WIDTH, 0));			
		}
		
		//draw any objects on background
		
		//draw platforms
		for(int i = 0; i < pf.length; i++){
			if(spriteIsOnScreen(pf[i])){
				drawImages.push(new DrawImage(pf[i].getImage(),-scroll+Math.round(pf[i].getX()),Math.round(pf[i].getY())));
			}
		}
		
		//draw player
		drawImages.push(new DrawImage(p.getImage(),Math.round(p.getX()),Math.round(p.getY())));
		
		//draw anything over the player / that the player can go behind
		//nothing
	}
	
	//get screen
	public synchronized Stack<DrawImage> getScreen(){
		createScreen();
		return drawImages;
	}
	
	//draw background and the sprites on to the screen
	public synchronized void draw(Graphics2D g) {		
		createScreen();
		while(!drawImages.isEmpty()){
			DrawImage di = drawImages.firstElement();
			g.drawImage(di.getImage(),di.getX(),di.getY(),null);
			drawImages.remove(di);
		}
	}
	
	//update sprite
	public synchronized void update(long timePassed){
		//easier use of variables
		int h = WindowManager.HEIGHT;
		int sh = p.getHeight();//player height
		int x = Math.round(p.getX());//player x and y
		int y = Math.round(p.getY());
		
		p.update(timePassed);
		
		//control the scrolling / movement
		if(scroll >= WindowManager.WIDTH * maps.get(currentMap).getMapSize()){
			scrollableRight = false;
		}else{
			scrollableRight = true;
		}
		if(scroll <= 0){
			scrollableLeft = false;
		}else{
			scrollableLeft = true;
		}
		if(moving){
			p.setMoving();
			if(scrollableRight && p.getX() >= paddingRight){
				scroll+=movementSpeed*timePassed;
			}else if(scrollableLeft && p.getX() <= paddingLeft){
				scroll-=movementSpeed*timePassed;
			}else if(p.getX() == paddingRight){
				p.setVelocityX(movementSpeed);
			}else if(p.getX() == paddingLeft){
				p.setVelocityX(-movementSpeed);
			}
		}
		
		//check if you reach the end of the level!
		if(p.getX() >= WindowManager.WIDTH - p.getWidth()){
			if(trackMap == maps.size()){
				trackMap = 0;
			}
			changeMap(trackMap);
			trackMap++;
		}
		
		//prevent player form leaving playing area
		if(scrollableLeft){
			maxLeft = paddingLeft;
		}else{
			maxLeft = 0;
		}
		if(scrollableRight){
			maxRight = paddingRight;
		}else{
			maxRight = WindowManager.WIDTH - p.getWidth();
		}
		if(x < maxLeft){
			p.setVelocityX(0f);
			p.setX(maxLeft);
		}else if(x > maxRight){
			p.setVelocityX(0f);
			p.setX(maxRight);
		}else if(y + sh > h*1.5){//if you fall off!
			//TODO implement checkpoints
			reset();
		}
		
		//check if the player is moving
		if(p.getVelocityX() == 0 
				&& p.getVelocityY() == 0
				&& !moving){
			p.setIdle();
		}else if(p.getVelocityX() > 0){
			p.setRight();
		}else if(p.getVelocityX() < 0){
			p.setLeft();
		}else if(p.getLastVelocity() > 0){
			p.setRight();
		}else if(p.getLastVelocity() < 0){
			p.setLeft();
		}else{
			p.setMoving();
		}
		
		//correct animation while scrolling/jumping
		
		if(p.getVelocityY() > 0){
			p.setJumping();
		}else if(p.getVelocityY() < 0){
			p.setFalling();
		}
		if(oldScroll < scroll && scrollableRight){
			p.setRight();
		}else if(oldScroll > scroll && scrollableLeft){
			p.setLeft();
		}
		
		oldScroll = scroll;
		
		//check if the player is falling
		if(p.getVelocityY() == 0){
			falling = false;
			jumping = false;
		}
		
		if(p.getVelocityY() > 0){
			jumping = false;
		}
		
		//gravity
		if(!collidingDown() && !jumping){
			p.setVelocityY(0.4f);
			falling = true;
			p.setFalling();
		}
		
		//jumping through solid walls? no way man
		checkVerticalCollision();
		
		//walking through solid walls? i think NOT!
		checkHorizontalCollision();
		
		
	}
	//check if the Sprite is on the screen
	public synchronized boolean spriteIsOnScreen(Sprite s){
		if(s.getX() + s.getWidth() - scroll > 0 
				&& s.getX() - scroll < WindowManager.WIDTH){
			return true;
		}else{
			return false;			
		}
	}
	/**
	 * TODO: rewrite collision to include sprites for enemy implementation
	 */
	//check if they are on a platform
	public synchronized void checkVerticalCollision(){
		collidingUp();
	}
	
	public synchronized void checkHorizontalCollision(){
		collidingLeft();
		collidingRight();
	}
	
	public synchronized boolean collidingUp(){
		int sw = p.getWidth();
		int x = Math.round(scroll + p.getX());
		int y = Math.round(p.getY());
		boolean canStopJumping = false;
		for(int i = 0; i < pf.length; i++){
			if(spriteIsOnScreen(pf[i])
					&& !pf[i].canWalkThrough()
					&& x + sw > pf[i].getX()
					&& x < pf[i].getX() + pf[i].getWidth()
					&& y > pf[i].getY()
					&& y < pf[i].getY() + pf[i].getHeight()
					&& p.getVelocityY() < 0){
				p.setVelocityY(0);
				p.setY(pf[i].getY() + pf[i].getHeight());
				canStopJumping = true;
			}
		}		
		return canStopJumping;	
	}
	
	public synchronized boolean collidingDown(){
		int sw = p.getWidth();
		int sh = p.getHeight();
		int x = Math.round(scroll + p.getX());
		int y = Math.round(p.getY());
		boolean canStopFalling = false;
		for(int i = 0; i < pf.length; i++){
			if(spriteIsOnScreen(pf[i])
					&& pf[i].canWalkOn()
					&& x < pf[i].getX() + pf[i].getWidth()
					&& x + sw > pf[i].getX()
					&& y + sh >= pf[i].getY()
					&& y + sh < pf[i].getY() + pf[i].getHeight()
					&& p.getVelocityY() >= 0){
				p.setVelocityY(0);
				p.setY(pf[i].getY() - sh);
				canStopFalling = true;
			}
		}		
		return canStopFalling;		
	}
	
	public synchronized boolean collidingLeft(){
		int sw = p.getWidth();
		int sh = p.getHeight();
		int x = Math.round(scroll + p.getX());
		int y = Math.round(p.getY());
		boolean collidingLeft = false;
		for(int i = 0; i < pf.length; i++){
			if(spriteIsOnScreen(pf[i])){			
				if(!pf[i].canWalkThrough()){
					if(!(y + sh <= pf[i].getY())
							&& !(y >= pf[i].getY() + pf[i].getHeight())){
						if(!(x + sw <= pf[i].getX())
								&& !(x >= pf[i].getX() + pf[i].getWidth())
								&& p.getLastVelocity() < 0){
							collidingLeft = true;
							p.setVelocityX(0);
							p.setX(pf[i].getX() + pf[i].getWidth() - scroll);
						}
					}
				}
			}
		}
		return collidingLeft;
	}
	
	public synchronized boolean collidingRight(){
		int sw = p.getWidth();
		int sh = p.getHeight();
		int x = Math.round(scroll + p.getX());
		int y = Math.round(p.getY());
		boolean collidingRight = false;
		for(int i = 0; i < pf.length; i++){
			if(spriteIsOnScreen(pf[i])){			
				if(!pf[i].canWalkThrough()){
					if(!(y + sh <= pf[i].getY())
							&& !(y >= pf[i].getY() + pf[i].getHeight())){
						if(!(x + sw <= pf[i].getX())
								&& !(x >= pf[i].getX() + pf[i].getWidth())
								&& p.getLastVelocity() > 0){
							collidingRight = true;
							p.setVelocityX(0);
							p.setX(pf[i].getX() - p.getWidth() - scroll);
						}
					}
				}
			}
		}
		return collidingRight;
	}

	//movement command
	public synchronized void keyPressed(KeyEvent ev) {
		int temp = ev.getKeyCode();
		
		//game controls
		if(temp == KeyEvent.VK_LEFT){
			if(p.getX() != maxLeft && !collidingLeft()){
				p.setVelocityX(-movementSpeed);
			}
			p.setLeft();
			p.setMoving();
			moving = true;
		}else if(temp == KeyEvent.VK_RIGHT){
			if(p.getX() != maxRight && !collidingRight()){
				p.setVelocityX(movementSpeed);
			}
			p.setRight();
			p.setMoving();
			moving = true;
		}else if(temp == KeyEvent.VK_1){
			changeMap(0);
		}else if(temp == KeyEvent.VK_2){
			changeMap(1);
		}else if(temp == KeyEvent.VK_3){
			changeMap(2);
		}else if(temp == KeyEvent.VK_UP){
			if(!jumping && !falling){
				jumping = true;
				p.setJumping();
				new Thread(
						new Runnable(){
							public void run(){
								try {
									p.setVelocityY(-0.5f);
									while(p.getVelocityY() < 0){
										p.setVelocityY(p.getVelocityY()+0.05f);
										Thread.sleep(50);
									}
									falling = true;
								} catch (Exception ex1) {
									ex1.printStackTrace();
								}
							}
						}).start();
			}
		}else if(temp == KeyEvent.VK_ENTER){
			int returnValue = mapChooser.showOpenDialog(wm);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
                File f = mapChooser.getSelectedFile();
                if(f.isFile()){
                	try {
                		maps.push(new Map(f.getAbsolutePath(),true));
                	} catch(Exception ex) {
                		JOptionPane.showMessageDialog(wm,"Error loading map file.","OOPS!",JOptionPane.PLAIN_MESSAGE);
                		ex.printStackTrace();
                	} finally {                		
                		trackMap = maps.size() - 1;
                		changeMap(trackMap);
                	}
                }else{
                	JOptionPane.showMessageDialog(wm,"Please select a file, not a directory.","OOPS!",JOptionPane.PLAIN_MESSAGE);
                }
            } else {
            	JOptionPane.showMessageDialog(wm,"Load cancelled by user.","Load cancelled!",JOptionPane.PLAIN_MESSAGE);
            }
		}
	}
	public synchronized void keyReleased(KeyEvent ev) {		
		//game controls
		if(ev.getKeyCode() != KeyEvent.VK_UP){
			if(ev.getKeyCode() == KeyEvent.VK_LEFT){
				if(p.getVelocityX()  < 0){
					p.setVelocityX(0);
				}
			}else if(ev.getKeyCode() == KeyEvent.VK_RIGHT){
				if(p.getVelocityX()  > 0){
					p.setVelocityX(0);
				}
			}
			moving = false;
		}
	}
	public void keyTyped(KeyEvent ev) {
	}
	
	//Protected INNER CLASS!!!!//////
	protected class DrawImage{
		private Image image;
		private int x,y;

		public DrawImage(Image i, int x, int y){
			image = i;
			this.x = x;
			this.y = y;
		}
		
		//return the image
		public Image getImage(){
			return image;
		}
		
		//return the x
		public int getX(){
			return x;
		}
		
		//return the y 
		public int getY(){
			return y;
		}
	}
}