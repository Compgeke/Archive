/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game.cutscene;

import java.util.HashMap;
import java.util.Map;
import kenta.game.MainGame;
import kenta.game.state.GameplayState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Sactual
 */
public class CutsceneGroup {

    private Map<Integer, BasicCutscene> cutscenes = new HashMap<Integer, BasicCutscene>();
    private int currentCutscene = 0;
    private boolean playing = false;
    private BasicCutscene currentScene;

    public void play() {
        if (cutscenes.size() > 0) {
            currentCutscene = 1;
            currentScene = cutscenes.get(currentCutscene);
            ((GameplayState)MainGame.getInstance().getState(GameplayState.ID)).setPaused(true);
            playing = true;
        } else {
            System.out.println("No cutscenes available.");
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void add(BasicCutscene cutscene) {
        System.out.println(cutscenes.size());
        cutscenes.put(cutscenes.size() + 1, cutscene);
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        if (playing && cutscenes.size() > 0 && currentScene != null) {
            currentScene.render(gc, g);
        }
    }

    public void update(int delta) throws SlickException {
        if (playing && cutscenes.size() > 0 && currentScene != null) {
            if (currentScene.isFinished()) {
                if (currentCutscene < cutscenes.size()) {
                    currentCutscene++;
                    currentScene = cutscenes.get(currentCutscene);
                } else {
                    currentCutscene = 0;
                    currentScene = null;
                    playing = false;
                    ((GameplayState)MainGame.getInstance().getState(GameplayState.ID)).setPaused(false);
                }
            } else {
                currentScene.update(delta);
            }
        }
    }
}
