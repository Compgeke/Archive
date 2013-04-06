/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game.object;

import java.util.ArrayList;
import kenta.game.MainGame;
import kenta.game.logic.Entity;
import kenta.game.logic.Score;
import kenta.game.logic.SoundWrapper;
import kenta.game.state.GameplayState;
import net.phys2d.raw.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Sactual
 */
public class Player extends Entity {
    public static final int MAX_LIFE = 3;
    public Image standSprite;
    private ArrayList<Image> walkSprites;
    private int frameCount = 0;
    private long frameTimer = 0, shootTimer = 0;
    private static int LIFE;
    public static Score SCORE;
    private static boolean DEAD, GAME_OVER, DEATH_STARTED;
    private static int CAUSE_OF_DEATH;
    public static final int POLICE = 0, FELL = 1, TIME = 2;
    public static final String[] DEATH_CUT_SCENES = {"Erm, I am just passing by fellow officer, please do not take me back..",
        "AHHHHHHHHHHHHHHHHHHHHHH!", "Oh no! I missed the flight!"};

    public Player(World world, float x, float y, int width, int height, String name, SoundWrapper soundWrapper) {
        super(world, x, y, width, height, 1.0f, name, soundWrapper);
        getBody().setFriction(0.0f);
    }
    
    public void init(Image stand, ArrayList<Image> walk) {
        super.setImage(image);
        this.standSprite = stand;
        this.walkSprites = walk;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if (!this.isMoving()) {
            this.image = standSprite;
        } else {
            if (System.currentTimeMillis() - frameTimer >= 150) {
                if (frameCount == 3) {
                    frameCount = 0;
                } else {
                    frameCount++;
                }
                frameTimer = System.currentTimeMillis();
            }
            this.image = walkSprites.get(frameCount);
        }
        if (isFacingRight()) {
            this.image = this.image.getFlippedCopy(true, false);
        }
        image.drawCentered(this.vx, this.vy);
    }
    
    public static void setCauseOfDeath(int i){
        CAUSE_OF_DEATH = i;
    }
    
    public static int getCauseOfDeath(){
        return CAUSE_OF_DEATH;
    }
    
    public static void setLife(int life){
        LIFE = life;
    }
    
    public static int getLife(){
        return LIFE;
    }
    
    public static boolean isDead(){
        return DEAD;
    }
    
    public static void loseLife(int causeOfDeath){
        CAUSE_OF_DEATH = causeOfDeath;
        LIFE--;
        DEAD = true;
        DEATH_STARTED = false;
        if(LIFE < 0){
            GAME_OVER = true;
        }
    }
    
    public static void revive(){
        DEAD = false;
    }
    
    public static boolean isGameOver(){
        return GAME_OVER;
    }
    
    public static boolean isDeathStarted(){
        return DEATH_STARTED;
    }
    
    public static void startDeath(){
        DEATH_STARTED = true;
    }
    
    public static void restart(){
        GAME_OVER = false;
    }
    
    @Override
    public void jump(float jumpVelocity) throws SlickException {
        super.jump(jumpVelocity);
        // Play sound?
    }

    public void shootBullet() {
        if (System.currentTimeMillis() - shootTimer >= 200) {
            int facingRightFactor = this.isFacingRight()?1:-1;
            Purse temp = new Purse(world, this.getX() + 30*facingRightFactor, getY(), soundWrapper, this.isFacingRight());
            ((GameplayState) MainGame.getInstance().getState(GameplayState.ID)).addProjectile(temp);
            temp.getBody().setFriction(1.0f);
            temp.getBody().setCanRest(true);
            world.add(temp.getBody());
            shootTimer = System.currentTimeMillis();
        }
    }

    public void walkLeft(int delta) {
        super.moveLeft(this.velocity);
    }

    public void walkRight(int delta) {
        super.moveRight(this.velocity);
    }
}