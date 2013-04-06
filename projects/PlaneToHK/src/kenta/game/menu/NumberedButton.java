package kenta.game.menu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import kenta.game.MainGame;
import org.newdawn.slick.Color;

/**
 * 
 * @author Kevin Nash
 */
public class NumberedButton {
    public static final int WIDTH = 50, HEIGHT = 50;
    private int x,y,num;
    private boolean clicked = false;
	
    public NumberedButton(int x, int y, int num){
        this.x = x;
        this.y = y;
        this.num = num;
    }
	
    public int getX(){return x;}
    public int getY(){return y;}
    public int getMap(){return num-1;}
    

    public void render(Input i, Graphics g){
        g.pushTransform();
        if(mouseOver(i.getMouseX(), i.getMouseY())){
            if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
                g.setColor(Color.blue);
                g.fillRect(x, y, WIDTH, HEIGHT);
                g.setColor(Color.black);
                g.drawString(Integer.toString(num), x, y);
                clicked = true;
            }else{
                g.setColor(Color.red);
                g.fillRect(x, y, WIDTH, HEIGHT);
                g.setColor(Color.black);
                g.drawString(Integer.toString(num), x, y);
            }
	}else{
            g.setColor(Color.white);
            g.fillRect(x, y, WIDTH, HEIGHT);
            g.setColor(Color.black);
            g.drawString(Integer.toString(num), x, y);
	}
        g.popTransform();
    }
	
    public boolean mouseOver(int mouseX, int mouseY){
        if(mouseX > x && mouseX < x + WIDTH
                && mouseY > y && mouseY < y + HEIGHT){
            return true;
	}else{
            return false;
	}
    }
	
    public boolean clicked(){
        return clicked;
    }
    
    public void release(){
        clicked = false;
    }      
}