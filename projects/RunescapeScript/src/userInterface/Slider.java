package userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Slider {
	int x;
	int y;
	int l;
	int h;
	Color c1;
	Color c2;
	Color c3;

	public Slider(int xa, int ya, int length, int height, Color cc1, Color cc2,
			Color cc3) {
		this.x = xa;
		this.y = ya;
		this.l = length;
		this.h = height;
		this.c1 = cc1;
		this.c2 = cc2;
		this.c3 = cc3;
	}

	public Slider(int xa, int ya, Color cc1, Color cc2, Color cc3) {
		this(xa, ya, 200, 20, cc1, cc2, cc3);
	}
	
	public Slider(int xa, int ya) {
		this(xa,ya, Color.BLACK, Color.WHITE, Color.BLACK);
	}

	public void draw(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		BasicStroke bs = new BasicStroke(1);
		g.setStroke(bs);
		g.setColor(c1);
		g.fillRect(x, y, l, h);
		g.setColor(c2);
		g.drawLine(x + 5, y + (h / 2), x + (l - 5), y + (h / 2));
		g.setColor(c3);
		g.fillOval(x + (getMarker()), y + 2, 16, 16);
	}

	public int mark;

	public void setMarker(int i) {
		this.mark = i;
	}

	public int getMarker() {
		return mark;
	}

	public int getValue() {
		int p = l - getMarker();
		return p;
	}

	public Rectangle getSlider() {
		return new Rectangle(x, y, l - 18, h);
	}

}