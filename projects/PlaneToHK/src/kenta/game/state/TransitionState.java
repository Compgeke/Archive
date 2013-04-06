package kenta.game.state;

import kenta.game.MainGame;
import kenta.game.object.Player;
import org.newdawn.slick.Color;
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
public class TransitionState extends BasicGameState{
    public static final int ID = 4;
    private static boolean END_OF_GAME;
    
    @Override
    public int getID() {
        return ID;
    }
    
    public static boolean isEndOfGame(){
        return END_OF_GAME;
    }
    
    public static void setEndOfGame(boolean b){
        END_OF_GAME = b;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setColor(Color.black);
        g.fillRect(0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        g.setColor(Color.orange);
        if(!END_OF_GAME){
            g.drawString("Score: " + Player.SCORE.getTotalScore()
                    + "\nPress Enter to continue.", 100, 100);
            
        }else{
            g.drawString("Congratulations! You won! Your total score is " + Player.SCORE.getTotalScore() + " points!"
                    + "\nPress Enter to return to the main menu.",100,100);
        }
    }

    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if(gc.getInput().isKeyPressed(Input.KEY_RETURN) && gc.getInput().isKeyDown(Input.KEY_RETURN)){
            System.out.println("Yes, key is pressed.");
            if(!END_OF_GAME){
                sbg.enterState(GameplayState.ID, new FadeOutTransition(), new FadeInTransition());
            }else{
                sbg.enterState(MenuState.ID, new FadeOutTransition(), new FadeInTransition());
            }
        }
    }    
}
