/**
 * Created by Kevin Nash
 * Contact: kieve10@hotmail.com
 * Project: 3D Rubik
 * Class: Cube
 * Description: Holds constants for rendering, determines what colours should be where.
 */

package rubikEngine;

public class Cube {
	private Cubie[] cubie;
	private float[][] relativeCords = {
			//Master core piece
			{ 0f, 0f, 0f}, //master
			
			//Core pieces (do not EVER move relative to each other)
			{ 0f, 0f, 1f}, //front 	centre
			{ 0f, 0f,-1f}, //back 	centre
			{-1f, 0f, 0f}, //left 	centre
			{ 1f, 0f, 0f}, //right 	centre
			{ 0f, 1f, 0f}, //top 	centre
			{ 0f,-1f, 0f}, //bottom centre
			
			//Corner Pieces
			{-1f, 1f, 1f}, //front	top		left	corner
			{ 1f, 1f, 1f}, //front	top 	right	corner
			{-1f,-1f, 1f}, //front	bottom	left	corner
			{ 1f,-1f, 1f}, //front	bottom	right	corner
			{-1f, 1f,-1f}, //back	top		left	corner
			{ 1f, 1f,-1f}, //back	top		right	corner
			{-1f,-1f,-1f}, //back	bottom	left	corner
			{ 1f,-1f,-1f}, //back	bottom	right	corner
			
			//edge pieces
			{ 0f, 1f, 1f}, //front	top
			{-1f, 0f, 1f}, //front	left
			{ 1f, 0f, 1f}, //front	right
			{ 0f,-1f, 1f}, //front	bottom
			{-1f, 1f, 0f}, //mid	top		left
			{ 1f, 1f, 0f}, //mid	top		right
			{-1f,-1f, 0f}, //mid	bottom	left
			{ 1f,-1f, 0f}, //mid	bottom	right
			{ 0f, 1f,-1f}, //back	top
			{-1f, 0f,-1f}, //back	left
			{ 1f, 0f,-1f}, //back	right
			{ 0f,-1f,-1f}  //back	bottom
	};
	
	
	//not used... for now. Maybe not ever!
	private float[][] cornerCords = {
			{-0.5f, 0.5f, 0.5f}, //front	top		left
			{ 0.5f, 0.5f, 0.5f}, //front	top		right
			{-0.5f,-0.5f, 0.5f}, //front	bottom	left
			{ 0.5f,-0.5f, 0.5f}, //front	bottom	right
			{-0.5f, 0.5f,-0.5f}, //back		top		left
			{ 0.5f, 0.5f,-0.5f}, //back		top		right
			{-0.5f,-0.5f,-0.5f}, //back		bottom	left
			{ 0.5f,-0.5f,-0.5f}  //back		bottom	right
	};
	
	private float[][] cubeFaceCords = {
			{ 0.5f, 0.5f, 0.5f}, //front
			{-0.5f, 0.5f, 0.5f},
			{-0.5f,-0.5f, 0.5f},
			{ 0.5f,-0.5f, 0.5f},
			{ 0.5f,-0.5f,-0.5f}, //back
			{-0.5f,-0.5f,-0.5f},
			{-0.5f, 0.5f,-0.5f},
			{ 0.5f, 0.5f,-0.5f},
			{-0.5f, 0.5f, 0.5f}, //left
			{-0.5f, 0.5f,-0.5f},
			{-0.5f,-0.5f,-0.5f},
			{-0.5f,-0.5f, 0.5f},
			{ 0.5f, 0.5f,-0.5f}, //right
			{ 0.5f, 0.5f, 0.5f},
			{ 0.5f,-0.5f, 0.5f},
			{ 0.5f,-0.5f,-0.5f},
			{ 0.5f, 0.5f,-0.5f}, //top
			{-0.5f, 0.5f,-0.5f},
			{-0.5f, 0.5f, 0.5f},
			{ 0.5f, 0.5f, 0.5f},
			{ 0.5f,-0.5f, 0.5f}, //bottom
			{-0.5f,-0.5f, 0.5f},
			{-0.5f,-0.5f,-0.5f},
			{ 0.5f,-0.5f,-0.5f},
	};
	
	private float[][] colours = {
		{0.0f,0.0f,0.0f},	//BLACK
		{1.0f,0.0f,0.0f},	//RED
		{1.0f,0.647f,0.0f},	//ORANGE
		{0.0f,0.0f,1.0f},	//BLUE
		{0.0f,1.0f,0.0f},	//GREEN
		{1.0f,1.0f,0.0f},	//YELLOW
		{1.0f,1.0f,1.0f}	//WHITE
	};
	

	//These colours are located based on a retail Rubik's cube
	/*
	 *  0 - black
	 *  1 - red
	 *  2 - orange
	 *  3 - blue
	 *  4 - green
	 *  5 - yellow
	 *  6 - white
	 *  
	 *  Index:Face Location
	 *  	0:Front
	 *  	1:Back
	 *  	2:Left
	 *  	3:Right
	 *  	4:Top
	 *  	5:Bottom
	 */
	private int[][] solvedColourMap = {
			{0,0,0,0,0,0},
			{1,0,0,0,0,0},
			{0,2,0,0,0,0},
			{0,0,3,0,0,0},
			{0,0,0,4,0,0},
			{0,0,0,0,5,0},
			{0,0,0,0,0,6},
			{1,0,3,0,5,0},
			{1,0,0,4,5,0},
			{1,0,3,0,0,6},
			{1,0,0,4,0,6},
			{0,2,3,0,5,0},
			{0,2,0,4,5,0},
			{0,2,3,0,0,6},
			{0,2,0,4,0,6},
			{1,0,0,0,5,0},
			{1,0,3,0,0,0},
			{1,0,0,4,0,0},
			{1,0,0,0,0,6},
			{0,0,3,0,5,0},
			{0,0,0,4,5,0},
			{0,0,3,0,0,6},
			{0,0,0,4,0,6},
			{0,2,0,0,5,0},
			{0,2,3,0,0,0},
			{0,2,0,4,0,0},
			{0,2,0,0,0,6}
	};
	
	
	//Cubie Cord associated with side rotations
	private int[][] cubieMap = {
			{1,7,8,9,10,15,16,17,18},
			{2,11,12,13,14,23,24,25,26},
			{3,7,9,11,13,16,19,21,24},
			{4,8,10,12,14,17,20,22,25},
			{5,7,8,11,12,15,19,20,23},
			{6,9,10,13,14,18,21,22,26}
	};
	
