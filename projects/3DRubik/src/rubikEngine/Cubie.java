/**
 * Created by Kevin Nash
 * Contact: kieve10@hotmail.com
 * Project: 3D Rubik
 * Class: Cubie
 * Description: Holds colours and position information for the cubie.
 * 
 * 		Note:
 * 		Reference number is where this piece should be.
 * 		Position number is where this piece is.
 * 
 * 		These will be used when i get around to writing the code that will solve the
 * 		cube for you, using the Fridrich Method. Everyone knows simply resetting the cube
 * 		is not even close to being as entertaining to watch as a true solve.
 */

package rubikEngine;

public class Cubie {
	protected int numVisibleFaces, referenceNumber, positionNumber;
	protected int[] sideColours;
	
	protected Cubie(int referenceNumber){
		this.referenceNumber = referenceNumber;
		positionNumber = referenceNumber;
	}
	
	protected int getVisibleFaces(){
		return numVisibleFaces;
	}
	
	protected int getRefrenceNumber(){
		return referenceNumber;
	}
	
	protected int getPostionNumber(){
		return positionNumber;
	}
	
	protected void setPositionNumber(int positionNumber){
		this.positionNumber = positionNumber;
	}
	
	protected void setSideColours(int[] sideColours){
		this.sideColours = sideColours;
	}
	
	protected int[] getSideColours(){
		return sideColours;
	}
}
