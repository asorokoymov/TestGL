package sample.gui;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import sample.Input;
import sample.Point;
import sample.util.GLUtil;

import java.nio.IntBuffer;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Flazher on 27.11.2016.
 */
public class GUIRunner implements Runnable {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_FPS = 60;

    private long window;
    private boolean running = true;

    private IntBuffer width = BufferUtils.createIntBuffer(1);
    private IntBuffer height = BufferUtils.createIntBuffer(1);

    private Point[] points = new Point[20];

    private Float speed = 0f;

    @Override
    public void run() {
        init(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        while (running) {
            syncFrameRate(DEFAULT_FPS);
            if (glfwWindowShouldClose(window))
                running = false;

            glfwGetWindowSize(window, width, height);
            render(width.get(), height.get());
            update();
            width.flip();
            height.flip();
        }
    }

    private void init(int width, int height) {
        initDisplay(width, height);
        initGL(width, height);

        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < points.length; i++) {
            Point point = new Point(
                random.nextFloat() * 10 - 5, random.nextFloat() * 10 - 5, (float)i*2
            );
            points[i] = point;
        }
    }

    private void initDisplay(int width, int height) {
        if (glfwInit() == false) {
        }
        window = glfwCreateWindow(width, height, "Test", NULL, NULL);
        glfwMakeContextCurrent(window);
        glfwSetKeyCallback(window, new Input());
        glfwShowWindow(window);
    }

    private void initGL(int width, int height) {
        GL.createCapabilities();
        glClearColor(0.2f, 0.2f, 0.2f, 1);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        //glOrtho(0, width, 0, height, 11, -11);
        glViewport(0, 0, width, height);
        GLUtil.perspectiveGL(90f, width/height, 0.1f, 200);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    private void render(int width, int height) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //glTranslatef(0, 0, speed);
        glPushMatrix();
        glBegin(GL_POINTS);

/*        for (Point point : points) {
            //glVertex3d(point.x, point.y, point.z);
            glVertex3d(point.x - 1, point.y - 1, point.z);
            glVertex3d(point.x - 1, point.y + 1, point.z);
            glVertex3d(point.x + 1, point.y + 1, point.z);
            glVertex3d(point.x + 1, point.y - 1, point.z);
        }*/
        glColor3f(1.0f, 1.0f, 1.0f);

        for (int i = 0; i < 800; i+=10) {
            for (int j = 0; j < 600; j+=10) {
                for (int k = 0; k < 10; k+=2) {
                    glVertex3d(i, j, k);
                }
            }
        }

        glEnd();


        glPopMatrix();
        glfwSwapBuffers(window);
    }

    private void update() {
        glfwPollEvents();
        if (Input.keys[GLFW_KEY_SPACE]) {
            speed += 0.001f;
        }
    }

    private void syncFrameRate(int fps) {
        long base = 1_000_000_000 / fps;
        int nanos = (int)(base % 1_000_000);
        long millis = (base - nanos) / 1_000_000;
        try {
            Thread.sleep(millis, nanos);
        }
        catch (InterruptedException ignore) {}
    }
}
