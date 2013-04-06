/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kenta.game.object;

import kenta.game.logic.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Sactual
 */
public class Sign extends Entity {

    public Sign(Image sign, float x, float y, int width, int height, String name) {
        super(null, x, y, width, height, 0, name, null);
        this.image = sign;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawImage(image, vx, vy);
    }

}
