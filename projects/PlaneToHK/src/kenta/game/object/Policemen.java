/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game.object;

import java.util.ArrayList;
import java.util.Random;
import kenta.game.logic.Entity;
import kenta.game.logic.MapUtil;
import kenta.game.logic.SoundWrapper;
import net.phys2d.raw.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Sactual
 */
public class Policemen extends Entity {

    private Random rand;
    private int walkDelta = 0;
    private int walkControl = 0;
    private int walkReset = 3000;
    protected boolean walkingRight = false;
    private MapUtil mapUtil;
    public Image standSprite;
    private ArrayList<Image> walkSprites;
    private int frameCount = 0;
    private long frameTimer = 0;

    public Policemen(World world, float x, float y, int width, int height, SoundWrapper soundWrapper, MapUtil map) {
        super(world, x, y, 50, 50, 1.0f, "police", soundWrapper);
        this.mapUtil = map;
        setVelocity(4000.0F);
        rand = new Random();
    }

    public void init(Image stand, ArrayList<Image> walk) {
        super.setImage(image);
        this.standSprite = stand;
        this.walkSprites = walk;
        this.setWidth(stand.getWidth());
        this.setHeight(stand.getHeight());
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if (!this.isMoving()) {
            this.image = standSprite;
        } else {
            if (System.currentTimeMillis() - frameTimer >= 150) {
                if (frameCount == 2) {
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
        //g.drawImage(image, getX(), getY() - (getHeight()/2));
        image.drawCentered(getX(), getY());
    }

    @Override
    public void update(int delta) {
        if (getX() <= 10) {
            walkControl = 0;
        } else {
            walkControl -= delta;
        }
        if (walkControl <= 0) {
            if (rand.nextInt(2) == 1) {
                walkRight(delta);
            } else {
                walkLeft(delta);
            }
            walkControl = walkReset;
        }

    }

    public void walkLeft(int delta) {
        super.moveLeft(this.velocity);
    }

    public void walkRight(int delta) {
        super.moveRight(this.velocity);

    }
}
