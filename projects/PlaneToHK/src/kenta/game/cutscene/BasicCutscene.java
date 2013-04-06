/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kenta.game.cutscene;

import kenta.game.MainGame;
import kenta.game.cache.ImageCache;
import kenta.game.logic.Entity;
import kenta.game.object.Player;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Sactual
 */
public class BasicCutscene extends Entity {
    private boolean finished = false;

    private Image speaker;
    private String text;

    public BasicCutscene(Player p, String text) {
        super(null, 0 + 30, 600 - 85, ImageCache.DIALOG.getWidth(), ImageCache.DIALOG.getHeight(), 0, "cutscene", null);
        this.image = ImageCache.DIALOG;
        this.speaker = p.standSprite;
        this.text = text;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawImage(image, 0 + 30, 600 - 85);
        g.drawImage(speaker.getFlippedCopy(true, false), 0 + 55, 600 - 70);
        g.setColor(Color.white);
        g.drawString(text, 0 + 115, 600 - 75);
    }

    @Override
    public void update(int i) throws SlickException {
        Input input = MainGame.getInstance().getContainer().getInput();
        if (input.isKeyPressed(Input.KEY_ENTER) && input.isKeyDown(Input.KEY_ENTER)) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
