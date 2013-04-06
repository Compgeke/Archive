/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game.state;

import java.util.ArrayList;
import java.util.Random;
import kenta.game.GameCollision;
import kenta.game.MainGame;
import kenta.game.cache.ImageCache;
import kenta.game.cutscene.BasicCutscene;
import kenta.game.cutscene.CutsceneGroup;
import kenta.game.object.RainDrop;
import kenta.game.object.Sign;
import kenta.game.logic.Backdrop;
import kenta.game.logic.Camera;
import kenta.game.logic.MapUtil;
import kenta.game.logic.Score;
import kenta.game.logic.SoundWrapper;
import kenta.game.logic.Timer;
import kenta.game.object.Player;
import kenta.game.object.Policemen;
import kenta.game.object.Portal;
import kenta.game.object.Purse;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Sactual
 */
public class GameplayState extends BasicGameState{
    public static final int ID = 1;
    public static final int TOTAL_MAPS = LevelSelectState.countMaps();
    private static final int MAX_PROJECTILES = 20;
    private World world;
    private Backdrop b;
    private Camera c;
    private CutsceneGroup cutscenes;
    private TiledMap map;
    private MapUtil mapUtil;
    private Player player;
    private SoundWrapper swrapper;
    private boolean soundOn = true, paused = false, started = false, weather = false, timed = false, boss = false;
    private ArrayList<Purse> projectiles = new ArrayList<Purse>();
    private static int CURRENT_MAP = -1;
    private RainDrop[] drops;
    private ArrayList<Policemen> mobs = new ArrayList<Policemen>();
    private Timer timer = new Timer();
    private int timeLimit, spawnTime;
    private ArrayList<StaticBody> mobBarriers = new ArrayList<StaticBody>();

    @Override
    public int getID() {
        return ID;
    }
    
    public static int getCurrentMap(){
        return CURRENT_MAP;
    }
    
    public static void setCurrentMap(int map){
        CURRENT_MAP = map-1;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Player.SCORE = new Score();
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        if(Player.SCORE.shouldReset())Player.SCORE.resetAll();
        TransitionState.setEndOfGame(false);
        initializeRain(900);
        loadNextMap(gc);
        started = true;
        Player.setLife(Player.MAX_LIFE);
    }
    
    @Override
    public void leave(GameContainer gc, StateBasedGame sbg){
        started = false;
        mobs.clear();
        projectiles.clear();
        mobBarriers.clear();
    }
    
