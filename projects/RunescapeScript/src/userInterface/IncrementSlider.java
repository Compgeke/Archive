package userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class IncrementSlider {

	int x;
	int y;
	int l;
	int h;
	int inc;
	int[] ins = new int[1000];
	ArrayList<Increment> incs = new ArrayList<Increment>();
	Color c1;
	Color c2;
	Color c3;
	boolean border;

	public int mark;
	public int tempmark;

	public IncrementSlider(int xa, int ya, int length, int height, int i,
			Color cc1, Color cc2, Color cc3) {
		this.x = xa;
		this.y = ya;
		this.l = length;
		this.h = height;
		this.c1 = cc1;
		this.c2 = cc2;
		this.c3 = cc3;
		this.inc = i;
		doIncs();
	}

	public IncrementSlider(int xa, int ya, int i, Color cc1, Color cc2,
			Color cc3) {
		this(xa, ya, 200, 20, i, cc1, cc2, cc3);
	}
	
	public IncrementSlider(int xa, int ya, int i) {
		this(xa, ya, 200, 20, i, Color.BLACK, Color.WHITE, Color.BLACK);
	}

	private void doIncs() {
		for (int i = 0; i <= inc; i++) {
			incs.add(new Increment(getIncX(i), i));
		}
	}

	private int getIncX(int i) {
		return ((l - 10) / inc) * i;
	}

	private int getIncLength(int i) {
		int length = (l - 10) / inc;
		if (i == 0 || i == inc) {
			return length / 2;
		} else {
			return length;
		}
	}

	public void draw(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		BasicStroke bs = new BasicStroke(1);
		g.setStroke(bs);

		g.setColor(c1);
		g.fillRect(x + getIncX(1) - 10, y, l + 10 - getIncLength(0), h);
		g.setColor(c2);
		g.drawRect(x + getIncX(1) - 10, y, l + 10 - getIncLength(0), h);
		g.drawLine(x + 5 + getIncX(1), y + (h / 2), x + l - getIncLength(0), y
				+ (h / 2));
		drawInclines(g);
		g.setColor(c3);
		g.fillOval(x + (getNearestInc(getTempMark()).getX()), y + 2, 16, 16);
	}

	private void drawInclines(Graphics g) {
		int hh = (h / 2) / 2;
		for (int i = 1; i <= inc; i++) {
			int x2 = getIncX(i);
			g.drawLine(x2 + 5 + x, y + hh, x2 + 5 + x, (y + h) - hh);
		}
		// g.drawLine(l + x, y + hh, l + x, (y + h) - hh);
	}

	public void setMarker(int i) {
		this.tempmark = i;
	}

	private void setRealMark(int i) {
		this.mark = i;
	}

	private int getTempMark() {
		return tempmark;
	}

	private int getMarker() {
		return mark;
	}

	public int getValue() {
		int p = getMarker();
		return p;
	}

	public Rectangle getSlider() {
		return new Rectangle(x, y, l, h);
	}

	private Increment getNearestInc(int i) {
		Increment closest = null;
		for (int p = 0; p <= inc; p++) {
			Increment in = incs.get(p);
			int x = in.getX();
			if (closest != null) {
				if (Math.abs(i - x) <= Math.abs(closest.getX() - x)) {
					closest = in;
				}
			} else {
				closest = in;
			}
		}
		setRealMark(closest.getX());
		return closest;
	}
}