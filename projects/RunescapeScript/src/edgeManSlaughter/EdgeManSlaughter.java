package edgeManSlaughter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.EnumSet;

import org.rsbuddy.tabs.Attack;
import org.rsbuddy.tabs.Equipment;
import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.widgets.Bank;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.methods.Skills;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Filter;

import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Path;
import com.rsbuddy.script.wrappers.Tile;

@Manifest(authors = "Kieve", version = 1.0, name = "dcEdgeManSlaughter", keywords={"Combat", "Men", "Pure"}, description = "Achieves 40att 40str automatically")
public class EdgeManSlaughter extends ActiveScript implements MessageListener, PaintListener{
	private static final int
		manID[] = {1,2,3},
		bankerID[] = {2759,553},
		boneID = 526,
		closedDoor = 37170,
		attackAnimation = 15071,
		foodID = 333,
		iron = 1323,
		steel = 1325,
		black = 1327,
		mith = 1329,
		adamant = 1331,
		rune = 1333,
		widget = 149,
		component = 242;
		
	private static final Tile
		hutTile = new Tile(3097,3510),
		bankTile = new Tile(3096,3496),
		doorFromHut = new Tile(3100,3509),
		hutSW = new Tile(3092, 3507),
		hutNE = new Tile(3100, 3511);
	
	private static final Area hut = new Area(hutSW, hutNE);
	
	private static final Filter<Npc> filter = new Filter<Npc>(){
		public boolean accept(Npc npc){
			if(npc.getId() == manID[0] || npc.getId() == manID[1] || npc.getId() == manID[2])
				if(!npc.isInCombat() && !Players.getLocal().isInCombat() && Players.getLocal().getAnimation() != attackAnimation)
					if(!(hut.contains(Players.getLocal().getLocation()) ^ hut.contains(npc.getLocation())))
						return true;
			return false;
		}
	};
	
	private static Path path;
	
	private long combatTimer;
	private String status;
	private int lastWeapon, nextWeapon;
	
	private enum ScriptState {
		INIT("Starting up."),
		WALKING_TO_BANK("Walking to bank."),
		WALKING_TO_HUT("Walking to hut."),
		BANKING("Banking."),
		FIGHTING("Killing the men."),
		SWITCHING_WEAPONS("Getting a new weapon out of the bank."),
		SWTICHING_STYLE("Switching to strength style."),
		END("Out of food, or finished, shutting down.");
		
		
		private final String description;
		
		private ScriptState(String description) {
            this.description = description;
        }

        private String getDescription() {
            return description;
        }
	};
	
	public boolean onStart(){
		status = ScriptState.INIT.getDescription();
		return true; 
	}

