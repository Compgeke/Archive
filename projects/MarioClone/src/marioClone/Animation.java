/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: Animation
 * Description: Manages the animation of a sprite, private inner class scales sprite
 */

package marioClone;

import java.awt.Image;
import java.util.Stack;

public class Animation {
	private Stack<OneScene> scenes;
	private int sceneIndex;
	private long movieTime;
	private long totalTime;
	private int spriteScale = 2;
	private boolean isSprite;
	
	
	//CONSTRUCTOR
	public Animation(boolean isSprite){
		this.isSprite  = isSprite;//if it is a sprite, scale it accordingly
		scenes = new Stack<OneScene>();
		totalTime = 0;
		start();
	}
	
	//add scene to ArrayList and set time for each scene
	public synchronized void addScene(Image i, long t){
		totalTime += t;
		scenes.push(new OneScene(i, totalTime));
	}
	
	//remove scene
	public synchronized void removeScene(){
		totalTime -= scenes.lastElement().endTime;
		scenes.pop();
	}
	
	//start animation from beginning
	public synchronized void start(){
		movieTime = 0;
		sceneIndex = 0;
	}
	
	//change scenes
	public synchronized void update(long timePassed){
		if(scenes.size()>1){
			movieTime += timePassed;
			if(movieTime >= totalTime){
				start();
			}
			while(movieTime > getScene(sceneIndex).endTime){
				sceneIndex++;
			}
		}
	}
	
	//get animations current scene(aka image)
	public synchronized Image getImage(){
		if(scenes.size() == 0){
			return null;
		}else{
			return getScene(sceneIndex).pic;
		}
	}
	
	private OneScene getScene(int x){
		return (OneScene)scenes.elementAt(x);
	}
	
///////////////////// PRIVATE INNER CLASS ////////////////
	
	private class OneScene{
		Image pic;
		long endTime;
		
		public OneScene(Image pic, long endTime){
			if(isSprite){
				this.pic = pic.getScaledInstance(pic.getWidth(null)*spriteScale, -1, Image.SCALE_FAST);
			}else{
				this.pic = pic;
			}
			this.endTime = endTime;
		}
	}
	
}