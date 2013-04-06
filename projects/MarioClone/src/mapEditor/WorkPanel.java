package mapEditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import mapEditor.MapEditor.EditorListener;

public class WorkPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final Dimension d = new Dimension(640,360);
	protected MapSlot placementTiles[][];
	
	public WorkPanel(EditorListener l){
		super(new GridLayout(11,20,0,0));
		setPreferredSize(d);
		setOpaque(false);
		
		placementTiles = new MapSlot[11][20];
		for(int i = 0; i < placementTiles.length; i++){
			for(int x = 0; x < placementTiles[i].length; x++){
				placementTiles[i][x] = new MapSlot(-1);
				placementTiles[i][x].setActionCommand(null);
				placementTiles[i][x].setOpaque(false);
				placementTiles[i][x].setContentAreaFilled(false);
				placementTiles[i][x].addActionListener(l);
				placementTiles[i][x].addMouseListener(l);
				placementTiles[i][x].addMouseMotionListener(l);
				add(placementTiles[i][x]);
			}
		}
		setVisible(false);
	}
	
	public void removeListener(EditorListener l){
		for(int i = 0; i < placementTiles.length; i++){
			for(int x = 0; x < placementTiles[i].length; x++){
				placementTiles[i][x].removeActionListener(l);
				placementTiles[i][x].removeMouseListener(l);
			}
		}
	}
	
	public void disable(){
		for(int i = 0; i < placementTiles.length; i++){
			for(int x = 0; x < placementTiles[i].length; x++){
				placementTiles[i][x].setEnabled(false);
			}
		}
	}
	
	public void enable(){
		for(int i = 0; i < placementTiles.length; i++){
			for(int x = 0; x < placementTiles[i].length; x++){
				placementTiles[i][x].setEnabled(true);
			}
		}
	}
	
	public void addListener(EditorListener el){
		for(int i = 0; i < placementTiles.length; i++){
			for(int x = 0; x < placementTiles[i].length; x++){
				placementTiles[i][x].addActionListener(el);
				placementTiles[i][x].addMouseListener(el);
			}
		}
	}
	
	protected int[][] getStates(){
		int[][] returnThis = new int[placementTiles.length][placementTiles[0].length];
		for(int i = 0; i < placementTiles.length; i++){
			for(int x = 0; x < placementTiles[i].length; x++){
				returnThis[i][x] = placementTiles[i][x].getState();
			}
		}
		return returnThis;
	}
	
	protected void paintComponent(Graphics g){
		Image i = new ImageIcon(this.getClass().getResource("/Images/back.png")).getImage();
		g.drawImage(i, 0, -9, null);
		super.paintComponent(g);
	}
}