	public int loop(){
		if(Widgets.get(widget).getComponent(component)!=null && Widgets.get(widget).getComponent(component).isValid()){
			Widgets.get(widget).getComponent(component).interact("Select");
			sleep(500,800);
		}
		if(Players.getLocal().getAnimation() == attackAnimation){
			combatTimer = System.currentTimeMillis();
		}
		ScriptState scriptState = getScriptState();
		status = scriptState.getDescription();
		if(Objects.getNearest(26983) != null && Objects.getNearest(26983).isOnScreen()){
			Objects.getNearest(26983).interact("Climb");
			sleep(400,500);
		}
		if(Objects.getNearest(closedDoor)!=null && Objects.getNearest(closedDoor).isOnScreen() && !Players.getLocal().isInCombat()){
			Objects.getNearest(closedDoor).interact("Open");
		}else{
			switch(scriptState){
				case SWITCHING_WEAPONS:
					if(Npcs.getNearest(bankerID)!=null && Npcs.getNearest(bankerID).isOnScreen()){
						if(!Inventory.contains(nextWeapon)){
							Equipment.unequip(lastWeapon);
							sleep(300,400);
							if(!Bank.isOpen()){
								Bank.open();
								sleep(300,400);
							}
							if(Bank.isOpen()){							
								Bank.depositAll();
								sleep(300,400);
								Bank.withdraw(nextWeapon, 1);
								sleep(300,400);
								Bank.close();
							}
						}else{
							if(Inventory.contains(nextWeapon)){
								Inventory.getItem(nextWeapon).interact("Wield");
								sleep(300,400);
							}						
						}
						break;
					}else{
						path = Walking.findPath(bankTile);
						if(!path.traverse(EnumSet.of(Path.TraversalOption.HANDLE_RUN, Path.TraversalOption.SPACE_ACTIONS))){
							path = Walking.findPath(doorFromHut);
							path.traverse();
						}
						sleep(1000,2000);
						break;
					}
				case WALKING_TO_BANK:
						path = Walking.findPath(bankTile);
						path.traverse(EnumSet.of(Path.TraversalOption.HANDLE_RUN, Path.TraversalOption.SPACE_ACTIONS));
						sleep(1000,2000);
						break;
				case WALKING_TO_HUT:path = Walking.findPath(hutTile);
						path.traverse(EnumSet.of(Path.TraversalOption.HANDLE_RUN, Path.TraversalOption.SPACE_ACTIONS));
						sleep(1000,2000);
						break;
				case BANKING:
					if(!Bank.isOpen()){
						Bank.open();
					}else{
						Bank.depositAll();
						if(Bank.getCount(foodID)>0){
							Bank.withdraw(foodID, 28);
						}else{
							Bank.close();
							log("Stopping, no food.");
							stop();
						}					
					}				
					break;
				case FIGHTING:
					if(Players.getLocal().isInCombat()){
						if(Players.getLocal().getHpPercent()<50){
							if(Inventory.contains(foodID)){
								Inventory.getItem(foodID).interact("Eat");
								sleep(300,500);
							}else{
								log("No food while in combat.");
							}
						}
					}else if(!Inventory.isFull() && GroundItems.getNearest(boneID) != null){
						GroundItems.getNearest(boneID).interact("Take Bones");
						sleep(300,400);
					}else if(!Players.getLocal().isMoving() && (System.currentTimeMillis()-combatTimer) > 3000){
						Npc temp = Npcs.getNearest(filter);
						if(temp != null){
							combatTimer = System.currentTimeMillis();
							temp.interact("Attack");
							sleep(150,200);
						}
					}
					break;
				case SWTICHING_STYLE:
					if(Skills.getCurrentLevel(Skills.ATTACK)<40){
						Attack.setFightMode(0);
					}else{
						Attack.setFightMode(1);
					}
					break;
				case END:
					log("Stopping, level 40 in att and str.");
					stop();
					break;
			}
		}
		sleep(10,15);
		return 0; 
	}
	@Override
	public void messageReceived(MessageEvent e){		
	}
	@Override
	public void onRepaint(Graphics g){
		Graphics2D pen = (Graphics2D)g;
		pen.setColor(Color.ORANGE.darker());
		pen.drawString("Status: " + status, 8, 30);
	}
	
	private ScriptState getScriptState(){
		if(Skills.getCurrentLevel(Skills.ATTACK) == 40 && Attack.getFightMode() == 0){
			return ScriptState.SWTICHING_STYLE;
		}else if(Skills.getCurrentLevel(Skills.ATTACK) < 40 && Attack.getFightMode() != 0){
			return ScriptState.SWTICHING_STYLE;
		}
		if(!Players.getLocal().isInCombat()){
			if((Skills.getCurrentLevel(Skills.ATTACK) == 40 && Skills.getCurrentLevel(Skills.STRENGTH) == 40)){
				return ScriptState.END;
			}
			switch(Skills.getCurrentLevel(Skills.ATTACK)){
				case 40:
					if(!Equipment.containsOneOf(rune)){
						lastWeapon = adamant;
						nextWeapon = rune;
						return ScriptState.SWITCHING_WEAPONS;
					}
					break;
				case 30:
					if(!Equipment.containsOneOf(adamant)){
						lastWeapon = mith;
						nextWeapon = adamant;
						return ScriptState.SWITCHING_WEAPONS;
					}
					break;
				case 20:
					if(!Equipment.containsOneOf(mith)){
						lastWeapon = black;
						nextWeapon = mith;
						return ScriptState.SWITCHING_WEAPONS;
					}
					break;/*
				case 10:
					if(!Equipment.containsOneOf(black)){
						lastWeapon = steel;
						nextWeapon = black;
						return ScriptState.SWITCHING_WEAPONS;
					}
					break;
				case 5:
					if(!Equipment.containsOneOf(steel)){
						lastWeapon = iron;
						nextWeapon = steel;
						return ScriptState.SWITCHING_WEAPONS;
					}*/
			}
			if(Inventory.getCount(boneID) == 28 || !Inventory.contains(foodID) ||(!Inventory.contains(foodID) && Inventory.isFull())){
				if(Npcs.getNearest(bankerID)!=null && Npcs.getNearest(bankerID).isOnScreen())
					return ScriptState.BANKING;
				else
					return ScriptState.WALKING_TO_BANK;
			}else if(Inventory.contains(foodID)){
				if(Npcs.getNearest(manID)!=null && Npcs.getNearest(manID).isOnScreen())
					return ScriptState.FIGHTING;
				else
					return ScriptState.WALKING_TO_HUT;
			}
		}else{
			return ScriptState.FIGHTING;
		}
		log("Something is wrong!!!");
		return ScriptState.INIT;
	}
}








