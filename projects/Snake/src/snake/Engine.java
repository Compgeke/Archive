package snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Stack;

public class Engine extends KeyAdapter{
	private boolean running,playing,pointUsed;
	private DrawArea drawArea;
	private Stack<Sprite> snake,evils;
	private Stack<Point> openSpots;
	private Sprite tempSprite,leadPart,tailPart,joiningPart;
	private int difficulty;
	private Stack<Direction> changePoints;
	private Direction lastPoint;
	private Random rn;
	private long countDown;
	
	protected Engine(){
		drawArea = new DrawArea();
		drawArea.addKeyListener(this);
	}
	
	protected void start(int difficulty){
		this.difficulty = difficulty;
		drawArea.requestFocus();
		drawArea.createBuffer();
		running = true;
		rn = new Random();
		snake = new Stack<Sprite>();
		evils = new Stack<Sprite>();
		openSpots = new Stack<Point>();
		changePoints = new Stack<Direction>();
		snake.add(new Sprite(256,256,16,16));
		leadPart = snake.peek();
		tailPart = snake.firstElement();

		setInitialVelocity();
		calculateOpenSpots();
		
		addGood();
		playing = true;
		gameLoop();
	}
	
	protected boolean isRunning(){
		return running;
	}
	
	private void calculateOpenSpots(){
		for(int i = 0; i < 31; i++){
			for(int x = 0; x < 31; x++){
				openSpots.push(new Point(i,x));
			}
		}
	}
	
	public void setInitialVelocity(){
		snake.get(0).setVelocityY(-1);
	}
	
	private void update(long timePassed){
		for(int i = 0; i < snake.size(); i++){
			tempSprite = snake.get(i);
			if(tempSprite != leadPart)checkDirectionChanged(tempSprite);
			if(tempSprite == tailPart && !pointUsed){
				int dir;
				if(tailPart.getVelocityX() > 0) dir = 3;
				else if(tailPart.getVelocityX() < 0) dir = 2;
				else if(tailPart.getVelocityY() > 0) dir = 1;
				else dir = 0;
				lastPoint = new Direction(tailPart.getX(),tailPart.getY(),dir);
			}
			if(!tempSprite.isJoining()){
				tempSprite.update(timePassed,difficulty);
				checkCollision();
			}else{
				if(tailPart.getX() != lastPoint.getX() || tailPart.getY() != lastPoint.getY()){
					snake.peek().isJoining(false);
					tailPart = snake.peek();
					pointUsed = false;
				}
			}
		}
		checkPartJoined();
	}
	
	private void checkCollision(){
		if(leadPart.getX() < 0 || leadPart.getX() > 496 || leadPart.getY() < 0 || leadPart.getY() > 496){
			end();
		}
		for(int i = 0; i < snake.size(); i++){
			Sprite temp = snake.get(i);
			if(temp != leadPart && !temp.isJoining()){
				if(temp.getX() == leadPart.getX() && 
						temp.getY() == leadPart.getY()){
					end();
				}
			}
		}
		for(int i = 0; i < evils.size(); i++){
			Sprite temp = evils.get(i);
			if(temp.getX() == leadPart.getX() && 
					temp.getY() == leadPart.getY()){
				end();
			}
		}
	}
	
	private void end(){
		countDown = 1000;
		playing = false;
	}
	
	private void checkPartJoined(){
		if(joiningPart.getX() == lastPoint.getX()
				&& joiningPart.getY() == lastPoint.getY()){
			pointUsed = true;
			setDirection(lastPoint.getDirection(),joiningPart);
			snake.push(joiningPart);
			addGood();
		}
	}
	
	private void changedDirection(int direction){
		changePoints.push(new Direction(leadPart.getX(),leadPart.getY(),direction));
	}
	
	private void checkDirectionChanged(Sprite temp){
		for(int i = 0; i < changePoints.size(); i++){
			if(temp.getX() == changePoints.get(i).getX()
					&& temp.getY() == changePoints.get(i).getY()){
				int tempInt = changePoints.get(i).getDirection();
				setDirection(tempInt,temp);
				if(temp == tailPart){
					changePoints.remove(changePoints.firstElement());
				}
			}
		}
	}
	
	private void setDirection(int direction, Sprite temp){
		if(direction == 0){
			temp.setVelocityX(0);
			temp.setVelocityY(-1);
		}else if(direction == 1){
			temp.setVelocityX(0);
			temp.setVelocityY(1);
		}else if(direction == 2){
			temp.setVelocityY(0);
			temp.setVelocityX(-1);
		}else if(direction == 3){
			temp.setVelocityY(0);
			temp.setVelocityX(1);
		}
	}
	
