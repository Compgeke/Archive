package engine;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class Start {
	 Robot robot;
	 int[][] locations = {{152,747},{722,430},{600,490},{675,632}};//{{400,1030},{890,703},{840,773},{840,918}};
	 String path = "C:\\Users\\Amber\\Desktop\\Upload Photos\\Renamed\\";
	 int totalPics = new File(path).listFiles().length;
	 int startNum = 1;
	 int startFrom = 72;

	public static void main(String[] args) throws AWTException {
		new Start().start();
	}
	
	public void start() throws AWTException{
		robot = new Robot();
		
		robot.setAutoDelay(2);
	    robot.setAutoWaitForIdle(true);
	    
	    robot.mouseMove(locations[0][0],locations[0][1]);
	    leftClick();
	    for(int i = startFrom; i < totalPics; i++){
		    robot.keyPress(KeyEvent.VK_END);
		    robot.keyRelease(KeyEvent.VK_END);
		    robot.mouseMove(locations[1][0],locations[1][1]);
		    leftClick();
		    robot.delay(300);
		    type(path + getNum(i+1) + ".JPG");
		    robot.keyPress(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);
		    robot.delay(300);
		    robot.mouseMove(locations[2][0],locations[2][1]);
		    leftClick();
		    robot.delay(500);
		    type(getNum(i+startNum));
		    robot.mouseMove(locations[3][0], locations[3][1]);
		    leftClick();
		    robot.delay(10000);
	    }
	}
	
	public String getNum(int i){
		String temp = Integer.toString(i);
		String finalString;
		if(temp.length() == 3){
			finalString = temp;
		} else if(temp.length() == 2){
			finalString = "0" + temp;
		} else{
			finalString = "00" + temp;
			System.out.println(finalString);
		}
		return finalString;
	}
	
	private void leftClick(){
		robot.mousePress(InputEvent.BUTTON1_MASK);
	    robot.delay(200);
	    robot.mouseRelease(InputEvent.BUTTON1_MASK);
	    robot.delay(200);
	}
	
	public void type(CharSequence characters){
		int length = characters.length();
		for (int i = 0; i < length; i++){
			char character = characters.charAt(i);
			type(character);
		}
	}
	public void type(char character){
		switch(character){
			case 'a': doType(KeyEvent.VK_A); break;
			case 'b': doType(KeyEvent.VK_B); break;
			case 'c': doType(KeyEvent.VK_C); break;
			case 'd': doType(KeyEvent.VK_D); break;
			case 'e': doType(KeyEvent.VK_E); break;
			case 'f': doType(KeyEvent.VK_F); break;
			case 'g': doType(KeyEvent.VK_G); break;
			case 'h': doType(KeyEvent.VK_H); break;
			case 'i': doType(KeyEvent.VK_I); break;
			case 'j': doType(KeyEvent.VK_J); break;
			case 'k': doType(KeyEvent.VK_K); break;
			case 'l': doType(KeyEvent.VK_L); break;
			case 'm': doType(KeyEvent.VK_M); break;
			case 'n': doType(KeyEvent.VK_N); break;
			case 'o': doType(KeyEvent.VK_O); break;
			case 'p': doType(KeyEvent.VK_P); break;
			case 'q': doType(KeyEvent.VK_Q); break;
			case 'r': doType(KeyEvent.VK_R); break;
			case 's': doType(KeyEvent.VK_S); break;
			case 't': doType(KeyEvent.VK_T); break;
			case 'u': doType(KeyEvent.VK_U); break;
			case 'v': doType(KeyEvent.VK_V); break;
			case 'w': doType(KeyEvent.VK_W); break;
			case 'x': doType(KeyEvent.VK_X); break;
			case 'y': doType(KeyEvent.VK_Y); break;
			case 'z': doType(KeyEvent.VK_Z); break;
			case 'A': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_A); break;
			case 'B': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_B); break;
			case 'C': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_C); break;
			case 'D': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_D); break;
			case 'E': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_E); break;
			case 'F': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_F); break;
			case 'G': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_G); break;
			case 'H': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_H); break;
			case 'I': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_I); break;
			case 'J': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_J); break;
			case 'K': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_K); break;
			case 'L': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_L); break;
			case 'M': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_M); break;
			case 'N': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_N); break;
			case 'O': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_O); break;
			case 'P': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_P); break;
			case 'Q': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Q); break;
			case 'R': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_R); break;
			case 'S': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_S); break;
			case 'T': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_T); break;
			case 'U': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_U); break;
			case 'V': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_V); break;
			case 'W': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_W); break;
			case 'X': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_X); break;
			case 'Y': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Y); break;
			case 'Z': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Z); break;
			case '`': doType(KeyEvent.VK_BACK_QUOTE); break;
			case '0': doType(KeyEvent.VK_0); break;
			case '1': doType(KeyEvent.VK_1); break;
			case '2': doType(KeyEvent.VK_2); break;
			case '3': doType(KeyEvent.VK_3); break;
			case '4': doType(KeyEvent.VK_4); break;
			case '5': doType(KeyEvent.VK_5); break;
			case '6': doType(KeyEvent.VK_6); break;
			case '7': doType(KeyEvent.VK_7); break;
			case '8': doType(KeyEvent.VK_8); break;
			case '9': doType(KeyEvent.VK_9); break;
			case '-': doType(KeyEvent.VK_MINUS); break;
			case '=': doType(KeyEvent.VK_EQUALS); break;
			case '~': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE); break;
			case '!': doType(KeyEvent.VK_EXCLAMATION_MARK); break;
			case '@': doType(KeyEvent.VK_AT); break;
			case '#': doType(KeyEvent.VK_NUMBER_SIGN); break;
			case '$': doType(KeyEvent.VK_DOLLAR); break;
			case '%': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_5); break;
			case '^': doType(KeyEvent.VK_CIRCUMFLEX); break;
			case '&': doType(KeyEvent.VK_AMPERSAND); break;
			case '*': doType(KeyEvent.VK_ASTERISK); break;
			case '(': doType(KeyEvent.VK_LEFT_PARENTHESIS); break;
			case ')': doType(KeyEvent.VK_RIGHT_PARENTHESIS); break;
			case '_': doType(KeyEvent.VK_UNDERSCORE); break;
			case '+': doType(KeyEvent.VK_PLUS); break;
			case '\t': doType(KeyEvent.VK_TAB); break;
			case '\n': doType(KeyEvent.VK_ENTER); break;
			case '[': doType(KeyEvent.VK_OPEN_BRACKET); break;
			case ']': doType(KeyEvent.VK_CLOSE_BRACKET); break;
			case '\\': doType(KeyEvent.VK_BACK_SLASH); break;
			case '{': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET); break;
			case '}': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET); break;
			case '|': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH); break;
			case ';': doType(KeyEvent.VK_SEMICOLON); break;
			case ':': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON); break;
			case '\'': doType(KeyEvent.VK_QUOTE); break;
			case '"': doType(KeyEvent.VK_QUOTEDBL); break;
			case ',': doType(KeyEvent.VK_COMMA); break;
			case '<': doType(KeyEvent.VK_LESS); break;
			case '.': doType(KeyEvent.VK_PERIOD); break;
			case '>': doType(KeyEvent.VK_GREATER); break;
			case '/': doType(KeyEvent.VK_SLASH); break;
			case '?': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH); break;
			case ' ': doType(KeyEvent.VK_SPACE); break;
			default:throw new IllegalArgumentException("Cannot type character " + character);
		}
	}
	private void doType(int... keyCodes){
		doType(keyCodes, 0, keyCodes.length);
	}
	private void doType(int[] keyCodes, int offset, int length){
		if (length == 0) {return;}
		robot.keyPress(keyCodes[offset]);
		doType(keyCodes, offset + 1, length - 1);
		robot.keyRelease(keyCodes[offset]);
	}
}