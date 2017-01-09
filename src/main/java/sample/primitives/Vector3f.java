package sample.primitives;

/**
 * Created by Flazher on 06.01.2017.
 */
public class Vector3f {

    public Float x;
    public Float y;
    public Float z;

    public Vector3f(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector3f{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            '}';
    }
}
