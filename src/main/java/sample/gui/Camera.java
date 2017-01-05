package sample.gui;

import sample.Point;

/**
 * Created by Flazher on 05.01.2017.
 */
public class Camera {

    public Point position;
    public Point angle;

    public Camera() {
        this.position = new Point(0f, 0f, 0f);
        this.angle = new Point(0f, 0f, 0f);
    }

}