    public void reloadPlayer() throws SlickException {
        this.player = new Player(getWorld(), 50, 50, 32, 49, "Mom", this.swrapper);
        ArrayList<Image> walkSprites = new ArrayList<Image>();
        for (int i = 0; i < 4; i++) {
            walkSprites.add(new Image("res/player/walk/" + i + ".png"));
        }
        this.getPlayer().init(new Image("res/player/stand/0.png"), walkSprites);
        // TODO seperate FileIO class to read in cutscenes from a file.
        cutscenes = new CutsceneGroup();
        if (CURRENT_MAP == 0) {
            cutscenes.add(new BasicCutscene(getPlayer(), "Oh dear, it seems that Indonesia's economy took a major downfall\r\nrecently."));
            cutscenes.add(new BasicCutscene(getPlayer(), "Perhaps it is time to move to a new country, somewhere much more\r\nrich in resources, and somewhere much more greater economy-wise."));
            cutscenes.add(new BasicCutscene(getPlayer(), "Lets see... (checks online for a great place to start her\r\ntailoring company)"));
            cutscenes.add(new BasicCutscene(getPlayer(), "Aha! I just got the perfect location, time to head towards Hong Kong.\r\nCome on kids! Lets go!"));
        } else if (CURRENT_MAP == 1) {
            cutscenes.add(new BasicCutscene(getPlayer(), "Phew, I finally managed to arrive with about 15 minutes to spare\r\nbefore the flight."));
            cutscenes.add(new BasicCutscene(getPlayer(), "(checks bag) Mm, lotion, clothes, towels, money, passp- OH NO!\r\nMy passport has expired! Oh what do I do! Its too late to go to the\r\nimmigration office..."));
            cutscenes.add(new BasicCutscene(getPlayer(), "In that case I will have to force myself into Hong Kong...\r\nTime to get through the police. (Instruction: Stack bags to form\r\n platforms to sneak through the police.)"));
        } else if (CURRENT_MAP == 2) {
            cutscenes.add(new BasicCutscene(getPlayer(), "Wow, that was quite... hard. Ugh, theres no end to the guards..\r\nI hope I make through this in one piece."));
            cutscenes.add(new BasicCutscene(getPlayer(), "(Tip that will scare you in your sleep: Some platforms are\r\nFAKE.)"));
        } else if (CURRENT_MAP == 3) {
            cutscenes.add(new BasicCutscene(getPlayer(), "At last! I have finally made it to the flight, I just have to\r\nget through these guards, and I can say hello\r\n to Hong Kong!"));
            cutscenes.add(new BasicCutscene(getPlayer(), "(Tip: Do a falcon punch on the guards.)"));
        } else if (CURRENT_MAP == 4) {
            cutscenes.add(new BasicCutscene(getPlayer(), "Finally... after all this time, I managed to reach Hong Kong."));
            cutscenes.add(new BasicCutscene(getPlayer(), "Now I can live through my families tradition and continue\r\nour tailoring company."));
            cutscenes.add(new BasicCutscene(getPlayer(), "All I have to do is to hide from the guards looking for me."));
        }
        cutscenes.play();
        this.getWorld().add(this.getPlayer().getBody());
        this.getPlayer().setWorld(this.getWorld());
        this.getPlayer().setJumpPower(20000.0F);
        this.getPlayer().setVelocity(10000.0F);
    }
    
    public void loadSelectedMap(GameContainer gc, int i) throws SlickException{
        CURRENT_MAP = i-1;
        loadNextMap(gc);
    }
    
    public void reloadMap(GameContainer gc) throws SlickException {
        CURRENT_MAP--;
        loadNextMap(gc);
    }

    public void loadNextMap(GameContainer gc) throws SlickException {
        CURRENT_MAP++;        
        mobs.clear();
        projectiles.clear();
        mobBarriers.clear();
        spawnTime = 0;
        this.world = new World(new Vector2f(0.0F, 750.0F), 20);
        this.world.addListener(new GameCollision());
        this.swrapper = new SoundWrapper();
        this.map = new TiledMap("res/map/Map" + CURRENT_MAP + ".tmx");
        this.mapUtil = new MapUtil(map, getWorld());
        this.b = new Backdrop(mapUtil.getMapWidth(), mapUtil.getMapHeight(), 800, 800);
        for (int i = 1; i <= Integer.decode(map.getMapProperty("backdrops", "0")); i++) {
            this.b.add(new BigImage("res/map/Map" + CURRENT_MAP + "/BD_" + i + ".png"));
        }
        weather = Boolean.parseBoolean(map.getMapProperty("weather", "false"));
        boss = Boolean.parseBoolean(map.getMapProperty("boss", "false"));
        timeLimit = Integer.parseInt(map.getMapProperty("time", "0"));
        if(timeLimit>0)timed = true;
        else timed = false;
        timer.reset();
        Player.SCORE.resetCurrentScore();
        reloadPlayer();
        this.c = new Camera(gc, map, mapUtil, getPlayer(), b);
        this.mapUtil.buildMap();
        initSigns();
        getCamera().update(gc, 1);
        if(timed)timer.start();
    }

    public void addProjectile(Purse p) {
        if(projectiles.size() == MAX_PROJECTILES){
            projectiles.get(0).remove();
            getCamera().removeEntity(projectiles.get(0));
            projectiles.remove(0);
        }
        for(Body b: mobBarriers){
            p.getBody().addExcludedBody(b);
        }
        projectiles.add(p);
        getCamera().addEntity(p);
        Player.SCORE.adjustPurse();
    }

