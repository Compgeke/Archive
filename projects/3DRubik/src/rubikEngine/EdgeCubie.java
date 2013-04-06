/**
 * Created by Kevin Nash
 * Contact: kieve10@hotmail.com
 * Project: 3D Rubik
 * Class: EdgeCubie
 * Description: Determines number of visible faces
 */

package rubikEngine;

public class EdgeCubie extends Cubie{

	protected EdgeCubie(int refrenceNumber) {
		super(refrenceNumber);
		numVisibleFaces = 2;
	}
}
