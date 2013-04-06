/**
 * Created by Kevin Nash
 * Contact: kieve10@hotmail.com
 * Project: 3D Rubik
 * Class: MasterCubie
 * Description: Determines number of visible faces
 */

package rubikEngine;

public class MasterCubie extends Cubie{
	protected MasterCubie(int refrenceNumber){
		super(refrenceNumber);
		numVisibleFaces = 0;
	}
}