	//draws to screen
	private void draw(Graphics2D g){
		g.setColor(Color.WHITE);
		g.fillRect(0,0,drawArea.getWidth(),drawArea.getHeight());
		if(playing){
			if(joiningPart != null){
				g.setColor(Color.CYAN);
				g.fillOval(joiningPart.getX(), joiningPart.getY(), joiningPart.getWidth(), joiningPart.getHeight());
				g.setColor(Color.BLUE);
				g.drawOval(joiningPart.getX(), joiningPart.getY(), joiningPart.getWidth(), joiningPart.getHeight());
			}
			for(int i = 0; i < snake.size(); i++){
				tempSprite = snake.get(i);
				g.setColor(Color.CYAN);
				g.fillOval(tempSprite.getX(), tempSprite.getY(), tempSprite.getWidth(), tempSprite.getHeight());
				if(tempSprite == leadPart)g.setColor(Color.BLACK);
				else g.setColor(Color.BLUE);
				g.drawOval(tempSprite.getX(), tempSprite.getY(), tempSprite.getWidth(), tempSprite.getHeight());
			}
			for(int i = 0; i < evils.size(); i++){
				tempSprite = evils.get(i);
				g.setColor(Color.RED);
				g.fillRect(tempSprite.getX(), tempSprite.getY(), tempSprite.getWidth(), tempSprite.getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(tempSprite.getX(), tempSprite.getY(), tempSprite.getWidth(), tempSprite.getHeight());			
			}
		}else{
			g.setColor(Color.RED);
			g.setFont(new Font("Comic Sans", Font.BOLD, 70));
			g.drawString("GAME", 50, 150);
			g.drawString("OVER", 250, 300);
		}
	}
	
	protected Canvas getCanvas(){
		return drawArea;
	}
	
	private void addGood(){
		int spot = rn.nextInt(openSpots.size());
		Point temp = openSpots.get(spot);
		openSpots.remove(spot);
		joiningPart = new Sprite((int)temp.getX()*16,(int)temp.getY()*16,16,16);
		joiningPart.isJoining(true);
	}
	
	private void addEvil(){
		int spot = rn.nextInt(openSpots.size());
		Point temp = openSpots.get(spot);
		openSpots.remove(spot);
		evils.push(new Sprite((int)temp.getX()*16,(int)temp.getY()*16,16,16));
	}
	
	//main game loop
	private void gameLoop(){
		new Thread(new Runnable(){
			public void run(){
				long startTime = System.currentTimeMillis();
				long cumTime = startTime;
				long runTime = 0;
				
				while(running){
					long timePassed = System.currentTimeMillis() - cumTime;
					cumTime += timePassed;
					runTime += timePassed;
					
					if(playing){
						if(runTime >= 10000 / difficulty){
							addEvil();
							runTime -= 10000 / difficulty;
						}
						update(timePassed);
					}else{
						countDown -= 20;
						if(countDown <= 0){
							running = false;
						}
					}
					Graphics2D g = drawArea.getGraphics();
					if(g != null){
						draw(g);
					}
					g.dispose();
					drawArea.update();
					try {
						Thread.sleep(20);
					} catch (Exception ex1) {
						ex1.printStackTrace();
					}
				}			
			}
		}).start();
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP){
			if(leadPart.getVelocityY() == 0){
				leadPart.setVelocityX(0);
				changedDirection(0);
				leadPart.setVelocityY(-1);
			}
		}else if(key == KeyEvent.VK_DOWN){
			if(leadPart.getVelocityY() == 0){
				leadPart.setVelocityX(0);
				changedDirection(1);
				leadPart.setVelocityY(1);
			}
		}else if(key == KeyEvent.VK_LEFT){
			if(leadPart.getVelocityX() == 0){
				leadPart.setVelocityY(0);
				changedDirection(2);
				leadPart.setVelocityX(-1);
			}
		}else if(key == KeyEvent.VK_RIGHT){
			if(leadPart.getVelocityX() == 0){
				leadPart.setVelocityY(0);
				changedDirection(3);
				leadPart.setVelocityX(1);
			}
		}
	}
	
	private class Direction{
		float x, y;
		int direction;
		public Direction(float x, float y, int direction){
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
		
		public float getX(){
			return x;
		}
		
		public float getY(){
			return y;
		}
		
		public int getDirection(){
			return direction;
		}
	}
}
