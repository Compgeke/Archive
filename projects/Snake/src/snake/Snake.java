package snake;

import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Snake extends JApplet implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static final int EASY = 4, NORMAL = 8, HARD = 10, IMPOSSIBLE = 20;
	
	private Canvas c;
	private Engine e;
	private JButton easy,normal,hard,impossible;
	private JPanel menu;
	
	public void init(){
		new Snake();
	}
	
	public Snake(){
		setSize(512,512);
		
		easy = new JButton("easy");
		normal = new JButton("normal");
		hard = new JButton("hard");
		impossible = new JButton("impossible");
		
		easy.addActionListener(this);
		normal.addActionListener(this);
		hard.addActionListener(this);
		impossible.addActionListener(this);
		
		FlowLayout fl = new FlowLayout(0,0,0);
		fl.setAlignment(FlowLayout.CENTER);
		
		menu = new JPanel(fl);
		
		menu.add(easy);
		menu.add(normal);
		menu.add(hard);
		menu.add(impossible);		
		
		e = new Engine();
		c = e.getCanvas();
		
		add(menu);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent buttonEvent) {
		menu.setVisible(false);
		add(c);
		
		if(buttonEvent.getSource() == easy){
			e.start(EASY);
		}else if(buttonEvent.getSource() == normal){
			e.start(NORMAL);
		}else if(buttonEvent.getSource() == hard){
			e.start(HARD);
		}else{
			e.start(IMPOSSIBLE);
		}
		listen();
	}
	
	private void listen(){
		new Thread(new Runnable(){
			public void run(){
				while(e.isRunning()){
					try {
						Thread.sleep(1000);
					} catch (Exception ex1) {
						ex1.printStackTrace();
					}
				}
				remove(c);
				menu.setVisible(true);
				reset();
			}
		}).start();
	}
	
	private void reset(){
		e = new Engine();
		c = e.getCanvas();
	}
}
