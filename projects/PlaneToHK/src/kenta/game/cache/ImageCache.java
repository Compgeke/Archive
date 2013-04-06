/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game.cache;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Sactual
 */
public class ImageCache {

    public static Image DIALOG, PLAYER_BULLET, GRASS_TILESET, PORTAL, SIGN, PLAYER_STAND, POLICE_STAND, PLAYER_LIFE_HEART, PLAYER_LIFE_MISSING,
            MENU_BACKGROUND, NEW_UP, NEW_OVER, NEW_DOWN, EXIT_UP, EXIT_OVER, EXIT_DOWN, SELECT_UP, SELECT_OVER, SELECT_DOWN, BACK_UP, BACK_OVER, BACK_DOWN,
            HELP_UP, HELP_OVER, HELP_DOWN;
    public static ArrayList<Image> POLICE_WALK = new ArrayList<Image>();

    public static void loadImages() throws SlickException {
        DIALOG = new Image("res/ui/Dialog.png");
        PLAYER_BULLET = new Image("res/player/Bullet.png");
        POLICE_STAND = new Image("res/mob/police/stand/0.png");
        GRASS_TILESET = new Image("res/map/tiles/GrassTile.png");
        SIGN = new Image("res/sign/Sign0.png");
        PORTAL = new Image("res/ui/portal.png");
        PLAYER_LIFE_HEART = new Image("res/ui/life.png");
        PLAYER_LIFE_MISSING = new Image("res/ui/life2.png");
        for (int z = 0; z < 3; z++) {
            POLICE_WALK.add(new Image("res/mob/police/walk/" + z + ".png"));
        }
        
        MENU_BACKGROUND = new Image("res/menu/background.png");
        
        NEW_UP = new Image("res/menu/button/NewGameUp.png");
        NEW_OVER = new Image("res/menu/button/NewGameOver.png");
        NEW_DOWN = new Image("res/menu/button/NewGameDown.png");
        
        EXIT_UP = new Image("res/menu/button/ExitUp.png");
        EXIT_OVER = new Image("res/menu/button/ExitOver.png");
        EXIT_DOWN = new Image("res/menu/button/ExitDown.png");
        
        SELECT_UP = new Image("res/menu/button/LevelSelectUp.png");
        SELECT_OVER = new Image("res/menu/button/LevelSelectOver.png");
        SELECT_DOWN = new Image("res/menu/button/LevelSelectDown.png");
        
        BACK_UP = new Image("res/menu/button/BackUp.png");
        BACK_OVER = new Image("res/menu/button/BackOver.png");
        BACK_DOWN = new Image("res/menu/button/BackDown.png");
        
        HELP_UP = new Image("res/menu/button/HelpUp.png");
        HELP_OVER = new Image("res/menu/button/HelpOver.png");
        HELP_DOWN = new Image("res/menu/button/HelpDown.png");
    }
}
