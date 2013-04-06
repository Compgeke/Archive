/**
 * Created by Kevin Nash
 * Contact: kieve10@hotmail.com
 * Project: 3D Rubik
 * Class: CornerCubie
 * Description: Determines number of visible faces
 */

package rubikEngine;

public class CornerCubie extends Cubie{
	protected CornerCubie(int refrenceNumber) {
		super(refrenceNumber);
		numVisibleFaces = 3;
	}

}
