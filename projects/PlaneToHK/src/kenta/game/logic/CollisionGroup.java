/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kenta.game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.phys2d.raw.Body;

/**
 *
 * @author Sactual
 */
public class CollisionGroup {
    private Map<Entity, CollisionData> entities = new HashMap<Entity, CollisionData>();
    private String name;

    public CollisionGroup(String name) { //referred via name
        this.name = name;
    }

    public void addEntity(Entity e, CollisionData cd) {
        entities.put(e, cd);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Body> getBodies() {
        ArrayList<Body> bb = new ArrayList<Body>();
        for (Entity e : entities.keySet()) {
            if (entities.get(e).isCollide())
                bb.add(e.getBody());
        }
        return bb;
    }

    public class CollisionData {
        private boolean canCollide = true;
        
        public CollisionData(boolean collide) {
            this.canCollide = collide;
        }

        /**
         * @return the canCollide
         */
        public boolean isCollide() {
            return canCollide;
        }
    }
}
