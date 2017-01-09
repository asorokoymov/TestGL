package sample.input;

import sample.primitives.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Flazher on 06.01.2017.
 */
public class Mouse {

    public Vector2f position;
    public Vector2f delta;
    private Vector2f previousPos;

    private boolean lmbPressed;
    private boolean rmbPressed;

    private Mouse(long windowId) {
        previousPos = new Vector2f(0f, 0f);
        position = new Vector2f(0f, 0f);

        glfwSetMouseButtonCallback(windowId, (window, button, action, mods) -> {
            this.lmbPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            this.rmbPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
        glfwSetCursorPosCallback(windowId, (window, xPos, yPos) -> {
            this.previousPos.x = this.position.x;
            this.previousPos.y = this.position.y;
            this.position.x = (float)xPos;
            this.position.y = (float)yPos;
        });
    }

    public static Mouse init(long windowId) {
        return new Mouse(windowId);
    }

    // TODO: subscription
    public Vector2f getDelta() {
        return position.subtract(previousPos);
    }

    public boolean isLmbPressed() {
        return lmbPressed;
    }

    public boolean isRmbPressed() {
        return rmbPressed;
    }

    @Override
    public String toString() {
        return "Mouse{" +
            "position=" + position +
            '}';
    }
}
