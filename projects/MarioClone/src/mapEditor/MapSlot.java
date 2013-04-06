package mapEditor;

import javax.swing.JButton;

public class MapSlot extends JButton{
	private static final long serialVersionUID = 1L;
	
	private int state;
	
	public MapSlot(){
		super();
	}
	
	public MapSlot(int c){
		super();
		state = c;
	}
	
	protected int getState(){
		return state;
	}
	protected void setState(int c){
		state = c;
	}	
}
