package mapEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import mapEditor.MapEditor.EditorListener;

public class PalletPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private Border b;
	private MapSlot pallet[];
	private int currentChoice;
	
	public PalletPanel(EditorListener l){
		super(new GridLayout(5,1,0,0));
		setPreferredSize(new Dimension(32,32*5));
		setOpaque(false);
		
		b = new LineBorder(Color.BLUE, 2);
		
		pallet = new MapSlot[5];
		BufferedImage tiles[] = l.getTiles();
		for(int i = 0; i < pallet.length; i++){
			pallet[i] = new MapSlot();
			pallet[i].setActionCommand("changeIcon");
			pallet[i].setOpaque(false);
			pallet[i].setContentAreaFilled(false);
			pallet[i].setIcon(new ImageIcon(tiles[i]));
			pallet[i].setState(i);
			pallet[i].setBorder(null);
			pallet[i].addActionListener(l);
			pallet[i].addMouseWheelListener(l);
			add(pallet[i]);
		}
		
		pallet[0].setBorder(b);
		currentChoice = 0;
	}
	
	private void removeBorders(){
		for(int i = 0; i < pallet.length; i++){
			pallet[i].setBorder(null);
		}
	}
	
	public void setCurrentChoice(int choice){
		currentChoice = choice;
		removeBorders();
		pallet[choice].setBorder(b);
	}
	
	public int getCurrentChoice(){
		return currentChoice;
	}
}
