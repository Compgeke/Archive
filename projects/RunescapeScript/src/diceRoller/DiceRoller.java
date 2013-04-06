package diceRoller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.rsbuddy.tabs.Inventory;

import userInterface.Button;
import userInterface.DropDown;
import userInterface.MenuItem;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.Mouse;




@Manifest(authors = "Kieve", name = "DiceRoller", version = 1, description = "Rolls the seleced die, the selected number of times.")
public class DiceRoller extends ActiveScript implements MessageListener, PaintListener, MouseListener, MouseMotionListener{	
	private boolean
		started = false,
		showing = true,
		rolling = false,
		privateRolls = true;
	private int
		selection = 0,
		numberOfRolls = 0,
		totalRolls = 0,
		totalSum = 0,
		diceID[] = {
			15084,
			15086,
			15088,
			15100,
			15090,
			15092,
			15094,
			15096,
			15098,			
		},
		roll = 109,
		privateRoll = 0,
		friendsRoll = 11,
		mouseX = 270,
		mouseY[] = {
			382,
			400,
			418,
			437,
			452			
		},		
		thousands = 0,
		hundreds = 0,
		tens = 0,
		ones = 0;
	
	private String[] dice = {
		"Null",
		"Six",
		"Double Sixs",
		"Four",
		"Eight",
		"Ten",
		"Tweleve",
		"Twenty",
		"Percentile"
	};
	
	String status = "Stopped.", message;
	Button visibility, toggleRun, togglePrivate, pThousand, pHundred, pTen, pOne, sThousand, sHundred, sTen, sOne;
	DropDown diceSelect;
	
	public boolean onStart(){
		visibility = new Button("Hide",477,459,35,13,Color.BLACK, Color.GREEN, Color.BLACK);
		toggleRun = new Button("Start",462,411,35,13,Color.BLACK, Color.RED, Color.BLACK);
		togglePrivate = new Button("Toggle",8,382,45,18,Color.BLACK, Color.MAGENTA, Color.BLACK);
		togglePrivate.setTextPosition(4, 14);
		
		diceSelect = new DropDown(8,360,80,18,"Dice", Color.BLACK, Color.CYAN, Color.BLACK);
		diceSelect.add(new MenuItem(13,"1. Six",diceSelect,0));
		diceSelect.add(new MenuItem(13,"2. 2Six",diceSelect,1));
		diceSelect.add(new MenuItem(13,"3. Four",diceSelect,2));
		diceSelect.add(new MenuItem(13,"4. Eight",diceSelect,3));
		diceSelect.add(new MenuItem(13,"5. Ten",diceSelect,4));
		diceSelect.add(new MenuItem(13,"6. Tweleve",diceSelect,5));
		diceSelect.add(new MenuItem(13,"7. Twenty",diceSelect,6));
		diceSelect.add(new MenuItem(13,"8. Percentile",diceSelect,7));
		
		pThousand = new Button("+",100,382,10,10,Color.BLACK, Color.GREEN, Color.BLACK);
		pHundred = 	new Button("+",110,382,10,10,Color.BLACK, Color.GREEN, Color.BLACK);
		pTen = 		new Button("+",120,382,10,10,Color.BLACK, Color.GREEN, Color.BLACK);
		pOne = 		new Button("+",130,382,10,10,Color.BLACK, Color.GREEN, Color.BLACK);
		
		sThousand = new Button("-",100,412,10,10,Color.BLACK, Color.RED, Color.BLACK);
		sHundred = 	new Button("-",110,412,10,10,Color.BLACK, Color.RED, Color.BLACK);
		sTen = 		new Button("-",120,412,10,10,Color.BLACK, Color.RED, Color.BLACK);
		sOne = 		new Button("-",130,412,10,10,Color.BLACK, Color.RED, Color.BLACK);
		
		return true; 
	}
	
	private void stopRunning(){
		started = false;
		status = "Stopped.";
		toggleRun.setText("Start");
		toggleRun.setFillColour(Color.RED);
	}
	private int parseNumberOfRolls(){
		return Integer.parseInt(thousands + "" + hundreds + "" + tens + "" + ones);
	}
	
	private String formattRolls(int i){
		String prefix, number;
		number = Integer.toString(i);
		if(number.length() == 1)
			prefix = "000";
		else if(number.length() == 2)
			prefix = "00";
		else if(number.length() == 3)
			prefix = "0";
		else
			prefix = "";
		return prefix+number;
	}
	
	@Override
	public int loop(){
		if(totalRolls == numberOfRolls){
			started = false;
			stopRunning();
		}
		if(started)
		if(Inventory.contains(diceID[selection])){
			if(!rolling){
				String roll;
				if(privateRolls)
					roll = "Private-roll";
				else
					roll = "Friends-roll";
				Inventory.getItem(diceID[selection]).interact(roll);
			}
		}else if(Inventory.containsOneOf(diceID)){
			Inventory.getItem(diceID).interact("Choose-dice");
			sleep(400,800);
			int current;
			if(selection > 4){
				Mouse.click(mouseX, mouseY[4], true);
				current = mouseY[selection - 5];
				sleep(400,800);
			}else{
				current = mouseY[selection-1];
			}
			Mouse.click(mouseX, current, true);
			sleep(800,1200);
		}else{
			started = false;
			stopRunning();
		}
		sleep(800, 1200);
		return 0; 
	}
	
