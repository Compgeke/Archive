package snake;

public class Sprite {
	private int x,y,vx,vy;
	private int height, width;
	private long lastTime;
	private boolean joining;
	
	protected Sprite(int x, int y, int width, int height){
		lastTime = 0;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		stop();
	}
	
	protected void update(long timePassed, int difficulty){
		lastTime += timePassed;
		if(lastTime > 1000 / difficulty){
			x += vx * 16;
			y += vy * 16;
			lastTime -= 1000 / difficulty;
		}
	}
	
	protected boolean isJoining(){
		return joining;
	}
	
	protected void isJoining(boolean joining){
		this.joining = joining;
	}
	
	protected void stop(){
		vx = 0;
		vy = 0;		
	}
	
	protected int getX(){
		return x;
	}
	
	protected int getY(){
		return y;
	}
	
	protected void setX(int x){
		this.x = x;
	}
	
	protected void setY(int y){
		this.y = y;
	}
	
	protected void setVelocityX(int vx){
		this.vx = vx;
	}
	
	protected void setVelocityY(int vy){
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