package userInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class MenuItem {
	int x;
	int y;
	int w;
	int h;
	String s;
	boolean selected;
	DropDown d;

	public MenuItem(int ha, String sa, DropDown d, int no) {
		this.h = ha;
		this.y = getY(no, d);
		this.x = getX(d);
		this.s = sa;
		this.w = getLength(d);
		this.d = d;
	}

	public MenuItem(String sa, DropDown d, int no) {
		this(15, sa, d, no);
	}

	public void draw(Graphics g, Color c1, Color c2, Color c3) {
		int h = (int) getBox().getHeight();
		int hh = (h / 2) / 2;
		g.setColor(c3);
		g.drawRect(x, y, w, h);
		g.setColor(c2);
		g.fillRect(x, y, w, h);
		g.setColor(c1);
		g.drawString(s, x + 5, y + h - hh);
	}

	public boolean wasSelected() {
		return selected;
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	public int getLength(DropDown d) {
		return (int) d.getBox().getWidth();
	}

	public int getY(int n, DropDown d) {
		return (int) ((h * n) + d.getBox().getY() + d.getBox().getHeight());
	}

	public int getX(DropDown d) {
		return (int) d.getBox().getX();
	}

	public Rectangle getBox() {
		return new Rectangle(x, y, w, h);
	}

	public String getString() {
		return s;
	}

}