	@Override
	public void messageReceived(MessageEvent e){
		if(totalRolls != numberOfRolls){
			if((e.getId() == friendsRoll || e.getId() == privateRoll)
					&& e.getMessage().startsWith("You rolled ")){
				rolling = false;
				int end = e.getMessage().indexOf('<', 23);
				String message = e.getMessage().substring(23, end);
				this.message = message;
				totalSum += Integer.parseInt(message);
				totalRolls++;
			}else if(e.getId() == roll && e.getMessage().startsWith("Roll")){
				rolling = true;
			}			
		}else{
			started = false;
			stopRunning();
		}
	}
	@Override
	public void onRepaint(Graphics g){
		Graphics2D pen = (Graphics2D) g;
		visibility.draw(pen);
		if(showing){
			pen.setColor(Color.BLUE.darker());
			pen.fillRect(7, 345, 490, 80);
			toggleRun.draw(pen);
			togglePrivate.draw(pen);
			
			pen.setColor(Color.YELLOW);
			pen.drawString("Kieve's Dice Roller",8,356);
			pen.drawString("Status: " + status, 130, 356);
			pen.drawString(privateRolls?"Private-roll":"Friends-roll", 8, 417);
			pen.drawString(formattRolls(parseNumberOfRolls()), 105, 408);
			
			pen.drawString("Roll: " + message + " on " + dice[selection], 200, 370);
			pen.drawString("Total: " + totalSum, 200, 383);
			pen.drawString("Rolls: " + totalRolls + " of " + numberOfRolls, 200, 396);
			pen.drawString("Current Average: " + totalSum/(totalRolls>0?totalRolls:1), 200, 419);
			
			diceSelect.draw(pen);
			
			pThousand.draw(pen);
			pHundred.draw(pen);
			pTen.draw(pen);
			pOne.draw(pen);
			sThousand.draw(pen);
			sHundred.draw(pen);
			sTen.draw(pen);
			sOne.draw(pen);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e){
		Point p = e.getPoint();
		if(visibility.buttonBox().contains(p)){
			showing = !showing;
			if(showing){
				visibility.setFillColour(Color.GREEN);
				visibility.setText("Hide");
			}else{
				visibility.setFillColour(Color.RED);
				visibility.setText("Show");
			}
		}
		if(toggleRun.buttonBox().contains(p)){
			if(!started){
				if(Inventory.containsOneOf(diceID)){
					if(diceSelect.getSelected() != null){
						if(numberOfRolls > 0){
							status = "Running.";
							totalRolls = 0;
							totalSum = 0;
							toggleRun.setText("Stop");
							toggleRun.setFillColour(Color.GREEN);
							started = true;
						}else{
							status = "Please select the number or rolls.";	
						}
					}else{
						status = "Please select a die from the dropdown list.";
					}
				}else{
					status = "You do not have a die in your inventory.";
				}
			}
		}else if(!started){
			if(togglePrivate.buttonBox().contains(e.getPoint()) && !diceSelect.getOpened()){
				privateRolls = !privateRolls;
			}else if (diceSelect.getBox().contains(e.getPoint())){
				diceSelect.setOpen(true);
			}else if (diceSelect.fullBox().contains(e.getPoint()) && diceSelect.getOpened()){
				for(MenuItem i : diceSelect.getMenuItems()){
					if (i.getBox().contains(e.getPoint())){
						diceSelect.setOpen(false);
						diceSelect.setSelected(true, i);
						selection = Integer.parseInt(diceSelect.getSelected().getString().substring(0,1));
					} else {
						diceSelect.setSelected(false, i);
					}
				}
			}else if (pThousand.buttonBox().contains(e.getPoint())){
				thousands = increase(thousands);
			}else if (pHundred.buttonBox().contains(e.getPoint())){
				hundreds = increase(hundreds);
			}else if (pTen.buttonBox().contains(e.getPoint())){
				tens = increase(tens);
			}else if (pOne.buttonBox().contains(e.getPoint())){
				ones = increase(ones);
			}else if (sThousand.buttonBox().contains(e.getPoint())){
				thousands = decrease(thousands);
			}else if (sHundred.buttonBox().contains(e.getPoint())){
				hundreds = decrease(hundreds);
			}else if (sTen.buttonBox().contains(e.getPoint())){
				tens = decrease(tens);
			}else if (sOne.buttonBox().contains(e.getPoint())){
				ones = decrease(ones);
			}
			numberOfRolls = parseNumberOfRolls();
		}
	}
	
	private int increase(int i){
		if(i < 9)
			i+=1;
		else
			i-=9;
		return i;
	}
	
	private int decrease(int i){
		if(i > 0)
			i--;
		else
			i+=9;
		return i;
	}

	@Override
	public void mouseMoved(MouseEvent e){
		if (!diceSelect.fullBox().contains(e.getPoint())){
			diceSelect.setOpen(false);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}	
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
}