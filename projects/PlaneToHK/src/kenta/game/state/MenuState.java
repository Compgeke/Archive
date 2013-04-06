package kenta.game.state;

import kenta.game.cache.ImageCache;
import kenta.game.menu.Button;
import kenta.game.object.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * 
 * @author Kevin Nash
 */
public class MenuState extends BasicGameState{
    public static final int ID = 0;
    private Button newGame, levelSelect, help, exit;

    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        ImageCache.loadImages();
        newGame = new Button(300, ImageCache.NEW_UP, ImageCache.NEW_OVER, ImageCache.NEW_DOWN);
        levelSelect = new Button(350, ImageCache.SELECT_UP, ImageCache.SELECT_OVER, ImageCache.SELECT_DOWN);
        help = new Button(400, ImageCache.HELP_UP, ImageCache.HELP_OVER, ImageCache.HELP_DOWN);
        exit = new Button(450, ImageCache.EXIT_UP, ImageCache.EXIT_OVER, ImageCache.EXIT_DOWN);
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        Input i = container.getInput();
	g.drawImage(ImageCache.MENU_BACKGROUND,0,0);
	newGame.render(i, g);
        levelSelect.render(i, g);
        help.render(i, g);
	exit.render(i, g);
    }

    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        if(newGame.clicked()){
            newGame.release();
            Player.SCORE.setReset(true);
            GameplayState.setCurrentMap(0);
            game.enterState(GameplayState.ID, new FadeOutTransition(), new FadeInTransition());
        }
        if(levelSelect.clicked()){
            levelSelect.release();
            game.enterState(LevelSelectState.ID);
        }
        if(help.clicked()){
            help.release();
            game.enterState(HelpState.ID);
        }
        if(exit.clicked()){
            container.exit();
        }
    }

    public int getID() {
        return ID;
    }
}
