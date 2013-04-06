import java.awt.Color;


public class Piece {
	int x, y, piece, rot;
	
	public static final int 
	b1 		= -14277082,
	b2 		= -13684945,
	ghost 	= -10066330,
	line 	= -16618362,
	tee		= -10092442,
	block 	= -6724096,
	zed 	= -16622591,
	two 	= -10092544,
	el 		= -6737152,
	rel 	= -16702346,
	
	gline 	= -14315607,
	gtee	= -7789687,
	gblock 	= -4421341,
	gzed 	= -14319836,
	gtwo 	= -7789789,
	gel 	= -4434397,
	grel 	= -14399591;

	public Piece(int x, int y, int piece, int c) {
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.piece = piece;
	}
	
	public static int calculateRotation(int piece){
		return 0;
	}
}
