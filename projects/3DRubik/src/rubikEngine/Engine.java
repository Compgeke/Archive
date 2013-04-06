/**
 * Created by Kevin Nash
 * Contact: kieve10@hotmail.com
 * Project: 3D Rubik
 * Class: Engine
 * Description: Contains main, handles user input, renders the cube, controls audio and AI.
 * 
 * 	AI functions include scrambling.
 */


package rubikEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.Stack;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Engine {
	private static final int tarFPS = 60;
	private int antiAliasing = 16;
	private boolean running, turning, started, rotating, useTextures, showUI = true, solved = true;
	private static final String title = "Nash Cube";
	private DisplayMode displayMode;
	private static final int[] windowSize = {640,480};
	private float w = windowSize[0]/2, h = windowSize[1]/2;
	private Cube cube;
	private int turnTicks,ticksAI = 0;
	private float turnSpeed = 10f, rotationSpeed = (float) (Math.PI/30), speedAI = 90f, currentSpeed = turnSpeed; // Degrees per frame. 90 for instant.
	private int sideTurning,scrambleLength;
	private boolean isPrime, canMoveAgain, controlledByAI, scrambling, undoing, changingBackground, changingUI;
	private float turnX,turnY,turnZ;
	private Quaternion quatCurrentRotation, quatRotationChange;
	private int[][] scramble;
	private Texture[] colourTextures = new Texture[7], backgroundTextures = new Texture[8];
	private int currentBackground = 0, fontTexture, fontDisplayList, renderFontSize = 8;
    private static final Color OPAQUE_WHITE = new Color(0xFFFFFFFF, true);
    private static final Color TRANSPARENT_BLACK = new Color(0x00000000, true);
    //private NumberFormat numberFormat = NumberFormat.getInstance();
    //numberFormat.setMaximumFractionDigits(2);
    //numberFormat.setMinimumFractionDigits(2);
	private Stack<int[]> appliedMoves;
	private FloatBuffer matrixBuffer;
	private int[] currentControls = {0,1,2,3,4,5};
	private int[][][] controlRotationMap = {
		{
			{0,1,5,4,2,3},
			{3,2,0,1,4,5},
			{5,4,2,3,0,1}
		},{
			{0,1,4,5,3,2},
			{2,3,1,0,4,5},
			{4,5,2,3,1,0}
		}
	};
	
	//OpenAL Buffers
	/*private IntBuffer buffer = BufferUtils.createIntBuffer(1);
	private IntBuffer source = BufferUtils.createIntBuffer(1);
	private FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
	*/
	public static void main(String args[]){	
		new Engine().start();
	}
	
	public void start(){
		try {
			init();
			while(running){
				logic();
				render();
				Display.update();
				Display.sync(tarFPS);
			}
			cleanup();
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
	}
	
	private void init() throws Exception{
		running = true;
		appliedMoves = new Stack<int[]>();
		matrixBuffer = ByteBuffer.allocateDirect(16*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		quatCurrentRotation = new Quaternion();
		quatRotationChange = new Quaternion();
        createWindow();
        cube = new Cube();
        try {
        	loadTextures();
        	useTextures = true;
        } catch(Exception ex) {
        	ex.printStackTrace();
        	useTextures = false;
        }
        initGL();
		AL.create();
        //initAL();
	}
	/*
	private void initAL(){
		AL10.alGenBuffers(buffer);
		WaveData waveFile = WaveData.create(getClass().getResourceAsStream("/res/sound1.wav"));
		AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		AL10.alGenSources(source);
		AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
		AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
		AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
		AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
		AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );		

		AL10.alListener(AL10.AL_POSITION,    listenerPos);
		AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}
	*/
	private void createWindow() throws Exception {
		selectMode();
        Display.setDisplayMode(displayMode);
        Display.setTitle(title);
        Display.setFullscreen(false);
        Display.setIcon(new ByteBuffer[] {
        		loadIcon(getClass().getResource("/res/icon16.png")),
        		loadIcon(getClass().getResource("/res/icon32.png"))
        		});
        createDisplay: while(!started){
        	try {
        		Display.create(new PixelFormat(8,0,0,antiAliasing));
        		started = true;
        	} catch (Exception ex){
        		System.out.println(antiAliasing + "x AntiAliasing Did not work");
        		if(antiAliasing == 1)break;
        		antiAliasing /= 2;
        		continue createDisplay;
        	}
        }
    }
	
	private void selectMode() throws LWJGLException{
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == windowSize[0]
                && d[i].getHeight() == windowSize[1]
                && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
	}
	
	private void initGL() {
		if(useTextures){
	        GL11.glEnable(GL11.GL_TEXTURE_2D); //enable Texture Mapping
		}
        GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading		
        GL11.glClearColor(1.0f, 0.75f, 0.796f, 0.0f); // Pink Background
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix

        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(45.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 25.0f);
        //position camera
        GLU.gluLookAt(5.0f, 3.0f, -5.0f, 0.0f, 0.0f, -10.0f, 0.0f, 1.0f, 0.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }
	
	private void logic(){
        if(Display.isCloseRequested()) {                     // Exit if window is closed
            running = false;
        }
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {       // Exit if Escape is pressed
            running = false;
        }
        
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
        	if(!changingBackground){
        		changingBackground = true;
    			currentBackground++;
        		if(currentBackground == backgroundTextures.length){
        			currentBackground = 0;
        		}
        	}
        }
		if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			changingBackground = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F1) && !changingUI){
			changingUI = true;
			showUI = !showUI;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_F1)){
			changingUI = false;
		}
		if(!turning && canMoveAgain && !rotating && !scrambling && !undoing){//if the cube is not moving
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
	        	isPrime = true;
	        }
	        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
	        	isPrime = false;
	        }
	        for(int i = 0; i < 6; i++){//layer controls
	        	if(Keyboard.isKeyDown(i+2)){
	            	setInputVariables(true, currentControls[i]);
	            	break;
	            }
	        }
	        if(Keyboard.isKeyDown(Keyboard.KEY_X)){//rotation controls
	        	setInputVariables(false, 0);
	        }
	        if(Keyboard.isKeyDown(Keyboard.KEY_Y)){
	        	setInputVariables(false, 1);
	        }
	        if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
	        	setInputVariables(false, 2);
	        }
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){//Start scramble AI
				currentSpeed = speedAI;
				genScramble(25);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_BACK)){
				if(appliedMoves.size() > 0){
					undo();
				}
			}
	        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){//reset the cube
	        	resetCube();
	        }
		}
        if(!controlledByAI
        		&& !Keyboard.isKeyDown(Keyboard.KEY_1)
        		&& !Keyboard.isKeyDown(Keyboard.KEY_2)
        		&& !Keyboard.isKeyDown(Keyboard.KEY_3)
        		&& !Keyboard.isKeyDown(Keyboard.KEY_4)
        		&& !Keyboard.isKeyDown(Keyboard.KEY_5)
        		&& !Keyboard.isKeyDown(Keyboard.KEY_6)){
        	canMoveAgain = true;
        }
	}
	
	private void resetCube(){
    	cube = new Cube();
    	appliedMoves.clear();
    	solved = true;
	}
	
	private void setInputVariables(boolean turn, int side){
		if(turn && !undoing){
			appliedMoves.push(new int[]{side,isPrime?1:0});
		}
    	turning = turn;
    	rotating = !turn;
    	canMoveAgain = false;
    	turnTicks = 0;
    	sideTurning = side;
    	if(turn){
    		turnX = side == 2 || side == 3?1.0f:0.0f;
    		turnY = side == 4 || side == 5?1.0f:0.0f;
    		turnZ = side == 0 || side == 1?1.0f:0.0f;    		
    	}else{
    		turnX = side == 2?1.0f:0.0f;
    		turnY = side == 1?1.0f:0.0f;
    		turnZ = side == 0?1.0f:0.0f;
    	}
    	if((side == 1 || side == 2 || side == 5) && !rotating){
    		isPrime = !isPrime;
    	}
    	//if(turn)AL10.alSourcePlay(source.get(0)); //annoying as hell untill i get a better sound
	}
	
	private void updateControls(){
		int[] beforeControls = new int[6];
		System.arraycopy(currentControls, 0, beforeControls, 0, 6);
		int[] controlIndices = controlRotationMap[isPrime?1:0][sideTurning];
		for(int i = 0; i < 6; i++){
			currentControls[i] = beforeControls[controlIndices[i]];
		}
	}
	
	private void scrambleTick(){
		controlledByAI = true;
		if(ticksAI < scrambleLength){
			if(scramble[ticksAI][1] == 0){
				isPrime = false;
			}else{
				isPrime = true;
			}
			setInputVariables(true,scramble[ticksAI][0]);
		}else{
			ticksAI = 0;
			currentSpeed = turnSpeed;
			controlledByAI = false;
			scrambling = false;
		}
	}
	
	private void genScramble(int length){
		scrambling = true;
		scrambleLength = length;
		scramble = new int[length][2];
		int lastMove,nextMove;
		Random rn = new Random();
		for(int i = 0; i < length; i++){
			if(i == 0){
				nextMove = rn.nextInt(6);
			}else{
				lastMove = scramble[i-1][0];
				if(lastMove == 0 || lastMove == 1){
					nextMove = rn.nextInt(4) + 2;
				}else if(lastMove == 2 || lastMove == 3){
					nextMove = rn.nextInt(4);
					if(nextMove == 2 || nextMove == 3)nextMove+=2;
				}else{
					nextMove = rn.nextInt(4);
				}
			}
			scramble[i][0] = nextMove;
			scramble[i][1] = rn.nextInt(2);
		}
		scrambleTick();
	}

	
	private void undo(){
		undoing = true;
		int[] undoMove = appliedMoves.pop();
		if(undoMove[1] == 1){
			isPrime = false;
		}else{
			isPrime = true;
		}
		setInputVariables(true,undoMove[0]);
	}
	
	private void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		float[][] cubieColours = cube.getColours();
		float[][] cubieFaceVerticies = cube.getCubeFaceCords();
		if(rotating){
			currentSpeed = rotationSpeed;
			quatRotationChange.setFromAxisAngle(new Vector4f(turnX, turnY, turnZ, (isPrime? -1:1) * currentSpeed));		
			Quaternion.mul(quatCurrentRotation, quatRotationChange, quatCurrentRotation);
			quatCurrentRotation.normalise();
			createMatrixBuffer();
		}		
		
		GL11.glLoadIdentity();
		
		enable2D();
		renderBackground();
		if(showUI){
			GL11.glColor3f(0.0f, 0.0f, 1.0f);
			float trackLines = -240;
			glPrint("Controls:",-300f,trackLines+=renderFontSize + 1);
			glPrint("1-6         - Turn sides",-300f,trackLines+=renderFontSize + 1);
			glPrint("X,Y,Z       - Rotate cube",-300f,trackLines+=renderFontSize + 1);
			glPrint("Left Shift  - Reverse directions (hold)",-300f,trackLines+=renderFontSize + 1);
			glPrint("Right Shift - Reset Cube",-300f,trackLines+=renderFontSize + 1);
			glPrint("Enter       - Scramble",-300f,trackLines+=renderFontSize + 1);
			glPrint("Backspace   - Undo",-300f,trackLines+=renderFontSize + 1);
			glPrint("Space       - Cycle background image",-300f,trackLines+=renderFontSize + 1);
			if(solved)GL11.glColor3f(0.0f, 1.0f, 0.0f);
			else GL11.glColor3f(1.0f, 0.0f, 0.0f);
			glPrint(solved?"SOLVED!":"Keep trying.",220f,-220f);
		}
		disable2D();
		
		
		GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
		GL11.glTranslatef(0f, 0f, -10f);
		
		createMatrixBuffer();
		GL11.glMultMatrix(matrixBuffer);
		
		for(int i = 0; i < 27; i++){
			float[] cubiePosition = cube.getOffset(i);
			int[] cubieColourMap = cube.getColorMap(i);
			GL11.glPushMatrix();
			if(turning && inTurnMap(i,sideTurning)){
				GL11.glRotatef((isPrime? 1:-1) * currentSpeed * turnTicks, turnX, turnY, turnZ);
			}
			GL11.glTranslatef(cubiePosition[0], cubiePosition[1], cubiePosition[2]);
			for(int x = 0; x < 24; x++){
				if(useTextures)colourTextures[cubieColourMap[x/4]].bind();
				if(x%4==0)GL11.glBegin(GL11.GL_QUADS);
				if(useTextures){
					switch(x%4){
						case 0:
							GL11.glTexCoord2f(0.0f, 0.0f);
							break;
						case 1:
							GL11.glTexCoord2f(1.0f, 0.0f);
							break;
						case 2:
							GL11.glTexCoord2f(1.0f, 1.0f);
							break;
						default:
							GL11.glTexCoord2f(0.0f, 1.0f);
							break;
					}
					GL11.glVertex3f(cubieFaceVerticies[x][0], cubieFaceVerticies[x][1], cubieFaceVerticies[x][2]);
				}else{
					GL11.glColor3f(cubieColours[cubieColourMap[x/4]][0],cubieColours[cubieColourMap[x/4]][1],cubieColours[cubieColourMap[x/4]][2]);
					GL11.glVertex3f(cubieFaceVerticies[x][0], cubieFaceVerticies[x][1], cubieFaceVerticies[x][2]);
				}
				if(x%4 == 3)GL11.glEnd();
			}
			GL11.glPopMatrix();
		}
		
		if(turning || rotating || controlledByAI || undoing){
			turnTicks++;
			if(!rotating && turnTicks*currentSpeed >= 90){
				if(turning){
					turning = false;
					cube.update(sideTurning, isPrime);
					solved = cube.isSolved();
					if(solved)appliedMoves.clear();
					//AL10.alSourcePlay(source.get(0));
					//AL10.alSourceStop(source.get(0));
				}else if(controlledByAI){
					ticksAI++;
					scrambleTick();
				}else if(undoing){
					undoing = false;
				}else{
					rotating = false;
					currentSpeed = turnSpeed;
				}
			}else if(rotating){
				if(turnTicks*currentSpeed >= Math.PI/2){
					updateControls();
					rotating = false;
					currentSpeed = turnSpeed;
				}
			}
		}
	}
	
	private void enable2D(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(-w, w, h, -h);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		 
		GL11.glLoadIdentity();		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private void disable2D(){		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
	}
	
	private void render2DQuad(float x, float y){
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2f(-x,-y);
		
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex2f(x,-y);
		
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex2f(x, y);
		
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex2f(-x, y);
		
		GL11.glEnd();
	}
	
	private void renderBackground(){
		if(useTextures)backgroundTextures[currentBackground].bind();
		render2DQuad(w,h);
	}
	
	
	
	private boolean inTurnMap(int test, int turning){
		int[] map = cube.getCubieMap()[turning];
		boolean returnThis = false;
		for(int i = 0; i < 9; i++){
			if(test == map[i])returnThis = true;
		}
		return returnThis;
	}
	
	private void cleanup(){
		//AL10.alDeleteSources(source);
		//AL10.alDeleteBuffers(buffer);
		//AL.destroy();
		if(colourTextures != null){
			for(int i = 0; i < colourTextures.length; i++){
				colourTextures[i].release();
			}
		}
        Display.destroy();		
	}
	
	private static ByteBuffer loadIcon(URL url) throws IOException {
        InputStream is = url.openStream();
        try {
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            return bb;
        } finally {
            is.close();
        }
    }
	private Texture getTexture(String name) throws IOException {
		return TextureLoader.getTexture("BMP",getClass().getResourceAsStream(name));
	}
	private void loadTextures() throws FileNotFoundException, IOException {
		System.out.println("Loading textures...");
		colourTextures[0] = getTexture("/res/black.bmp");
		colourTextures[1] = getTexture("/res/red.bmp");
		colourTextures[2] = getTexture("/res/orange.bmp");
		colourTextures[3] = getTexture("/res/blue.bmp");
		colourTextures[4] = getTexture("/res/green.bmp");
		colourTextures[5] = getTexture("/res/yellow.bmp");
		colourTextures[6] = getTexture("/res/white.bmp");
		System.out.println("Loaded cube face textures.");
		System.out.println("Loading background textures...");
		
		for(int i = 0; i < backgroundTextures.length; i++){
			System.out.print(i + "..." + (i == backgroundTextures.length-1?"\n":""));
			backgroundTextures[i] = getTexture("/res/back" + i + ".bmp");
		}
		System.out.println("Background Textures loaded.");
		System.out.println("Buiding fonts...");
		buildFont();
		System.out.println("Textures loaded.");
    }
	
	private void createMatrixBuffer(){
		float w,x,y,z;
		w = quatCurrentRotation.w;
		x = quatCurrentRotation.x;
		y = quatCurrentRotation.y;
		z = quatCurrentRotation.z;
		Matrix4f m = new Matrix4f();
		
		m.m00 = sq(w) + sq(x) - sq(y) - sq(z);
		m.m01 = 2 * x * y + 2 * w * z;
		m.m02 = 2 * x * z - 2 * w * y;
		m.m03 = 0;

		m.m10 = 2 * x * y - 2 * w * z;
		m.m11 = sq(w) - sq(x) + sq(y) - sq(z);
		m.m12 = 2 * y * z + 2 * w * x;
		m.m13 = 0;
		
		m.m20 = 2 * x * z + 2 * w * y;
		m.m21 = 2 * y * z - 2 * w * x;
		m.m22 = sq(w) - sq(x) - sq(y) + sq(z);
		m.m23 = 0;

		m.m30 = 0;
		m.m31 = 0;
		m.m32 = 0;
		m.m33 = sq(w) + sq(x) + sq(y) + sq(z);
		
		matrixBuffer.clear();
		m.storeTranspose(matrixBuffer);
		matrixBuffer.flip();
	}
	
	private float sq(float x){
		return x*x;
	}
	
	private void glPrint(String msg, float x, float y) {                                      // Custom GL "Print" Routine
        if(msg != null) {
        	GL11.glPushMatrix();
        	GL11.glTranslatef(x, y, 0.0f);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture);
            for(int i=0;i<msg.length();i++) {
                GL11.glCallList(fontDisplayList + msg.charAt(i));
                GL11.glTranslatef(renderFontSize, 0.0f, 0.0f);
            }
            GL11.glPopMatrix();
        }
    }
	
	private void buildFont() {                          // Build Our Bitmap Font
        Font font;                                      // Font object

        /* Note that I have set the font to Courier New.  This font is not guaranteed to be on all
         * systems.  However it is very common so it is likely to be there.  You can replace this name
         * with any named font on your system or use the Java default names that are guaranteed to be there.
         * Also note that this will work well with monospace fonts, but does not look as good with
         * proportional fonts.
         */
        String fontName = "Courier New";                // Name of the font to use
        int fontSize = 72;
        BufferedImage fontImage;                        // image for creating the bitmap
        int bitmapSize = 512;                           // set the size for the bitmap texture
        boolean sizeFound = false;
        boolean directionSet = false;
        int delta = 0;

        /* To find out how much space a Font takes, you need to use a the FontMetrics class.
         * To get the FontMetrics, you need to get it from a Graphics context.  A Graphics context is
         * only available from a displayable surface, ie any class that subclasses Component or any Image.
         * First the font is set on a Graphics object.  Then get the FontMetrics and find out the width
         * and height of the widest character (W).  Then take the largest of the 2 values and find the
         * maximum size font that will fit in the size allocated.
         */
        while(!sizeFound) {
            font = new Font(fontName, Font.PLAIN, fontSize);  // Font Name
            // use BufferedImage.TYPE_4BYTE_ABGR to allow alpha blending
            fontImage = new BufferedImage(bitmapSize, bitmapSize, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D)fontImage.getGraphics();
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth("W");
            int height = fm.getHeight();
            int lineWidth = (width > height) ? width * 16 : height * 16;
            if(!directionSet) {
                if(lineWidth > bitmapSize) {
                    delta = -2;
                }
                else {
                    delta = 2;
                }
                directionSet = true;
            }
            if(delta > 0) {
                if(lineWidth < bitmapSize) {
                    fontSize += delta;
                }
                else {
                    sizeFound = true;
                    fontSize -= delta;
                }
            }
            else if(delta < 0) {
                if(lineWidth > bitmapSize) {
                    fontSize += delta;
                }
                else {
                    sizeFound = true;
                    fontSize -= delta;
                }
            }
        }

        /* Now that a font size has been determined, create the final image, set the font and draw the
         * standard/extended ASCII character set for that font.
         */
        font = new Font(fontName, Font.BOLD, fontSize);  // Font Name
        // use BufferedImage.TYPE_4BYTE_ABGR to allow alpha blending
        fontImage = new BufferedImage(bitmapSize, bitmapSize, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D)fontImage.getGraphics();
        g.setFont(font);
        g.setColor(OPAQUE_WHITE);
        g.setBackground(TRANSPARENT_BLACK);
        FontMetrics fm = g.getFontMetrics();
        for(int i=0;i<256;i++) {
            int x = i % 16;
            int y = i / 16;
            char ch[] = {(char)i};
            String temp = new String(ch);
            g.drawString(temp, (x * 32) + 1, (y * 32) + fm.getAscent());
        }

        /* The following code is taken directly for the LWJGL example code.
         * It takes a Java Image and converts it into an OpenGL texture.
         * This is a very powerful feature as you can use this to generate textures on the fly out
         * of anything.
         */
        //      Flip Image
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -fontImage.getHeight(null));
        AffineTransformOp op =
            new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        fontImage = op.filter(fontImage, null);

        // Put Image In Memory
        ByteBuffer scratch =
            ByteBuffer.allocateDirect(
                4 * fontImage.getWidth() * fontImage.getHeight());

        byte data[] =
            (byte[])fontImage.getRaster().getDataElements(
                0,
                0,
                fontImage.getWidth(),
                fontImage.getHeight(),
                null);
        scratch.clear();
        scratch.put(data);
        scratch.rewind();

        // Create A IntBuffer For Image Address In Memory
        IntBuffer buf =
            ByteBuffer
                .allocateDirect(4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        GL11.glGenTextures(buf); // Create Texture In OpenGL

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));
        // Typical Texture Generation Using Data From The Image

        // Linear Filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        // Linear Filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        // Generate The Texture
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, fontImage.getWidth(), fontImage.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, scratch);


        fontTexture = buf.get(0);                           // Return Image Address In Memory

        fontDisplayList = GL11.glGenLists(256);                    // Storage For 256 Characters

        /* Generate the display lists.  One for each character in the standard/extended ASCII chart.
         */
        float textureDelta = 1.0f / 16.0f;
        for(int i=0;i<256;i++) {
            float u = ((float)(i % 16)) / 16.0f;
            float v = 1.f - (((float)(i / 16)) / 16.0f);
            GL11.glNewList(fontDisplayList + i, GL11.GL_COMPILE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture);
            GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(u, v);
                GL11.glVertex3f(-renderFontSize, -renderFontSize, 0.0f);
                GL11.glTexCoord2f((u + textureDelta), v);
                GL11.glVertex3f(renderFontSize, -renderFontSize, 0.0f);
                GL11.glTexCoord2f((u + textureDelta), v - textureDelta);
                GL11.glVertex3f(renderFontSize, renderFontSize, 0.0f);
                GL11.glTexCoord2f(u, v - textureDelta);
                GL11.glVertex3f(-renderFontSize, renderFontSize, 0.0f);
            GL11.glEnd();
            GL11.glEndList();
        }
    }
}