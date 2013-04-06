package pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Engine extends KeyAdapter{
	private boolean running,playing;
	private DrawArea drawArea;
	private Sprite ball,player1,player2;
	private int startCords[][] = {{310,170},{40,130},{580,130}};
	private Random rn;
	private int scoreOne,scoreTwo;
	
	protected Engine(){
		drawArea = new DrawArea();
		drawArea.addKeyListener(this);
		reset();
	}
	
	private void reset(){
		playing = false;
		ball = new Sprite(startCords[0][0],startCords[0][1],20,20);
		player1 = new Sprite(startCords[1][0],startCords[1][1],20,100);
		player2 = new Sprite(startCords[2][0],startCords[2][1],20,100);
		
		ball.stop();
		player1.stop();
		player2.stop();
	}
	
	protected void start(){
		scoreOne = 0;
		scoreTwo = 0;
		drawArea.createBuffer();
		running = true;
		gameLoop();
		startAI();
	}
	
	//update animation
	private void update(long timePassed){
		checkBounds();
		ball.update(timePassed);
		player1.update(timePassed);
		player2.update(timePassed);
	}
	
	//draws to screen
	private void draw(Graphics2D g){		
		//draw main components
		g.setColor(Color.BLACK);
		g.fillRect(0,0,drawArea.getWidth(),drawArea.getHeight());
		g.setColor(Color.GREEN);
		g.fillRect(Pong.WIDTH/2-5, 0, 10, Pong.HEIGHT);
		g.fillRect(player1.getRoundX(),player1.getRoundY(),player1.getWidth(),player1.getHeight());
		g.fillRect(player2.getRoundX(),player2.getRoundY(),player2.getWidth(),player2.getHeight());
		if(playing){
			g.fillOval(Math.round(ball.getX()), Math.round(ball.getY()), ball.getWidth(), ball.getHeight());
		}else{
			g.setColor(Color.RED);
			g.drawString("Press Space to Play", 260, 190);
		}
		g.setColor(Color.WHITE);
		g.drawString("Score: " + scoreOne, 250, 40);
		g.drawString("Score: " + scoreTwo, 350, 40);
		
		
	}
	
	protected Canvas getCanvas(){
		return drawArea;
	}
	
	public void keyPressed(KeyEvent ev){
		if(ev.getKeyCode() == KeyEvent.VK_UP){
			player1.setVelocityY(-0.3f);
		}else if(ev.getKeyCode() == KeyEvent.VK_DOWN){
			player1.setVelocityY(0.3f);
		}else if(ev.getKeyCode() == KeyEvent.VK_SPACE){
			if(!playing){
				playing = true;
				startAI();
				launchBall();
			}
		}
	}
	
	public void launchBall(){
		int x,y,xd,yd;
		rn = new Random();
		x = rn.nextInt(2) + 2;
		y = rn.nextInt(2) + 2;
		xd = rn.nextInt(2);
		if(xd == 0){
			xd = -1;
		}else{
			xd = 1;
		}
		
		yd = rn.nextInt(2);
		if(yd == 0){
			yd = -1;
		}else{
			yd = 1;
		}
		
		System.out.println(x + " " + y + " " + xd + " " + yd);
		
		switch(x){
		case 3:
			ball.setVelocityX(xd*0.3f);
			break;
		case 2:
			ball.setVelocityX(xd*0.2f);
			break;
		default:
			ball.setVelocityX(xd*0.3f);			
		}
		
		switch(y){
		case 3:
			ball.setVelocityY(yd*0.3f);
			break;
		case 2:
			ball.setVelocityY(yd*0.2f);
			break;
		default:
			ball.setVelocityY(yd*0.3f);			
		}
	}
	
	public void keyReleased(KeyEvent ev){
		if(ev.getKeyCode() == KeyEvent.VK_UP || ev.getKeyCode() == KeyEvent.VK_DOWN){
			player1.setVelocityY(0f);
		}
	}
	
	//main game loop
	private void gameLoop(){
		new Thread(new Runnable(){
			public void run(){
				long startTime = System.currentTimeMillis();
				long cumTime = startTime;
				
				while(running){
					long timePassed = System.currentTimeMillis() - cumTime;
					cumTime += timePassed;
					
					update(timePassed);				
					Graphics2D g = drawArea.getGraphics();
					if(g != null){
						draw(g);
					}
					g.dispose();
					drawArea.update();
					try {
						Thread.sleep(30);
					} catch (Exception ex1) {
						ex1.printStackTrace();
					}
				}				
			}
		}).start();
	}
	
	private void checkBounds(){
		int y;
		float vx,vy;
		ball.getRoundX();
		y = ball.getRoundY();
		vx = ball.getVelocityX();
		vy = ball.getVelocityY();
		if(y < 0){
			ball.setY(0f);
			ball.setVelocityY(-vy*1.05f);
		}else if(y+20 > Pong.HEIGHT){
			ball.setY(Pong.HEIGHT-20);
			ball.setVelocityY(-vy);
		}
		if(ball.getX()+20 < 0){
			scoreTwo++;
			reset();
		}else if(ball.getX() > Pong.WIDTH){
			scoreOne++;
			reset();
		}
		if(ballCollidesWith(player1,true)){
			ball.setX(player1.getX()+20);
			ball.setVelocityX(-vx);
		}else if(ballCollidesWith(player2,false)){
			ball.setX(player2.getX()-20);
			ball.setVelocityX(-vx);
		}
		
		//players can't go out of bounds
		checkPlayerBounds(player1);
		checkPlayerBounds(player2);
	}
	
	private void checkPlayerBounds(Sprite player){
		if(player.getY() < 0){
			player.setY(0);
			player.setVelocityX(0);
		}else if(player.getY()+100 > Pong.HEIGHT){
			player.setY(Pong.HEIGHT-100);
			player.setVelocityX(0);
		}
	}
	
	private boolean ballCollidesWith(Sprite s,boolean left){
		boolean result = false;
		if((left
				&& ball.getX() < s.getX()+20
				&& ball.getX()+20 > s.getX()
				&& ball.getY() > s.getY()
				&& ball.getY()+20 < s.getY()+ 100)
				||
			(!left
					&& ball.getX()+20 > s.getX()
					&& ball.getX() < s.getX()+20
					&& ball.getY() > s.getY()
					&& ball.getY()+20 < s.getY()+ 100)){
			result = true;
		}
		return result;
	}
	
	private void startAI(){
		new Thread(new Runnable(){
			public void run(){
				int time = 0;
				while(playing){
					if(ball.getX() > Pong.WIDTH/2){
						if(player2.getY()+50 > ball.getY()+10){
							player2.setVelocityY(-0.25f);
						}else if(player2.getY()+50 < ball.getY()+10){
							player2.setVelocityY(0.25f);
						}else{
							player2.setVelocityY(0.0f);
						}
					}else{
						player2.setVelocityY(0.0f);
					}
						if(time % 2500 == 0){
							ball.setVelocityX(ball.getVelocityX()*1.05f);
							ball.setVelocityY(ball.getVelocityY()*1.05f);
						}
					try {
						Thread.sleep(20);
						time++;
					} catch (Exception ex1) {
						ex1.printStackTrace();
					}			
				}
			}
		}).start();
	}
}
