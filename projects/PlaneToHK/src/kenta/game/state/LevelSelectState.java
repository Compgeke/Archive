package kenta.game.state;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import kenta.game.cache.ImageCache;
import kenta.game.menu.Button;
import kenta.game.menu.NumberedButton;
import kenta.game.object.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 *
 * @author Kevin Nash
 */
public class LevelSelectState extends BasicGameState {
    public static final int ID = 2;
    private static int MAX_LEVEL;
    private static final String PATH = System.getProperty("user.home") +"/ThePlaneToHK/save.txt";
    private static final String DIR = System.getProperty("user.home") +"/ThePlaneToHK/";
    private NumberedButton[] buttons;
    private Button back;
    
    @Override
    public int getID() {
        return ID;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        loadMaxLevel();
        back = new Button(50, 300, ImageCache.BACK_UP, ImageCache.BACK_OVER, ImageCache.BACK_DOWN);
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame sbg){
        adjustButtons();
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawImage(ImageCache.MENU_BACKGROUND, 0, 0);
        if(buttons != null){
            for (int i = 0; i < buttons.length; i++){
                buttons[i].render(gc.getInput(), g);
            }   
        }
        
        back.render(gc.getInput(), g);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        for(int i = 0; i < buttons.length; i++){
            if(buttons[i].clicked()){
                buttons[i].release();
                GameplayState.setCurrentMap(buttons[i].getMap());
                Player.SCORE.setReset(true);
                sbg.enterState(GameplayState.ID,new FadeOutTransition(), new FadeInTransition());
            }
        }
        
        if(back.clicked()){
            back.release();
            sbg.enterState(MenuState.ID);
        }
    }
    
    private void adjustButtons(){
        buttons = new NumberedButton[MAX_LEVEL];
        for(int i = 0; i < MAX_LEVEL; i++){
            buttons[i] = new NumberedButton(50 + 115*i, 350, i+1);
        }
    }
    
    public static int getMaxLevel(){
        return MAX_LEVEL;
    }
    
    public static void setMaxLevel(int max){
        MAX_LEVEL = max;
        saveMaxLevel();
    }
    
    public static void increaseMaxLevel(){
        MAX_LEVEL++;
        saveMaxLevel();
    }
    
    public static boolean loadMaxLevel(){
        File f = new File(PATH);
        if(f.exists()){
            try {
                MAX_LEVEL = Integer.parseInt(new BufferedReader(new FileReader(PATH)).readLine());
                return true;
            } catch (IOException ex) {
                System.out.println("Error reading level data.");
                ex.printStackTrace();
                MAX_LEVEL = 1;
                return false;
            }
        }else{
            MAX_LEVEL = 1;
            return false;
        }
    }
    
    public static boolean saveMaxLevel(){
        File f = new File(PATH);
        if(f.exists()){
            try {
                writeLevelData();
                return true;
            } catch (IOException ex) {
                System.out.println("Error saving level data");
                return false;
            }
        }else{
            try {
                f = new File(DIR);
                if(!f.exists())f.mkdir();
                f = new File(PATH);
                f.createNewFile();
                writeLevelData();
                return true;
            } catch (IOException ex) {
                System.out.println("Error creating files and writing level data.");
                return false;
            }
        }
    }
    
    public static void writeLevelData() throws IOException{
        FileWriter fw = new FileWriter(PATH);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Integer.toString(MAX_LEVEL));
        bw.flush();
        bw.close();
        fw.close();
    }
    
    public static int countMaps(){
        File f = new File("res/map");
        return f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String suffix = ".tmx";
                if( pathname.getName().toLowerCase().endsWith(suffix) ) {
                    return true;
                }
                return false;
            }
        }).length;
    }
}
