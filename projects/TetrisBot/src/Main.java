import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private static Main INSTANCE;
	private static int WIDTH, HEIGHT;
	private static Robot r;
	private static JPanel canvas;
	private static ArrayList<String> msg = new ArrayList<String>();

	private static final int KEY_DELAY = 70;
	private static boolean searchMutex = false;

	public Main() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		INSTANCE = this;
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		WIDTH = d.width;
		HEIGHT = d.height;
		setSize(WIDTH, HEIGHT);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBackground(new Color(0, 0, 0, 0));

		canvas = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				render(g);
			}
		};

		add(canvas);

		setLocationRelativeTo(null);
		com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
		setVisible(true);
	}

	public static void main(String args[]) {
		new Main();
		new Thread(INSTANCE).start();
		msg.add("Status:");
	}

	private void findBoard() {
		searchMutex = true;
		msg.remove("Searching for Board");
		msg.add("Searching for Board");
		new Thread(new Runnable() {
			public void run() {
				BufferedImage bi = r.createScreenCapture(getBounds());
				boolean foundIt = false;
				search: for (int w = 0; w < bi.getWidth(); w++) {
					for (int h = 0; h < bi.getHeight(); h++) {
						if (Piece.b1 == bi.getRGB(w, h)) {
							for (int i = 0; i < 22; i++) {
								try {
									if (Piece.b1 != bi.getRGB(w + i, h)) {
										break;
									}
									if (Piece.b2 != bi.getRGB(w + i + 22, h)) {
										break;
									}
									if (i == 21) {
										foundIt = true;
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									break;
								}
							}
							if (foundIt) {
								Board.BOARD_X = w;
								Board.BOARD_Y = h;
								msg.add("Board Location:");
								msg.add("X: " + Board.BOARD_X);
								msg.add("Y: " + Board.BOARD_Y);
								msg.remove("Searching for Board");
								break search;
							}
						}
					}
				}
				searchMutex = false;
			}
		}).start();
	}
	
	public static int getGhost(){
		int temp = r.getPixelColor(Board.BOARD_X+4*22, Board.BOARD_Y-22).getRGB();
		if(temp != Piece.b1 && temp != Piece.b2){
			return temp;
		}
		return -1;
	}
	
	private void findGhost() {
		this.invalidate();
		
		int temp = r.getPixelColor(Board.BOARD_X+4*22, Board.BOARD_Y-22).getRGB();
		if(temp != Piece.b1 && temp != Piece.b2){
			Board.updateGhost(temp);
		}
		/*
		 BufferedImage bi = r.createScreenCapture(new Rectangle(Board.BOARD_X+4*22,Board.BOARD_Y-1,1,1));
		 end:for (int w = 0; w < bi.getWidth(); w += 22) {
			for (int h = 0; h < bi.getHeight(); h += 22) {
				if(bi.getRGB(w+1,h) == Piece.ghost){
					int temp = h;
					while(bi.getRGB(w+1, temp) == Piece.ghost || bi.getRGB(w, temp) == Piece.b1 || bi.getRGB(w, temp) == Piece.b2){
						temp-=22;
						if(temp < 0){
							if(temp+)
						}
							break end;
					}
					System.out.println(bi.getRGB(w, temp));
					break end;
				}
			}
		}*/
	}
	
	public static void key(int i) {
		r.keyPress(i);
		r.keyRelease(i);
		r.delay(KEY_DELAY);
	}
	
	public static void drop(){
		key(KeyEvent.VK_SPACE);
		Board.canHold = true;
	}
	
	public static void hold(){
		key(KeyEvent.VK_F);
		Board.canHold = false;
	}
	
	public static void left(int num){
		for(int i = 0; i < num; i++)
			key(KeyEvent.VK_LEFT);
	}
	
	public static void right(int num){
		for(int i = 0; i < num; i++)
			key(KeyEvent.VK_RIGHT);
	}
	
	public static void rotate(int num){
		int temp;
		if(num > 0)
			temp = KeyEvent.VK_S;
		else {
			temp = KeyEvent.VK_D;
			num *= -1;
		}
		for(int i = 0; i < num; i++)
			key(temp);
	}
	
	private void logic() {
		if (!foundBoard() && !searchMutex) {
			findBoard();
		}else if(foundBoard()){
			findGhost();
		}
	}
	
	private static void render(Graphics g) {
		for (int i = 0; i < msg.size(); i++) {
			g.setColor(Color.BLACK);
			g.fillRect(WIDTH - 250, HEIGHT - 15 * ((msg.size()-1-i) + 1) - 42, 250, 15);
			g.setColor(Color.RED);
			g.drawString(msg.get(i), WIDTH - 250 + 3, HEIGHT - 15 * ((msg.size()-1-i) + 1) - 30);
		}
		ArrayList<Piece> pieces = Board.pieces;
		for (int i = 0; i < pieces.size(); i++) {
			g.setColor(new Color(pieces.get(i).piece));
			g.fillRect(pieces.get(i).x+2, pieces.get(i).y+2, 18, 18);
		}
		if(foundBoard()){
			g.setColor(Color.RED);
			g.drawRect(Board.BOARD_X-1, Board.BOARD_Y-1, Board.BOARD_W+1, Board.BOARD_H+1);
		}
		
		g.fillRect(Board.BOARD_X+4*22, Board.BOARD_Y-3, 1, 1);
	}

	public void run() {
		while (true) {
			logic();
			//canvas.repaint();
			this.repaint();
			try {
				Thread.sleep(2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean foundBoard() {
		return Board.BOARD_X != -1 && Board.BOARD_Y != -1;
	}
	
	public static void removeMsg(String s){
		for(int i = 0; i < msg.size(); i++){
			if(msg.get(i).contains(s)){
				msg.remove(i);
			}
		}
	}
	
	public static void addMsg(String s){
		msg.add(s);
	}
}
