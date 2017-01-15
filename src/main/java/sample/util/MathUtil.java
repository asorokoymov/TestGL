package sample.util;

import sample.primitives.Vector3f;

/**
 * Created by Flazher on 14.01.2017.
 */
public class MathUtil {

    public static float dotProduct(Vector3f v, Vector3f u) {
        return v.x*u.x + v.y*u.y + v.z*u.z;
    }

    public static boolean isPowerOfTwo(Integer number) {
        return (number & (number - 1)) == 0;
    }

}
