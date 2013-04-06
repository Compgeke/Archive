/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: Sprite
 * Description: Manages the location, and movements of an image on the screen
 */

package marioClone;

import java.awt.Image;

public class Sprite {
	
	private Animation a;
	private float x,y,vx,vy,lastVelocityX;
	private boolean collidingLeft,collidingRight;
	
	//CONSTRUCTOR (Sprite's animation)
	public Sprite(){}
	public Sprite(Animation a){
		this.a = a;
	}
	
	//change animation
	public void changeAnimation(Animation a){
		this.a = a;
	}
	
	//change position
	public void update(long timePassed){
		x += vx * timePassed;
		y += vy * timePassed;
		a.update(timePassed);
	}
	
	//get x position
	public float getX(){
		return x;
	}
	
	//get y position
	public float getY(){
		return y;
	}
	
	//set sprite x position
	public void setX(float x){
		this.x = x;
	}
	
	//set sprite y position
	public void setY(float y){
		this.y = y;
	}
	
	//get sprite width
	public int getWidth(){
		return a.getImage().getWidth(null);
	}
	
	//get sprite height
	public int getHeight(){
		return a.getImage().getHeight(null);
	}
	
	//get horizontal velocity
	public float getVelocityX(){
		return vx;
	}
	
	//get vertical velocity
	public float getVelocityY(){
		return vy;
	}
	
	//set horizontal velocity
	public void setVelocityX(float vx){
		if(this.vx != 0){
			lastVelocityX = this.vx;
		}
		this.vx = vx;
	}
	
	//set vertical velocity
	public void setVelocityY(float vy){
		this.vy = vy;
	}
	
	//get sprite / image
	public Image getImage(){
		return a.getImage();
	}
	
	public boolean isCollidingLeft(){
		return collidingLeft;
	}
	
	public boolean isCollidingRight(){
		return collidingRight;
	}
	
	public void setCollidingLeft(boolean b){
		collidingLeft = b;
	}
	
	public void setCollidingRight(boolean b){
		collidingRight = b;
	}
	
	public float getLastVelocity(){
		if(vx == 0){
			return lastVelocityX;			
		}else{
			return vx;
		}
	}
}