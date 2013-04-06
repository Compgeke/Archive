package FeathersAreUs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.EnumSet;

import org.rsbuddy.tabs.Inventory;
import util.Trade;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Keyboard;
import com.rsbuddy.script.methods.Menu;
import com.rsbuddy.script.methods.Mouse;
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

@Manifest(authors = "Kieve", version = 1.0, name = "Feather trader", description = "It collects feathers!")
public class Banker extends ActiveScript implements PaintListener, MessageListener{
	private static String status = "Unknown";
	private static final int
		stairsID = 45482,
		goldAmmount = 6466,
		goldID = 995;
	
	private static final Tile
		SW = new Tile(3191,3251),
		NE = new Tile(3198,3255),
		destination = new Tile(3193,3254);
	private static final Area store = new Area(SW,NE);
	
	private static final Filter<Player> filter = new Filter<Player>(){
		public boolean accept(Player Player){
			if(Player.getName() != null && Player.getName().contains(tradeTarget) && !Player.getName().equals(Players.getLocal().getName()))
				return true;
			return false;
		}
	};
	
	private static String tradeTarget;
	private static boolean shouldTrade;
	private static long startTime;
	
	public boolean onStart(){
		startTime = System.currentTimeMillis();
		return true;
	}
	
	private int calcPerHour(int total){
    	return (int)(1000*60*60* (double)total / (double)(System.currentTimeMillis()-startTime));
    }

	public int loop() {
		status = "Gold: " + (Inventory.contains(goldID)?Inventory.getItem(goldID).getStackSize():"None!");
		if(Objects.getNearest(stairsID)!=null){
			Objects.getNearest(stairsID).interact("Climb-down Stairs");
		}else if(!Players.getLocal().getLocation().equals(destination)){
			if(!Players.getLocal().isMoving()){
				if(Walking.getTileOnMap(destination).isOnScreen()){
					Walking.getTileOnMap(destination).interact("Walk here");
				}else{
					Walking.findPath(destination).traverse(EnumSet.of(Path.TraversalOption.HANDLE_RUN, Path.TraversalOption.SPACE_ACTIONS));
				}
			}
		}else {
			if(Trade.isOpen()){
				status = "Trade is open";
				if(Trade.isTradeWindowOpen()){
					if(!Trade.offeringContains(goldID)){
						status = "Offering the gold";
						if(Trade.getOfferingItemIdAt(0) != goldID){
							Item temp = Inventory.getItem(goldID);
							Mouse.click(temp.getComponent().getAbsLocation().x + 15, temp.getComponent().getAbsLocation().y + 15,10,10, false);
							Menu.click("Offer-X");
							//Inventory.getItem(goldID).interact("Offer-X");
							sleep(2000);
							if(Trade.isOpen())
								Keyboard.sendTextInstant(""+goldAmmount, false);
							sleep(500,1000);
							if(!Trade.offeringContains(goldID) && Trade.isOpen()){
								Keyboard.sendTextInstant("",true);
							}
							sleep(1000);
						}
						
					}else if(Trade.getOfferingStackSizeAt(0) != 6466){
						status = "There is a gold ammount, but it's not 6466";
						Trade.getCloseComponent().click(true);
					}else{
						status = "Accepting the gold ammount of 6466";
						Trade.getAcceptComponent().click(true);
					}
				}else if(Trade.isConfirmWindowOpen()){
					status = "Confirming the trade";
					Trade.getAcceptComponent().click(true);
					sleep(1000,2000);
				}else{
					status = "Something is wrong";
				}
				sleep(200,300);
			}else if(shouldTrade){
				shouldTrade = false;
				Player temp = Players.getNearest(filter);
				if(temp != null){
					temp.interact("Trade with "+tradeTarget);
				}
				sleep(1000,2000);
			}else{
				//Game.setChatOption(Game.CHAT_OPTION_TRADE, Game.ChatMode.VIEW);
				if(!checkChat()){
					Keyboard.sendTextInstant("\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f\b\f",false);
				}
				sleep(500);
			}
		}
		sleep(10,15);
		return 0;
	}
	public void messageReceived(MessageEvent e) {
		//if(e.getId() == MessageEvent.MESSAGE_TRADE_REQ){
			if(e.getSender().contains(("CENSORED"))){
				tradeTarget = e.getSender();
				shouldTrade = true;
			}
		//}
	}

	public void onRepaint(Graphics g){
		Graphics2D pen = (Graphics2D)g;
		pen.setColor(Color.ORANGE.darker());
		pen.drawString("Status: " + status, 8, 30);
	}
	
	private boolean checkChat(){
		String temp = Widgets.get(137).getComponent(55).getText();
		temp = temp.replace(Players.getLocal().getName() + "<img=3>: <col=0000ff>", "");
		return temp.equals("");
	}
}
