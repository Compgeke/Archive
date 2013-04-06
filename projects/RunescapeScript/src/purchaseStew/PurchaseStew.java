package purchaseStew;

import static com.rsbuddy.script.util.Random.nextInt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.EnumSet;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.widgets.Bank;

import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.Camera;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.GrandExchange;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.Path;
import com.rsbuddy.script.wrappers.Tile;

@Manifest(authors = "Kieve", version = 1.0, name = "Stews for profit!", keywords={"Private"}, description = "Purchases stews, then banks them.")
public class PurchaseStew extends ActiveScript implements PaintListener{
	private static final int
		bankerID = 494,
		barTenderID = 737,
		stewID = 2003;
	private static final Tile bankTilePosition[] = {
			new Tile(2724,3493),//start here
			new Tile(2691,3493)//arrive here
		};
	
	private enum ScriptState {
		INIT("Starting up."),
		WALKING_TO_BANK("Walking to bank."),
		WALKING_TO_BAR("Walking to bar."),
		BANKING("Depositing Stews."),
		PURCHASING("Purchasing Stews."),
		END("Out of gold, shutting down.");
		
		
		private final String description;
		
		private ScriptState(String description) {
            this.description = description;
        }

        private String getDescription() {
            return description;
        }
	};
	
	private Path path;
	private String status;
	private int collected = 0, stewValue;
	private long startTime;
	
	public boolean onStart(){
		status = ScriptState.INIT.getDescription();
		//stewValue = GeItem.lookup(stewID).getGuidePrice();
		stewValue = GrandExchange.lookup(stewID).getGuidePrice();
		startTime = System.currentTimeMillis();
		return true; 
	}
	
	boolean talking;
	long lastActionTime;
	public int loop(){
		ScriptState scriptState = getScriptState();
		if(scriptState != ScriptState.PURCHASING)
			antiBan();
		status = scriptState.getDescription();
		switch(scriptState){
			case BANKING:
				if(Inventory.contains(stewID)){
					Bank.open();
					if(Inventory.getItem(stewID)!=null)
						Inventory.getItem(stewID).interact("Deposit-All");
					sleep(200,300);
					Bank.close();
				}
				break;
			case PURCHASING:
				if(System.currentTimeMillis()-lastActionTime > 3000){
					talking = false;
				}
				if(!talking){
					status = "Attempting to talk to bartender.";
					if(Players.getLocal().isIdle()){
						Npcs.getNearest(barTenderID).interact("Talk-to Bartender");
						sleep(400,600);
					}
				}
				if(Game.getTalkComponent()!=null){
					lastActionTime = System.currentTimeMillis();
					status = "Talking with bartender.";
					if(Widgets.get(241).isValid()){
						if(Widgets.get(241).containsText("20") && talking){
							status = "Bartender is telling us it is 20 coins.";
							collected++;
							sleep(300,500);
							talking = false;
						}else{
							status = "First dialog, progressing.";
							talking = true;
						}
						Widgets.clickContinue();
					}else if(Widgets.get(230).isValid()){
						status = "Selecting \"What do you have?\"";
						talking = true;
						Widgets.get(230).getComponent(2).click();
					}else if(Widgets.get(64).isValid()){
						status = "Progressing.";
						talking = true;
						Widgets.clickContinue();
					}else if(Widgets.get(242).isValid()){
						status = "Bartender is telling us what he has.";
						talking = true;
						Widgets.clickContinue();
					}else{
						status = "Opps!";
						log("something is wrong");
					}
				}else if(Widgets.get(232).isValid()){
					lastActionTime = System.currentTimeMillis();
					status = "Selecting \"I'll have some stew please.\"";
					sleep(100,150);
					Widgets.get(232).getComponent(4).click();
				}
				sleep(10,15);
				break;
			case WALKING_TO_BANK:
				path = Walking.findPath(bankTilePosition[0]);
				path.traverse(EnumSet.of(Path.TraversalOption.HANDLE_RUN, Path.TraversalOption.SPACE_ACTIONS));
				sleep(1000,2000);
				break;
			case WALKING_TO_BAR:
				path = Walking.findPath(bankTilePosition[1]);
				path.traverse(EnumSet.of(Path.TraversalOption.HANDLE_RUN, Path.TraversalOption.SPACE_ACTIONS));
				sleep(1000,2000);
				break;
			case END:
				stop();
				break;
		}
		return 0; 
	}
	
