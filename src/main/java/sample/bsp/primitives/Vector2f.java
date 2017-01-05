package sample.bsp.primitives;

/**
 * Created by Flazher on 06.01.2017.
 */
public class Vector2f {

    public Float x;
    public Float y;

    public Vector2f(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f subtract(Vector2f vector) {
        return new Vector2f(this.x - vector.x, this.y - vector.y);
    }

    @Override
    public String toString() {
        return "Vector2f{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
