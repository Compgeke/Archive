/**
 * Author: Kevin Nash (Kieve)
 * Please give credit where it is due.
 */



package util;

import java.awt.Point;
import java.util.regex.Pattern;

import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.methods.Menu;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.util.Timer;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.Model;
import com.rsbuddy.script.wrappers.Player;
import com.rsbuddy.script.wrappers.Widget;

public class Trade {
	private static final short
		tradeWindowW = 335,
			tradeAcceptC = 16,
			tradeDeclineC = 18,
			tradeCloseC = 12,
			tradePartnerC = 15,
			tradeItemListLeftC = 30,
			tradeItemListRightC = 32,
		confirmWindowW = 334,
			confirmAcceptC = 21,
			confirmDeclineC = 22,
			confirmCloseC = 6,
			confirmPartnerC = 54,
			confirmItemListLeftC = 38,
			confirmItemListRightC = 49;
	private static final Point
		nullPoint = new Point(-1,-1),
		closedPoint = new Point(0,0);
	
	public static boolean isOpen(){
		if(isTradeWindowOpen() || isConfirmWindowOpen())
			return true;
		else
			return false;
	}
	
	public static boolean isTradeWindowOpen(){
		Point t = Widgets.get(tradeWindowW).getComponent(tradeAcceptC).getAbsLocation();
		if(!t.equals(nullPoint) && !t.equals(closedPoint))
			return true;
		return false;
	}
	public static boolean isConfirmWindowOpen(){
		Point c = Widgets.get(confirmWindowW).getComponent(confirmAcceptC).getAbsLocation();
		if(!c.equals(nullPoint) && !c.equals(closedPoint))
			return true;
		return false;
	}
	
	public static Widget getWidget(){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW);
		if(isConfirmWindowOpen())
			return Widgets.get(confirmWindowW);
		else
			return null;
	}
	
	public static Component getAcceptComponent(){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW).getComponent(tradeAcceptC);
		if(isConfirmWindowOpen())
			return Widgets.get(confirmWindowW).getComponent(confirmAcceptC);
		else
			return null;
	}
	
	public static Component getDeclineComponent(){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW).getComponent(tradeDeclineC);
		if(isConfirmWindowOpen())
			return Widgets.get(confirmWindowW).getComponent(confirmDeclineC);
		else
			return null;
	}
	
	public static Component getCloseComponent(){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW).getComponent(tradeCloseC);
		if(isConfirmWindowOpen())
			return Widgets.get(confirmWindowW).getComponent(confirmCloseC);
		else
			return null;
	}
	
	public static String getTradePartner(){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW).getComponent(tradePartnerC).getText().replaceAll("Trading With: ", "");
		if(isConfirmWindowOpen())
			return Widgets.get(confirmWindowW).getComponent(confirmPartnerC).getText();
		else
			return null;
	}
	
	public static int getOfferingItemIdAt(int i){
		if(isTradeWindowOpen()){
			if(Widgets.get(tradeWindowW).getComponent(tradeItemListLeftC).getComponent(i)!=null)
				return Widgets.get(tradeWindowW).getComponent(tradeItemListLeftC).getComponent(i).getItemId();
			return -1;
		}
		else
			return -1;
	}
	
	public static int getOfferedItemIdAt(int i){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW).getComponent(tradeItemListRightC).getComponent(i).getItemId();
		else
			return -1;
	}
	
	private static String parseItemName(String s){
		int start = s.indexOf('>',3)+1;
		int end = s.indexOf('<', 3);
		if(isTradeWindowOpen())
			return s.substring(start);
		if(isConfirmWindowOpen())
			return s.substring(start,end);
		return null;
	}
	
	public static String getOfferingItemNameAt(int i){
		if(isTradeWindowOpen())
			return parseItemName(Widgets.get(tradeWindowW).getComponent(tradeItemListLeftC).getComponent(i).getItemName());
		if(isConfirmWindowOpen())
			return parseItemName(Widgets.get(confirmWindowW).getComponent(confirmItemListLeftC).getComponent(i).getText());
		else
			return null;
	}
	
	public static String getOfferedItemNameAt(int i){
		if(isTradeWindowOpen())
			return parseItemName(Widgets.get(tradeWindowW).getComponent(tradeItemListRightC).getComponent(i).getItemName());
		if(isConfirmWindowOpen())
			return parseItemName(Widgets.get(confirmWindowW).getComponent(confirmItemListRightC).getComponent(i).getText());
		else
			return null;
	}
	
	public static boolean offeringContains(int id){
		for(int i = 0; i < 30; i++){
			if(getOfferingItemIdAt(i) == -1)
				break;
			if(getOfferingItemIdAt(i) == id)
				return true;
		}
		return false;
	}
	
	public static boolean offeredContains(int id){
		for(int i = 0; i < 30; i++){
			if(getOfferedItemIdAt(i) == -1)
				break;
			if(getOfferedItemIdAt(i) == id)
				return true;
		}
		return false;
	}
	
	public static boolean offeringContainsAll(int... ids){
		for(int i: ids){
			if(!offeringContains(i))
				return false;
		}
		return true;
	}
	
	public static boolean offeredContainsAll(int... ids){
		for(int i: ids){
			if(!offeredContains(i))
				return false;
		}
		return true;
	}
	
	public static boolean offeringContainsOneOf(int... ids){
		for(int i: ids){
			if(offeringContains(i))
				return true;
		}
		return false;
	}
	
	public static boolean offeredContainsOneOf(int... ids){
		for(int i: ids){
			if(offeredContains(i))
				return true;
		}
		return true;
	}
	
	public static int getOfferingStackSizeAt(int i){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW).getComponent(tradeItemListLeftC).getComponent(i).getItemStackSize();
		else
			return -1;
	}
	
	public static int getOfferedStackSizeAt(int i){
		if(isTradeWindowOpen())
			return Widgets.get(tradeWindowW).getComponent(tradeItemListRightC).getComponent(i).getItemStackSize();
		else
			return -1;
	}
	
	public static boolean tradePlayer(String name) {
		final String _name = name.replaceAll("\\s", ".");
		Player player = Players.getNearest(new Filter<Player>() {

			@Override
			public boolean accept(Player p) {
				if (p != null && p.getName() != null)
					return Pattern.matches(_name, p.getName());
				return false;
			}

		});
		if (player == null)
			return false;

		Timer timeout = new Timer(30000);
		if (timeout.isRunning()) {
			if (interactPlayer(player)) {
				Timer timer = new Timer(Random.nextInt(3000, 6000));
				while (timer.isRunning() && !isOpen())
					ActiveScript.sleep(100);
			}
			return isOpen();
		}
		return false;
	}

	private static boolean interactPlayer(Player player) {
		if (player != null) {
			Model model = player.getModel();
			String regex = "Trade.*" + player.getName() + ".*";
			for (int tries = 0; tries < 10 && model != null; tries++) {
				Point point = model.getNextPoint();
				if (point != null && player.isOnScreen()) {
					Mouse.move(point);
					Mouse.click(false);
					ActiveScript.sleep(500);
					int index = -1;
					for (int i = 0; i < Menu.getItems().length; i++) {
						if (Pattern.matches(regex, Menu.getItems()[i])) {
							index = i;
							break;
						}
					}
					if (index > -1) {
						Menu.click(index);
						return true;
					} else {
						Point moveTo = Menu.getLocation();
						if (moveTo != null) {
							moveTo.x -= Random.nextInt(5, 15);
							moveTo.y -= Random.nextInt(5, 15);
							Mouse.move(moveTo);
						}
					}
				}
			}
		}
		return false;
	}
}
