package FeathersAreUs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.SwingUtilities;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.widgets.Lobby;
import org.rsbuddy.widgets.Store;

import util.Trade;

import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Keyboard;
import com.rsbuddy.script.methods.Menu;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Path;
import com.rsbuddy.script.wrappers.Player;
import com.rsbuddy.script.wrappers.Tile;
import com.rsbuddy.script.wrappers.Widget;

@Manifest(authors = "Kieve", version = 1.0, name = "Feather Buyer", description = "It buys feathers!")
public class Trader extends ActiveScript implements PaintListener, ActionListener{
	private static String status = "Unknown";
	
	private static final String
		banker = "CENSORED",
		userStart = "CENSORED",
		userEnd	 =	"CENSORED",
		password  =	"CENSORED";
	private static final int
		featherID = 314,
		goldID = 995,
		npcID = 8864,
		stairsID = 45482,
		loginWidget = 596,
		wrongPassComponent = 14,
		tryAgainComponent = 68,
		userComponent = 73,
		passComponent = 79,
		loginComponent = 45,
		storeWidget = 620,
		closeComponent = 18;

	private static final Tile
		SW = new Tile(3191,3251),
		NE = new Tile(3197,3255),
		destination = new Tile(3194,3254);
	private static final Area store = new Area(SW,NE);
    ////////////////////////////////////////////////////////////////////////
	private static int userNumber = 1139, endNumber = 0;
	////////////////////////////////////////////////////////////////////////
	private static boolean hasTraded = false, firstTime = true, started = false, feathersLeft = true, loggingOut = false;
	private static Player bankPlayer;
	private static OptionFrame options;
	private static Trader instance;
	
	private static final Filter<Player> filter = new Filter<Player>(){
		public boolean accept(Player Player){
			if(Player.getName() != null && Player.getName().contains(banker) && !Player.getName().equals(Players.getLocal().getName()))
				return true;
			return false;
		}
	};
	
	private static enum ScriptState {
		LOGGING_IN,
		WALKING_TO_TILE,
		PURCHASING,
		TRADING,
		LOGGING_OUT;
	};
	
