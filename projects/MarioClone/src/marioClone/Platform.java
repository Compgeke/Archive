/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: Platform
 * Description: Extends Sprint, image objects on the map
 */

package marioClone;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Platform extends Sprite{
	private Image platformImage;
	private int collisionLevel;//0 - none; 1 - from top only; 2 - 4 direction collision;
	
	public Platform(String path){
		platformImage = new ImageIcon(this.getClass().getResource("../images/" + path)).getImage();
	}
	
	public Platform(BufferedImage bi){
		platformImage = bi;
	}
	
	public Image getImage(){
		return platformImage;
	}
	
	public int getWidth(){
		return platformImage.getWidth(null);
	}
	
	public int getHeight(){
		return platformImage.getHeight(null);
	}
	
	public void setCollisionLevel(int i){
		collisionLevel = i;
	}
	
	public boolean canWalkOn(){
		return (collisionLevel == 0? false:true);
	}
	
	public boolean canWalkThrough(){
		return (collisionLevel == 2? false:true);
	}
}