    private void initSigns() throws SlickException {
        for (int i = 0; i < map.getObjectGroupCount(); i++) {
            for (int j = 0; j < map.getObjectCount(i); j++) {
                if (map.getObjectName(i, j).startsWith("Sign")) {
                    Image sign = ImageCache.SIGN;
                    getCamera().addEntity(new Sign(sign, map.getObjectX(i, j), map.getObjectY(i, j), sign.getWidth(), sign.getHeight(), map.getObjectName(i, j)));
                }
                if (map.getObjectName(i, j).equals("police")) {
                    Image stand = ImageCache.POLICE_STAND;
                    Policemen police = new Policemen(getWorld(), map.getObjectX(i, j), map.getObjectY(i, j), 30, 48, swrapper, mapUtil);
                    //police.getBody().addExcludedBody(player.getBody());
                    police.init(stand, ImageCache.POLICE_WALK);
                    getWorld().add(police.getBody());
                    getCamera().addEntity(police);
                    mobs.add(police);
                    for (Policemen testing : mobs) {
                        testing.getBody().addExcludedBody(police.getBody());
                    }
                }
                if (map.getObjectName(i, j).equals("portal")) {
                    Image portal = ImageCache.PORTAL;
                    Portal porta = new Portal(portal, getWorld(), map.getObjectX(i, j), map.getObjectY(i, j));
                    porta.getBody().setMoveable(false);
                    getWorld().add(porta.getBody());
                    getCamera().addEntity(porta);
                }
                if (map.getObjectName(i, j).equals("barrier")) {
                    StaticBody bar = new StaticBody("Barrier_" + i + "_" + j,new Box(2,2));
                    bar.setRestitution(1.0f);
                    bar.setPosition(map.getObjectX(i,j), map.getObjectY(i,j));
                    mobBarriers.add(bar);
                    bar.addExcludedBody(player.getBody());
                    getWorld().add(bar);
                }
            }
        }
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if(started){
            if(!timer.isPaused()){
                if(paused)timer.pause();
            }else{
                if(!paused)timer.resume();
            }
            getCamera().render(gc, g);
            
            if(weather){
                g.setColor(new Color(188.0f, 227.0f, 229.0f, 0.3f));
                for (int i = 0; i < drops.length - 1; i++) {
                    g.draw(drops[i]);
                }
            }
            cutscenes.render(gc, g);
            
            g.setColor(Color.red.darker());
            if(timed){
                g.drawString("Flight leaves in: " + timer.getRemainingTimeMMSS(timeLimit), 390, ImageCache.PLAYER_LIFE_HEART.getHeight() - 15);
                Player.SCORE.adjustTime(timeLimit - (int)timer.getElapsedTimeSecs());
            }else{
                Player.SCORE.adjustTime(0);
            }
            Player.SCORE.adjustLife();            
            g.pushTransform();
            //g.translate(0, ImageCache.PLAYER_LIFE_HEART.getHeight());
            g.translate(400, 15);
            g.drawString("Total score: " + Integer.toString(Player.SCORE.getTotalScore()), 0, 0);
            g.translate(0, 15);
            g.drawString("Current score: " + Integer.toString(Player.SCORE.getCurrentScore()), 0, 0);
            g.popTransform();

            for (int i = 0; i < Player.MAX_LIFE; i++){
                if(i < Player.getLife()){
                    g.drawImage(ImageCache.PLAYER_LIFE_HEART, (i+9)*ImageCache.PLAYER_LIFE_HEART.getWidth(), 0);
                }else{
                    g.drawImage(ImageCache.PLAYER_LIFE_MISSING, (i+9)*ImageCache.PLAYER_LIFE_MISSING.getWidth(), 0);
                }
            }

            if(!paused){
                getWorld().step();
            }else{
                g.setColor(new Color(0.0f,0.0f,0.0f,0.5f));
                g.fillRect(0, 0, MainGame.WIDTH, MainGame.HEIGHT);
            }
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if(timed && timer.getElapsedTimeSecs() >= timeLimit && !Player.isDead()){
            Player.loseLife(Player.TIME);
        }
        if(player.isBelowMap() && !Player.isDead()){
            Player.loseLife(Player.FELL);
        }
        if(Player.isDead()){
            if(!Player.isDeathStarted()){
                Player.startDeath();
                cutscenes = new CutsceneGroup();
                cutscenes.add(new BasicCutscene(getPlayer(), Player.DEATH_CUT_SCENES[Player.getCauseOfDeath()]));
                cutscenes.play();
            }else if(!cutscenes.isPlaying()){
                Player.revive();
                if(Player.isGameOver()){
                    paused = true;
                    Player.restart();
                    sbg.enterState(MenuState.ID, new FadeOutTransition(), new FadeInTransition());
                }else{
                    reloadMap(gc);
                }
            }
        }
        getPlayer().preUpdate(i);
        
        Input input = gc.getInput();
        if (input.isKeyPressed(Input.KEY_P) && !cutscenes.isPlaying()){
                paused = !paused;
        }
        if (!paused) {
            this.getPlayer().setMoving(false);
            if ((this.getPlayer().isOnGround()) && (!this.player.isJumping())
                    && (input.isKeyPressed(Input.KEY_UP))) {
                this.getPlayer().jump(this.getPlayer().getJumpPower());
            }
            if ((input.isKeyDown(Input.KEY_RIGHT)) && (this.getPlayer().getX() < mapUtil.getMapWidth())) {
                this.getPlayer().walkRight(i);
            }
            if ((input.isKeyDown(Input.KEY_LEFT)) && (this.getPlayer().getX() > 0.0F)) {
                this.getPlayer().walkLeft(i);
            }
            if (input.isKeyPressed(Input.KEY_SPACE)) {
                getPlayer().shootBullet();
            }
            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                gc.exit();
            }
            if(boss && mobs.size() < 30){
                spawnTime += i;
                if(spawnTime >= 2000){
                    spawnTime -= 2000;
                    for(int t = 0; t < 2; t++){
                        Image stand = ImageCache.POLICE_STAND;
                        Policemen police = new Policemen(getWorld(), new Random().nextFloat()*map.getWidth()*map.getTileWidth(), 0, 30, 48, swrapper, mapUtil);
                        police.init(stand, ImageCache.POLICE_WALK);
                        getWorld().add(police.getBody());
                        getCamera().addEntity(police);
                        mobs.add(police);
                        for (Policemen testing : mobs) {
                            testing.getBody().addExcludedBody(police.getBody());
                        } 
                    }
                }               
            }
            getCamera().update(gc, i);
        }
        getPlayer().update(i);
        cutscenes.update(i);
        if(weather){
            for (int z = 0; z < drops.length - 1; z++) {
                drops[z].setY(drops[z].getY() + 4.0f);
                drops[z].setX(drops[z].getX() - 1.0f);
                if (drops[z].getY() > 768) {
                    drops[z].setX((float) ((Math.random() * 2000) + 30));
                    drops[z].setY((float) ((Math.random() * 5)));
                }
            }
        }
    }

    void initializeRain(int num_of_drops) {
        drops = new RainDrop[num_of_drops];
        for (int i = 0; i < drops.length - 1; i++) {
            drops[i] = new RainDrop((int) (Math.random() * 10 + 4));
            drops[i].setX((float) ((Math.random() * 2000) + 30));
            drops[i].setY((float) ((Math.random() * 750)));
        }
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the world
     */
    public World getWorld() {
        return world;
    }
    
    /**
     * @return the map util
     */
    public MapUtil getMapUtil(){
        return mapUtil;
    }

    /**
     * @return the c
     */
    public Camera getCamera() {
        return c;
    }
}
