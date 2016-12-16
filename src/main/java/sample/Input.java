package sample;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by Flazher on 06.12.2016.
 */
public class Input extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int nodes) {
        keys[key] = action != GLFW.GLFW_RELEASE;
    }

}
