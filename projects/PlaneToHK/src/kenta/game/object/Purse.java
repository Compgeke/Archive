/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game.object;

import kenta.game.cache.ImageCache;
import kenta.game.logic.Entity;
import kenta.game.logic.SoundWrapper;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/** *
 * @author Sactual
 */
public class Purse extends Entity {

    public Purse(World world, float x, float y, SoundWrapper soundWrapper, boolean right) {
        super(world, x, y, ImageCache.PLAYER_BULLET.getWidth(), ImageCache.PLAYER_BULLET.getHeight(), 1.0F, "purse", soundWrapper);
        setImage(ImageCache.PLAYER_BULLET);
    }
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawImage(image, getX()-getWidth()/2, getY()-getHeight()/2);
    }
    
    public void remove(){
        getWorld().remove(getBody());
    }
}
