package pong;

public class Sprite {
	private float x,y,vx,vy;
	private int height, width;
	
	protected Sprite(float x, float y, int width, int height){
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		stop();
	}
	
	protected void update(long timePassed){
		x += vx * timePassed;
		y += vy * timePassed;
	}
	
	protected void stop(){
		vx = 0;
		vy = 0;		
	}
	
	protected float getX(){
		return x;
	}
	
	protected float getY(){
		return y;
	}
	
	protected int getRoundX(){
		return Math.round(x);
	}
	
	protected int getRoundY(){
		return Math.round(y);
	}
	
	protected void setX(float x){
		this.x = x;
	}
	
	protected void setY(float y){
		this.y = y;
	}
	
	protected void setVelocityX(float vx){
		this.vx = vx;
	}
	
	protected void setVelocityY(float vy){
		this.vy = vy;
	}
	
	protected float getVelocityX(){
		return vx;
	}
	
	protected float getVelocityY(){
		return vy;
	}
	
	protected int getHeight(){
		return height;
	}
	
	protected int getWidth(){
		return width;
	}
}
