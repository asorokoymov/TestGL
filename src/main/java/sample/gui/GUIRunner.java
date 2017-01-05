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

    private Point[] points = new Point[3000];

    private Float xPos = 0f;
    private Float yPos = 0f;
    private Float zPos = 0f;

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

    private void generatePoints(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < points.length; i++) {
            Float deltaX = maxX - minX;
            Float xCenter = (minX + maxX) / 2;
            Float deltaZ = maxZ - minZ;

            Float x = minX + deltaX * random.nextFloat();
            double cos = Math.abs((xCenter - x) / (deltaX / 2));
            double acos = Math.acos(cos);
            double zRange = Math.sin(acos) * deltaZ / 2;
            Float z = minZ + (float)(deltaZ/2 - zRange) + (float)zRange * 2 * random.nextFloat();
            Point point = new Point(
                x,
                minY + (maxY - minY) * random.nextFloat(),
                z
            );
            points[i] = point;
        }
    }

    private void init(int width, int height) {
        initDisplay(width, height);
        initGL(width, height);

        generatePoints(-4, 4, -1, 1, -16, -8);
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

        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        GLUtil.perspectiveGL(90f, ((float)width/height), 1f, 20);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    private void render(int width, int height) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glTranslatef(0, 0, -12);
        glRotatef(1f, 0f, 0.5f, 0);
        glTranslatef(0, 0, 12);

        glPushMatrix();

        glTranslatef(0, -4, 0);

        glBegin(GL_POINTS);
        glColor3f(1.0f, 1.0f, 1.0f);
        for (Point point : points) {
            glVertex3d(point.x, point.y, point.z);
        }

        glEnd();
        glPopMatrix();
        glfwSwapBuffers(window);
    }

    private void update() {
        glfwPollEvents();
        if (Input.keys[GLFW_KEY_A]) {
            xPos -= 0.1f;
        } else if (Input.keys[GLFW_KEY_D]) {
            xPos += 0.1f;
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
