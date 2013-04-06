package userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Button {
	Rectangle box;
	Color text, fill, border;
	int x, y, w, h;
	int textX, textY;
	String label;
	public Button(String label, int x, int y, int w, int h, Color text,
			Color fill, Color border) {
		this.box = new Rectangle(x , y, w, h);
		this.border = border;
		this.fill = fill;
		this.text = text;
		this.x = x;
		this.y = y;
		this.label = label;
		
		textX = 2;
		textY = 11;
	}
	
	public void setFillColour(Color colour){
		fill = colour;
	}
	
	public void setText(String label){
		this.label = label;		
	}
	
	public void setTextPosition(int x, int y){
		textX = x;
		textY = y;
	}
	
	public Rectangle buttonBox() {
		return box;
	}
	
	public void draw(Graphics g) {
		Graphics2D g1 = (Graphics2D) g;
		BasicStroke bs = new BasicStroke(1);
		g1.setStroke(bs);
		int boxX = (int) box.getX();
		int boxY = (int) box.getY();
		int boxW = (int) box.getWidth();
		int boxH = (int) box.getHeight();

		g.setColor(fill);
		g.fillRect(boxX, boxY, boxW, boxH);
		g.setColor(border);
		g.drawRect(boxX, boxY, boxW, boxH);
		g.setColor(text);
		g.drawString(label, x+textX, y+textY);
	}
}

