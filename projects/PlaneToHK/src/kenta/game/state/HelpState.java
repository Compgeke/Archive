package kenta.game.state;

import kenta.game.cache.ImageCache;
import kenta.game.menu.Button;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Kevin Nash
 */
public class HelpState extends BasicGameState{
    public static final int ID = 3;
    private Button back;
    @Override
    public int getID() {
        return ID;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        back = new Button(50, 300, ImageCache.BACK_UP, ImageCache.BACK_OVER, ImageCache.BACK_DOWN);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawImage(ImageCache.MENU_BACKGROUND, 0, 0);
        g.drawString("Instructions:"
                + "\nUse the LEFT and RIGHT arrows to move, UP to jump."
                + "\nBy pressing space you will spawn a movable Purse. A large one."
                + "\nThe goal is to get to your flight on time with out getting caught by the police."
                + "\nDon't fall off, and use your purses to reach high places.", 75, 325);
        back.render(gc.getInput(), g);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if(back.clicked()){
            back.release();
            sbg.enterState(MenuState.ID);
        }
    }
    
}
