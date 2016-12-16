package sample.util;

import static org.lwjgl.opengl.GL11.glFrustum;

/**
 * Created by Flazher on 29.11.2016.
 */
public class GLUtil {

    public static void perspectiveGL(float fov, float aspect, float zNear, float zFar) {
        float fH = (float) Math.tan(fov / 360 * Math.PI) * zNear;
        float fW = fH * aspect;
        glFrustum( -fW, fW, -fH, fH, zNear, zFar );
    }

}