	//How 4 of 6 colours rotate along the axis determined by the axis they are rotated on
	//One set for a clockwise turn, a separate set for a counter clockwise turn.
	private int[][][] colorRotationMap = {
		{
			{0,1,5,4,2,3},
			{5,4,2,3,0,1},
			{3,2,0,1,4,5}
		},{
			{0,1,4,5,3,2},
			{4,5,2,3,1,0},
			{2,3,1,0,4,5}
		}
	};
	
	protected Cube(){
		initializeCubies();
		allocateColours();
	}
	
	private void initializeCubies(){
		cubie = new Cubie[27];
		cubie[0] = new MasterCubie(0);
		for(int i = 1; i < 7; i++){
			cubie[i] = new CenterCubie(i);	
		}
		for(int i = 7; i < 15; i++){
			cubie[i] = new CornerCubie(i);
		}
		for(int i = 15; i < 27; i++){
			cubie[i] = new EdgeCubie(i);
		}
	}
	
	private void allocateColours(){
		for(int i = 0; i < cubie.length; i++){
			cubie[i].setSideColours(solvedColourMap[i]);
		}
	}

	protected float[][] getRelativeFaces(int positionNumber){
		float[][] returnThis = new float[24][3];
		for(int i = 0; i < 24; i++){
			returnThis[i] = calculateRelativeCords(positionNumber, cubeFaceCords[i]);
		}
		return returnThis;
	}
	
	private float[] calculateRelativeCords(int positionNumber, float[] cords){
		float[] returnThis = new float[3];
		returnThis[0] = cords[0] + relativeCords[positionNumber][0];
		returnThis[1] = cords[1] + relativeCords[positionNumber][1];
		returnThis[2] = cords[2] + relativeCords[positionNumber][2];
		return returnThis;
	}
	
	protected float[] getOffset(int positionNumber){
		return relativeCords[positionNumber];
	}

	protected float[][] getCornerCords(){
		return cornerCords;
	}
	
	protected float[][] getCubeFaceCords(){
		return cubeFaceCords;
	}
	
	protected int[] getColorMap(int position){
		return cubie[position].getSideColours();
	}
	
	protected float[][] getColours(){
		return colours;
	}
	
	protected int[][] getCubieMap(){
		return cubieMap;
	}
	
	protected boolean isSolved(){
		boolean returnThis = true;
		for(int i = 0; i < cubie.length; i++){
			if(cubie[i].getPostionNumber() != cubie[i].getRefrenceNumber()){
				returnThis = false;
				break;
			}
			if(returnThis == false)break;
			for(int x = 0; x < cubie[i].getSideColours().length; x++){
				if(cubie[i].getSideColours()[x] != solvedColourMap[i][x]){
					returnThis = false;
					break;
				}
			}
			if(returnThis == false)break;
		}
		return returnThis;
	}
	
	protected void update(int sideTurning, boolean isPrime){
		int[][] beforeColours = new int[8][6];
		int[] beforePositions = new int[8];
		
		//move the colours to the new cube position
		for(int i = 0; i < 8; i++){
			beforeColours[i] = cubie[cubieMap[sideTurning][i+1]].getSideColours();
			beforePositions[i] = cubie[cubieMap[sideTurning][i+1]].getPostionNumber();
		}
		int[] newPlacements;
		if((!isPrime && (sideTurning == 0 || sideTurning == 1)) || (isPrime && sideTurning != 0 && sideTurning != 1)){
			newPlacements = new int[]{2,0,3,1,5,7,4,6};			      
		}else{
			newPlacements = new int[]{1,3,0,2,6,4,7,5};
		}
		
		for(int i = 0; i < newPlacements.length; i++){
			cubie[cubieMap[sideTurning][i+1]].setSideColours(beforeColours[newPlacements[i]]);
			cubie[cubieMap[sideTurning][i+1]].setPositionNumber(beforePositions[newPlacements[i]]);
		}
		
		//orient the colours to face outwards
		int beforeColour[] = new int[6];
		int firstIndex, secondIndex;
		if(!isPrime){
			firstIndex = 0;
		}else{
			firstIndex = 1;
		}
		if(sideTurning == 0 || sideTurning == 1){
			secondIndex = 0;
		}else if(sideTurning == 2 || sideTurning == 3){
			secondIndex = 1;
		}else{
			secondIndex = 2;
		}
		int[] colourIndices = colorRotationMap[firstIndex][secondIndex];
		for(int i = 1; i < 9; i++){
			beforeColour = cubie[cubieMap[sideTurning][i]].getSideColours();
			cubie[cubieMap[sideTurning][i]].setSideColours(new int[]{
				beforeColour[colourIndices[0]],
				beforeColour[colourIndices[1]],
				beforeColour[colourIndices[2]],
				beforeColour[colourIndices[3]],
				beforeColour[colourIndices[4]],
				beforeColour[colourIndices[5]]
			});
		}
	}
}