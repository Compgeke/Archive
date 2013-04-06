package oscilloscope;

import javax.swing.JFrame;

public class OscillofunStart extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1296, HEIGHT = 748;
	
	public static void main(String args[]){
		new OscillofunStart();
	}
	
	public OscillofunStart(){
		super("OscilloFun");
		setSize(WIDTH,HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		

		Oscilloscope o = new Oscilloscope(WIDTH, HEIGHT);
		add(o);
		o.start();
	}
}
