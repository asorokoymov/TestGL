package sample.bsp.lump;

import java.util.Vector;

/**
 * Created by Flazher on 24.11.2016.
 */
public class BSPPlane {

    private BSPVector normal;
    private Float dist;
    private Integer type;

    public BSPPlane(BSPVector normal, Float dist, Integer type) {
        this.normal = normal;
        this.dist = dist;
        this.type = type;
    }

    public BSPVector getNormal() {
        return normal;
    }

    public Float getDist() {
        return dist;
    }

    public Integer getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[normal: " + this.normal.toString() + ", dist: " + this.dist + ", type: " + this.type + "]";
    }
}
