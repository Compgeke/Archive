/**
 * Created by Kevin Nash
 * Contact: kieve10@hotmail.com
 * Project: 3D Rubik
 * Class: CenterCubie
 * Description: Determines number of visible faces
 */

package rubikEngine;

public class CenterCubie extends Cubie{
	protected CenterCubie(int refrenceNumber) {
		super(refrenceNumber);
		numVisibleFaces = 1;
	}

}
