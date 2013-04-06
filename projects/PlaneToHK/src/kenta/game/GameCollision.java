/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game;

import kenta.game.object.Player;
import kenta.game.object.Policemen;
import kenta.game.object.Portal;
import kenta.game.state.GameplayState;
import kenta.game.state.LevelSelectState;
import kenta.game.state.TransitionState;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.CollisionListener;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 *
 * @author Sactual
 */
public class GameCollision implements CollisionListener {

    public void collisionOccured(CollisionEvent ce) {
        if (ce.getBodyA().getUserData() instanceof Player
                && ce.getBodyB().getUserData() instanceof Policemen) {
            if(!Player.isDead()){
                Player.loseLife(Player.POLICE);
            }
        }
        if (ce.getBodyA().getUserData() instanceof Player && ce.getBodyB().getUserData() instanceof Portal) {
            try {
                if(GameplayState.getCurrentMap()+1 < GameplayState.TOTAL_MAPS){
                    if(GameplayState.getCurrentMap()+1 == LevelSelectState.getMaxLevel()){
                        System.out.println("Increasing max level.");
                        LevelSelectState.increaseMaxLevel();
                    }
                }else{
                    TransitionState.setEndOfGame(true);
                }
                Player.SCORE.updateTotalScore();
                Player.SCORE.setReset(false);
                MainGame.getInstance().enterState(TransitionState.ID, new FadeOutTransition(), new FadeInTransition());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