	private ScriptState getScriptState(){
		if(Inventory.isFull()){
			if(Npcs.getNearest(bankerID) != null && Npcs.getNearest(bankerID).isOnScreen()){
				return ScriptState.BANKING;
			}else{
				return ScriptState.WALKING_TO_BANK;
			}
		}else if(!Inventory.contains(995) || Inventory.getItem(995).getStackSize() < 20){
			return ScriptState.END;
		}else{
			if(Npcs.getNearest(barTenderID) != null){
				if(Npcs.getNearest(barTenderID).isOnScreen()){
					return ScriptState.PURCHASING;
				}else{
					return ScriptState.WALKING_TO_BAR;
				}
			}else{
				return ScriptState.WALKING_TO_BAR;
			}
		}
	}
	
	private int calcPerHour(int total){
    	return (int)(1000*60*60* (double)total / (double)(System.currentTimeMillis()-startTime));
    }
	
	private String formatTime(long time){
    	String returnThis;
    	long seconds, minutes, hours, leftovers;
    	hours = time / (1000 * 60 * 60);
    		leftovers = time % (1000 * 60 * 60);
    	minutes = leftovers / (1000 * 60);
    		leftovers %= (1000 * 60);
    	seconds = leftovers / (1000);
    	
    	returnThis = "" + hours + ":" + (minutes<10?"0"+minutes:minutes) + ":" + (seconds<10?"0"+seconds:seconds);
    	
    	return returnThis;
    }
	
	public void onRepaint(Graphics g){
		g.setColor(new Color(30,30,30,98));
		g.fill3DRect(5, 29, 135, 84, true);
		g.fill3DRect(5, 322, 355, 16, true);
		g.setColor(Color.ORANGE.darker());
		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		g.drawString("Price:     " + stewValue, 8, 40);
		g.drawString("Run Time:  " + formatTime(System.currentTimeMillis()-startTime), 8, 53);
		g.drawString("Collected: " + collected, 8, 66);
		g.drawString("Per Hour:  " + calcPerHour(collected), 8, 79);
		g.drawString("Profit:    " + collected*(stewValue-20), 8, 92);
		g.drawString("Per Hour:  " + calcPerHour(collected*(stewValue-20)), 8, 105);
		

		g.drawString("Status:    " + status, 8, 333);
	}
	
	public void antiBan() {
        switch (nextInt(1, 90)) {
            case 1:
            	log("[Anti-ban] Moving mouse");
            	Mouse.moveSlightly();
            	sleep(2000, 6000);
            	Mouse.moveRandomly(150, 350);
                break;
            case 2:
                if (nextInt(0, 13) == 2){
	                log("[Anti-ban] Turning screen");
	                Camera.setCompassAngle(nextInt(30, 70));
                }
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            	log("[Anti-ban] Moving mouse off screen");
            	Mouse.moveOffScreen();
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                Camera.moveRandomly(nextInt(2000, 4200));
                break;
            case 13:
            	Camera.setPitch(true);
                break;
            case 14:
            	Camera.setPitch(false);
                break;
            case 15:
                Camera.moveRandomly(nextInt(9000, 12000));
                break;
            case 16:
                Camera.moveRandomly(nextInt(5000, 8000));
                break;
            case 17:
                Camera.setCompass('n');
                break;
            case 18:
                Camera.setCompass('e');
                break;
            case 19:
                Camera.setCompass('s');
                break;
            case 20:
                Camera.setCompass('w');
                break;
            case 21:
                Camera.moveRandomly(nextInt(500, 950));
                log("[Anti-ban] Turning screen");
                Camera.setCompassAngle(nextInt(60, 90));
                break;
            default:
                break;
        }
    }
}