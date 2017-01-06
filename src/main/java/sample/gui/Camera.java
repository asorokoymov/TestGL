package sample.gui;

import sample.Point;

/**
 * Created by Flazher on 05.01.2017.
 */
public class Camera {

    private Point position;
    private Point angle;

    public Camera() {
        this.position = new Point(0f, 0f, 0f);
        this.angle = new Point(0f, 0f, 0f);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getAngle() {
        return angle;
    }

    public void setAngle(Point angle) {
        this.angle = angle;
    }
}
