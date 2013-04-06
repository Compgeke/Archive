package kenta.game.menu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import kenta.game.MainGame;
import org.newdawn.slick.Image;

/**
 * 
 * @author Kevin Nash
 */
public class Button {
    private int x = -1,y;
    private Image defaultImage, overImage, pressedImage;
    private boolean clicked = false;
	
    public Button(int x, int y, Image defaultImage, Image overImage, Image pressedImage){
        this(y,defaultImage,overImage,pressedImage);
	this.x=x;
    }

    public Button(int y, Image defaultImage, Image overImage, Image pressedImage){
        this.y=y;
	this.defaultImage = defaultImage;
	this.overImage = overImage;
	this.pressedImage = pressedImage;
	if(x == -1){
            x = getCenterX();
	}
    }
	
    public int getX(){return x;}
    public int getY(){return y;}
    public Image getDefaultImage(){return defaultImage;}
    public Image getOverImage(){return overImage;}
    public Image getPressedImage(){return pressedImage;}

    public void render(Input i, Graphics g){
        if(mouseOver(i.getMouseX(), i.getMouseY())){
            if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
                g.drawImage(pressedImage, x, y);
                clicked = true;
            }else{
                g.drawImage(overImage, x, y);
            }
	}else{
            g.drawImage(defaultImage, x, y);
	}
    }
	
    public boolean mouseOver(int mouseX, int mouseY){
        if(mouseX > x && mouseX < x + defaultImage.getWidth()
                && mouseY > y && mouseY < y + defaultImage.getHeight()){
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
        
    public int getCenterX(){
        return (MainGame.WIDTH-defaultImage.getWidth())/2;
    }        
}