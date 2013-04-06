/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kenta.game.object;

import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author Sactual
 */
public class RainDrop extends Polygon {

    public RainDrop(int size) {
        super();

        /** coordinates of vertices of polygon(rain drop) */
        this.addPoint(0, 0);
        this.addPoint(0, size);
    }
}
