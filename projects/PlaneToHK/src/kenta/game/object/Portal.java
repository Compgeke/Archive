/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kenta.game.object;

import kenta.game.logic.Entity;
import net.phys2d.raw.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Sactual
 */
public class Portal extends Entity {
    public Portal(Image porta, World world, float x, float y) {
        super(world, x, y, porta.getWidth(), porta.getHeight(), 1.0f, "police", null);
        this.image = porta;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        image.drawCentered(getX(), getY());
    }

}
