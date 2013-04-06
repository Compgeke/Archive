/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game;

import kenta.game.state.GameplayState;
import kenta.game.state.HelpState;
import kenta.game.state.LevelSelectState;
import kenta.game.state.MenuState;
import kenta.game.state.TransitionState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Sactual
 */
public class MainGame extends StateBasedGame {
    public static MainGame instance;
    public static final int WIDTH = 800, HEIGHT = 600;

    public static MainGame getInstance() {
        if (instance == null) instance = new MainGame();
        return instance;
    }

    public MainGame() {
        super("The Plane to HK");
    }

    public void initStatesList(GameContainer gameContainer)
            throws SlickException {
        gameContainer.setTargetFrameRate(60);
        gameContainer.setVSync(true);
        addState(new MenuState());
        addState(new LevelSelectState());
        addState(new HelpState());
        addState(new GameplayState());
        addState(new TransitionState());
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(MainGame.getInstance());
            app.setDisplayMode(WIDTH, HEIGHT, false);
            app.setShowFPS(true);
            app.setTargetFrameRate(60);
            app.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
