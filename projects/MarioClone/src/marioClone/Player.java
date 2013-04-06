/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: Player
 * Description: Extends Sprite, contains animation of the player, and possibly
 * 		inventory items and such later on
 */

package marioClone;

public class Player extends AnimatedSprite{
	public static final int MARIO = 3;
	public static final int LUIGI = 76;
	public Player(int character) {
		super(character);
		int pixelsHigh = 21;
		
		//create left facing animations
		moveLeft = new Animation(true);
		moveLeft.addScene(btest.getSubimage(68 + 35, spriteSheetStartY, 11, pixelsHigh),frameLength);
		moveLeft.addScene(btest.getSubimage(68, spriteSheetStartY, 14, pixelsHigh),frameLength+75);
		moveLeft.addScene(btest.getSubimage(68 + 35, spriteSheetStartY, 11, pixelsHigh),frameLength);
		moveLeft.addScene(btest.getSubimage(68 + 17, spriteSheetStartY, 15, pixelsHigh),frameLength+75);
		
		idleLeft = new Animation(true);
		idleLeft.addScene(btest.getSubimage(68 + 50, spriteSheetStartY, 16, pixelsHigh),frameLength);

		jumpLeft = new Animation(true);
		jumpLeft.addScene(btest.getSubimage(68 + 70, spriteSheetStartY, 15, pixelsHigh),frameLength);
		fallLeft = jumpLeft;
		
		//create right facing animations
		moveRight = new Animation(true);
		moveRight.addScene(btest.getSubimage(234 - 33, spriteSheetStartY, 11, pixelsHigh),frameLength);
		moveRight.addScene(btest.getSubimage(234, spriteSheetStartY, 14, pixelsHigh),frameLength+75);
		moveRight.addScene(btest.getSubimage(234 - 33, spriteSheetStartY, 11, pixelsHigh),frameLength);
		moveRight.addScene(btest.getSubimage(234 - 18, spriteSheetStartY, 15, pixelsHigh),frameLength+75);
		
		idleRight = new Animation(true);
		idleRight.addScene(btest.getSubimage(232 - 52, spriteSheetStartY, 16, pixelsHigh),frameLength);

		jumpRight = new Animation(true);
		jumpRight.addScene(btest.getSubimage(232 - 70, spriteSheetStartY, 15, pixelsHigh),frameLength);
		fallRight = jumpRight;
		
		setIdle();
	}	
}