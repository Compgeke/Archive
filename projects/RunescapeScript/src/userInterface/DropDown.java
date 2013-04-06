package userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class DropDown {
	int x;
	int y;
	int l;
	int h;
	Color c1;
	Color c2;
	Color c3;
	String d;
	boolean open;
	MenuItem prev;

	ArrayList<MenuItem> items = new ArrayList<MenuItem>();

	public DropDown(int xa, int ya, int length, int height, String da,
			Color cc1, Color cc2, Color cc3) {
		this.x = xa;
		this.y = ya;
		this.l = length;
		this.h = height;
		this.c1 = cc1;
		this.c2 = cc2;
		this.c3 = cc3;
		this.d = da;
	}
	
	public DropDown(int xa, int ya, String da) {
		this(xa,ya,50, 20, da, Color.BLACK, Color.WHITE, Color.BLACK);
	}

	public void draw(Graphics g) {
		Graphics2D g1 = (Graphics2D) g;
		BasicStroke bs = new BasicStroke(1);
		g1.setStroke(bs);
		int hh = (int) getBox().getHeight() / 2;
		int hhh = hh + (hh / 2);
		g.setColor(c3);
		g.drawRect(x, y, l, h);
		g.setColor(c2);
		g.fillRect(x, y, l, h);
		g.setColor(c1);
		g.drawString(getString(), x + 5, y + hhh);
		if (getOpened()) {
			drawMenuItems(g, c1, c2, c3);
		}
	}

	private String getString() {
		if (wasSelected() == null) {
			if (getPrev() != null) {
				return getPrev().getString();
			} else {
				return d;
			}
		} else {
			return wasSelected().getString();
		}
	}

	private void drawMenuItems(Graphics g, Color c1, Color c2, Color c3) {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).draw(g, c1, c2, c3);
		}
	}

	public MenuItem wasSelected() {
		for (MenuItem i : items) {
			if (i.wasSelected()) {
				return i;
			}
		}
		return null;
	}

	public boolean getOpened() {
		return open;
	}

	public void setOpen(boolean b) {
		open = b;
	}

	public Rectangle getBox() {
		return new Rectangle(x, y, l, h);
	}

	public void add(MenuItem i) {
		items.add(i);
	}

	public ArrayList<MenuItem> getMenuItems() {
		return items;
	}

	public Rectangle fullBox() {
		return new Rectangle(x, y, l, fullHeight());
	}

	private int fullHeight() {
		int h1 = h;
		int h2 = 0;
		for (int i = 0; i <= items.size() - 1; i++) {
			h2 = (int) (h2 + items.get(0).getBox().getHeight());
		}
		return h1 + h2;
	}

	public void setPrev(MenuItem i) {
		prev = i;
	}

	public MenuItem getPrev() {
		return prev;
	}

	public void setSelected(boolean b, MenuItem i) {
		if (b == true) {
			for (MenuItem t : getMenuItems()) {
				if (t.wasSelected()) {
					setPrev(t);
				}
			}
			i.setSelected(true);
		} else {
			for (MenuItem t : getMenuItems()) {
				if (t.wasSelected()) {
					setPrev(t);
				}
			}
			i.setSelected(false);
		}
	}
	
	public MenuItem getSelected() {
		for(MenuItem i: items) {
			if (i.wasSelected()) {
				return i;
			}
		}
		return null;
	}
}