	public boolean onStart(){
		instance = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				options = new OptionFrame(instance);
				options.setResizable(false);
				options.setLocationRelativeTo(null);
				options.setVisible(true);
			}
		});
		return true;
	}
	
	private static final ScriptState getState(){
		if(!Game.isLoggedIn()){
			return ScriptState.LOGGING_IN;
		}else if(!Players.getLocal().getLocation().equals(destination)){
			return ScriptState.WALKING_TO_TILE;
		}else if(!feathersLeft && !Inventory.contains(featherID) && !Trade.isOpen()){
			return ScriptState.LOGGING_OUT;
		}else if(!hasTraded && ((Inventory.contains(goldID) && feathersLeft) || Widgets.get(storeWidget).isValid())){
			return ScriptState.PURCHASING;
		}else if(!hasTraded || Trade.isOpen()){
			return ScriptState.TRADING;
		}else if(Game.isLoggedIn()){
			return ScriptState.LOGGING_OUT;
		}else{
			return null;
		}
	}
	
	public int loop() {
		if(started){
			ScriptState state = getState();
			switch(state){
				case LOGGING_IN:
					if(userNumber == 3)
						userNumber++;
					hasTraded = false;
					feathersLeft = true;
					firstTime = true;
					if(loggingOut){
						loggingOut = false;
						log("The last was " + userNumber);
						userNumber++;
						log("Next user number is " + userNumber);
					}
					if(!Widgets.get(loginWidget).getComponent(wrongPassComponent).getText().equals("")){
						Widgets.get(loginWidget).getComponent(tryAgainComponent).click(true);
					}
					if(Lobby.isAlertOpen()){
						if(Lobby.getAlertText().contains("disabled")){
							log(userNumber + " is banned!!");
							userNumber++;
						}
						Lobby.closeAlert();
					}
					if(Widgets.get(906).getComponent(247).getText().equals("Back")){
						Widgets.get(906).getComponent(247).click(true);
						sleep(500);
					}
					if(Lobby.isValid()){
						status = "Hitting enter game";
						Lobby.clickPlay();
						sleep(2000);
					}else if(Widgets.get(loginWidget).isValid()){
						Widget w = Widgets.get(loginWidget);
						if(!w.getComponent(userComponent).getText().equals(userStart + userNumber + userEnd)){
							if(w.getComponent(userComponent).getText().equals("")){
								status = "Entering the correct username";
								w.getComponent(userComponent).click(true);
								sleep(200,300);
								Keyboard.sendTextInstant(userStart + userNumber + userEnd, false);
								sleep(500);
							}else{
								status = "Removing the wrong input";
								w.getComponent(userComponent).click(true);
								sleep(200,300);
								Keyboard.sendTextInstant("\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f",false);
							}
						}else{
							if(!w.getComponent(passComponent).getText().equals("")){
								status = "entering the password";
								w.getComponent(passComponent).click(true);
								sleep(200,300);
								Keyboard.sendTextInstant("\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f",false);
							}else{
								status = "Username is correct, entering pass and clicking login";
								w.getComponent(passComponent).click(true);
								sleep(200,300);
								Keyboard.sendTextInstant(password, false);
								w.getComponent(loginComponent).click(true);
							}
						}
					}
					break;
				case WALKING_TO_TILE:
					if(Store.isOpen()){
						Store.close();
					}
					if(Trade.isOpen()){
						Trade.getCloseComponent().click(true);
					}
					if(Objects.getNearest(stairsID)!=null){
						Objects.getNearest(stairsID).interact("Climb-down Stairs");
					}else if(!Players.getLocal().isMoving() || !store.contains(Players.getLocal().getLocation())){
						if(Walking.getTileOnMap(destination).isOnScreen()){
							Walking.getTileOnMap(destination).interact("Walk here");
						}else{
							Walking.setRun(true);
							Walking.findPath(destination).traverse();
						}
					}
					sleep(1000);
					break;
				case PURCHASING:
					if(Npcs.getNearest(npcID)!=null && Npcs.getNearest(npcID).isOnScreen() &&!Players.getLocal().isMoving()){
						if(Widgets.get(storeWidget).isValid()){
							if(Store.getItem(featherID) != null && Store.getItem(featherID).getStackSize()>=500){
								Store.getItem(featherID).interact("Buy 500 Feather");
								sleep(1000);
							}else{
								feathersLeft = false;
								Widgets.get(storeWidget).getComponent(closeComponent).click(true);
								sleep(1000);
							}
						}else{
							if(!Inventory.contains(featherID) && !Players.getLocal().isMoving()){
								Npcs.getNearest(npcID).interact("Trade Hank");
								sleep(1500);	
							}						
						}
					}
					break;
				case TRADING:
					if(Inventory.contains(featherID)){
						firstTime = false;
					}
					if(Trade.isOpen()){
						if(Trade.isTradeWindowOpen()){
							if(!Trade.offeringContains(featherID) && Inventory.contains(featherID)){
								Item temp = Inventory.getItem(featherID);
								Mouse.click(temp.getComponent().getAbsLocation().x + 15, temp.getComponent().getAbsLocation().y + 15,10,10, false);
								Menu.click("Offer-All");
								//Inventory.getItem(featherID).interact("Offer-All");
								sleep(1000);
							}else if(!Trade.offeringContains(goldID) && Inventory.contains(goldID)){
								Item temp = Inventory.getItem(goldID);
								Mouse.click(temp.getComponent().getAbsLocation().x + 15, temp.getComponent().getAbsLocation().y + 15,10,10, false);
								Menu.click("Offer-All");
								//Inventory.getItem(goldID).interact("Offer-All");
								sleep(1000);
							}else{
								Trade.getAcceptComponent().click(true);
							}
						}else if(Trade.isConfirmWindowOpen()){
							status = "Confirming!";
							Trade.getAcceptComponent().click(true);
							if(!firstTime){
								hasTraded = true;
							}
							sleep(2000,3000);
							if(!Trade.isOpen()){
								firstTime = false;
							}
						}
						sleep(200,300);
					}else if(store.contains(Players.getLocal().getLocation()) && !Players.getLocal().isMoving()){
						if((bankPlayer = Players.getNearest(filter)) != null  && !Players.getLocal().isMoving()){
							bankPlayer.interact("Trade with " + banker);
							sleep(6000);
						}
					}
					break;
				case LOGGING_OUT:
					if(!Trade.isOpen() && !Inventory.contains(featherID)){
						if(Game.getCurrentTab() != Game.TAB_LOGOUT){
							Game.openTab(Game.TAB_LOGOUT);
						}
						Game.logout(false);
						loggingOut = true;
						sleep(1000,2000);
						if(userNumber > endNumber){
							started = false;
							stop();
							break;
						}
					}else{
						hasTraded = false;
					}
					break;
			}
			sleep(10,15);
		}else{
			sleep(100,200);
		}
		return 0;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try{
			userNumber = Integer.parseInt(options.getStartField().getText());
			endNumber = Integer.parseInt(options.getEndField().getText());
			started = true;
			options.dispose();
		}catch(Exception e){
			
		}
	}
	
	public void onRepaint(Graphics g){
		Graphics2D pen = (Graphics2D)g;
		pen.setColor(Color.ORANGE.darker());
		pen.drawString("Status: " + status, 8, 30);
	}
}
