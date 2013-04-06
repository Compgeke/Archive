package userInterface;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;


public class CheckBox{
	Rectangle box;
	Color txt;
	Color fill;
	Color stroke;
	int x;
	int y;
	String s;
	boolean selected;

	public CheckBox(String sa, int xa, int ya, Color text,
			Color fil, Color strok) {// a 90 X 10 box
		this.box = new Rectangle(xa + 90, ya - 10, 10, 10);
		this.stroke = strok;
		this.fill = fil;
		this.txt = text;
		this.x = xa;
		this.y = ya;
		this.s = sa;
	}

	public CheckBox(String sa, int xa, int ya) {
		this(sa, xa, ya, Color.BLACK, new Color(135, 137, 0), Color.BLACK);
	}

	public Rectangle selectionBox() {
		return box;
	}

	public void draw(Graphics g) {
		Graphics2D g1 = (Graphics2D) g;

		BasicStroke bs = new BasicStroke(1);
		FontRenderContext frc = g1.getFontRenderContext();
		Font font = g1.getFont().deriveFont(12f);
		LineMetrics lm = getLM(font, s, frc);
		int y2 = (int) lm.getAscent();

		g1.setColor(txt);
		g1.drawString(s, x, y);
		g1.setStroke(bs);
		g1.setColor(fill);
		g1.fillRect(x + 90, y - y2, 10, 10);
		g1.setColor(stroke);
		g1.drawRect(x + 90, y - y2, 10, 10);
		int boxX = (int) box.getX();
		int boxY = (int) box.getY();
		int boxW = (int) box.getWidth();
		int boxH = (int) box.getHeight();

		if (isSelected() == true) {
			g1.drawLine(boxX, boxY, boxX + boxW, boxY + boxH);
			g1.drawLine(boxX+ boxW, boxY, boxX , boxY + boxH);
		}
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected() {
		if (selected == false) {
			selected = true;
		} else if (selected == true) {
			selected = false;
		}
	}
	
	private static LineMetrics getLM(Font font, String s, FontRenderContext frc) {
		return font.getLineMetrics(s, frc);
	}
}