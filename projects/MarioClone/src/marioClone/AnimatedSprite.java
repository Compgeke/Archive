package marioClone;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class AnimatedSprite extends Sprite{
	protected boolean facingLeft;
	protected Animation moveLeft,moveRight,jumpLeft,jumpRight,fallLeft,fallRight,idleLeft,idleRight;
	protected Image sprites;
	protected BufferedImage btest;
	protected int spriteSheetStartY, frameLength;
	
	public AnimatedSprite(int character){
		spriteSheetStartY = character;
		try {
			sprites = ImageIO.read(getClass().getResourceAsStream("/Images/spriteSheet.gif"));
			btest = Engine.toBufferedImage(sprites);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setRight(){
		facingLeft = false;
	}
	
	public void setLeft(){
		facingLeft = true;
	}
	
	public void setMoving(){
		if(facingLeft){
			changeAnimation(moveLeft);
		}else{
			changeAnimation(moveRight);
		}
	}
	
	public void setJumping(){
		if(facingLeft){
			changeAnimation(jumpLeft);
		}else{
			changeAnimation(jumpRight);
		}
	}
	
	public void setFalling(){
		if(facingLeft){
			changeAnimation(fallLeft);
		}else{
			changeAnimation(fallRight);
		}
	}
	
	public void setIdle(){
		if(facingLeft){
			changeAnimation(idleLeft);
		}else{
			changeAnimation(idleRight);
		}
	}
}
