package sample.bsp.lump;

/**
 * Created by Flazher on 24.11.2016.
 */
public class BSPVector {

    public Float x;
    public Float y;
    public Float z;

    public BSPVector(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "{x: " + this.x + ", y: " + this.y + ", z: " + this.z + "}";
    }
}
