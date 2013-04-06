import java.util.ArrayList;


public class Board {
	public static final int BOARD_W = 220, BOARD_H = 440, PIECE_SIZE = 22;
	public static int BOARD_X = -1, BOARD_Y = -1;
	public static ArrayList<Piece> pieces = new ArrayList<Piece>();
	
	private static int holdPiece;
	public static boolean canHold = true;
	
	private static int rightState = 0;
	/*
	 * 	Format: XY
	 *  X: Number of blocks
	 *  Y: Which blocks
	 *  
	 *  X = 0
	 *  Y = 0 = FLAT
	 *  
	 *  X = 1
	 *  Y =
	 *  
	 *  1: BLOCK
	 *  2: LEFT
	 *  3: RIGHT
	 *  
	 *  X = 2
	 *  Y =
	 *  
	 *  1: BLOCK + LEFT
	 *  2: BLOCK + RIGHT
	 *  3: LEFT + RIGHT
	 * 
	 */

	private static int leftState = 0;
	/*
	 * Format: XY
	 * X: Current State
	 * Y: 
	 * 
	 * X = 0 (Not set up)
	 * Y = 
	 * 
	 * 0: FLAT (Need T)
	 * 1: ZED (Need T moved in)
	 * 2: ESS (Need T-SPIN)
	 * 
	 * X = 1 (Zed on left)
	 * Y = (DON'T CARE)
	 * 
	 * X = 2 (Zed on right)
	 * Y = (DON'T CARE)
	 * 
	 */
	
	private static int midState = 0;
	/*
	 * Format: XY
	 * X: Placement of last line
	 * 
	 * X = 0
	 * 
	 * Y = 0 place LEFT
	 * Y = 1, place RIGHT
	 * 
	 */

	private static void hold(int piece){
		holdPiece = piece;
		Main.hold();
	}
	
	public static void updateGhost(int piece){
		//MID |||||||||||||||||||||||||||||||||||||||||||||||||
		if(piece == Piece.gline){
			if(midState == 0){
				Main.rotate(1);
				Main.drop();
				midState = 1;
			}else{
				Main.rotate(1);
				Main.right(1);
				Main.drop();
				midState = 0;
			}
		//RIGHT||||||||||||||||||||||||||||||||||||||||||||||||
		}else if(piece == Piece.gblock){
			switch(rightState/10){
				case 0:
					Main.right(3);
					Main.drop();
					rightState = 11;
					break;
				case 1:
					if(canHold){
						hold(piece);
					} else {
						Main.drop();
					}
					break;
				case 2:
					if(rightState%10==3){
						Main.right(3);
						Main.drop();
						rightState = 0;
					}else{
						if(canHold) {
							hold(piece);
						} else {
							Main.drop();
						}
					}
					break;
				case 3:
					rightState = 0;
			}
		}else if(piece == Piece.gel){
			switch(rightState/10){
				case 0:
					Main.rotate(-1);
					Main.right(2);
					Main.drop();
					rightState = 12;
					break;
				case 1:
					if(rightState%10==3){
						Main.rotate(-1);
						Main.right(2);
						Main.drop();
						rightState = 23;
					}else if(rightState%10==1){
						Main.rotate(1);
						Main.right(5);
						Main.drop();
						rightState = 21;
					}else{
						if(canHold) {
							hold(piece);
						} else {
							if(midState == 0){
								Main.rotate(1);
								Main.right(2);
								Main.drop();
							}else if(midState == 1){
								Main.rotate(-1);
								Main.right(1);
								Main.drop();
							}
						}
					}
					break;
				case 2:
					if(rightState%10==2){
						Main.rotate(1);
						Main.right(5);
						Main.drop();
						rightState = 0;
					}else{
						if(canHold) {
							hold(piece);
						} else {
							Main.rotate(1);
							Main.drop();
						}
					}
					break;
				case 3:
					rightState = 0;
			}
		}else if(piece == Piece.grel){
				switch(rightState/10){
					case 0:
						Main.rotate(1);
						Main.right(5);
						Main.drop();
						rightState = 13;
						break;
					case 1:
						if(rightState%10==2){
							Main.rotate(1);
							Main.right(5);
							Main.drop();
							rightState = 23;
						}else if(rightState%10==1){
							Main.rotate(-1);
							Main.right(2);
							Main.drop();
							rightState = 22;
						}else{
							if(canHold) {
								hold(piece);
							} else {
								Main.rotate(-1);
								Main.drop();
							}
						}
						break;
					case 2:
						if(rightState%10==1){
							Main.rotate(-1);
							Main.right(2);
							Main.drop();
							rightState = 0;
						}else{
							if(canHold) {
								hold(piece);
							} else {
								Main.rotate(-1);
								Main.drop();
							}
						}
						break;
					case 3:
						rightState = 0;
				}

		//LEFT ||||||||||||||||||||||||||||||||||||||||||||||||
		}else if(piece == Piece.gtee){
			switch(leftState){
				case 0:
					Main.left(3);
					Main.drop();
					leftState = 10;
					break;
				case 10:
					if(holdPiece == Piece.gtee){
						Main.rotate(-1);
						Main.left(4);
						Main.drop();
						int temp;
						while((temp = Main.getGhost()) == -1)
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						hold(temp);
						Main.rotate(1);
						Main.left(1);
						Main.drop();
						leftState = 20;
					}else {
						if(canHold){
							hold(piece);
						}else{
							Main.rotate(-1);
							Main.drop();
						}
					}
					break;
				case 20:
					if(holdPiece == Piece.gtee){
						Main.rotate(-1);
						Main.left(2);
						Main.drop();
						int temp;
						while((temp = Main.getGhost()) == -1)
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						hold(temp);
						Main.rotate(1);
						Main.left(3);
						Main.drop();
						leftState = 10;
					}else {
						if(canHold){
							hold(piece);
						}else{
							Main.rotate(-1);
							Main.drop();
						}
					}
					break;
					
			}
		}else if(piece == Piece.gzed){
			switch(leftState){
			case 0:
				hold(piece);//TODO T-Spin n shit
				break;
			case 10:
				Main.rotate(1);
				Main.left(1);
				Main.drop();
				break;
			case 20:
				Main.rotate(1);
				Main.left(3);
				Main.drop();
				break;
			}	
		}else if(piece == Piece.gtwo){
			switch(leftState){
			case 0:
				hold(piece);//TODO T-Spin n shit
				break;
			case 10:
				Main.rotate(-1);
				Main.left(4);
				Main.drop();
				break;
			case 20:
				Main.rotate(-1);
				Main.left(2);
				Main.drop();
				break;
			}
		}
		Main.removeMsg("Right State: ");
		Main.addMsg("Right State: " + rightState);
		Main.removeMsg("Mid State: ");
		Main.addMsg("Mid State: " + midState);
		Main.removeMsg("Left State: ");
		Main.addMsg("Left State: " + leftState);
	}
}
