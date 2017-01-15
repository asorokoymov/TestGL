package sample.gui;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL12;
import sample.bsp.BspFile;
import sample.bsp.lump.BSPEdge;
import sample.bsp.lump.BSPFace;
import sample.input.Input;
import sample.input.Mouse;
import sample.primitives.Vector3f;
import sample.util.GLUtil;
import sample.wad.Lightmap;
import sample.wad.TextureCoordinates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
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
    private static final float DEFAULT_FOV = 90f;
    private static final float DEFAULT_SENSITIVITY = 0.2f;
    private static final float DEFAULT_MOVEMENT_SPEED = 0.5f;
    private static final float DEFAULT_FAST_MOVEMENT_SPEED = 2.0f;

    private long window;
    private boolean running = true;

    private Camera camera;

    // TODO: change view angle
    private Vector3f viewVector = new Vector3f(0f ,0f, 0f);

    private Mouse mouse;

    private IntBuffer width = BufferUtils.createIntBuffer(1);
    private IntBuffer height = BufferUtils.createIntBuffer(1);

    private BspFile bsp;
    private Map<Integer, Vector3f> faceColors = new HashMap<>();
    private Map<Integer, Integer> glTexIdByWadId = new HashMap<>();
    private Map<Integer, Integer> faceGlText = new HashMap<>();
    private Map<Integer, Map<Integer, TextureCoordinates>> faceTexCoords;

    private Float movementSpeed = DEFAULT_MOVEMENT_SPEED;

    public GUIRunner(BspFile file) {
        this.bsp = file;
    }

    @Override
    public void run() {
        init(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        while (running) {
            syncFrameRate(DEFAULT_FPS);
            if (glfwWindowShouldClose(window))
                running = false;

            glfwGetWindowSize(window, width, height);
            render();
            update();
/*            width.flip();
            height.flip();*/
        }
    }

    private void init(int width, int height) {
        initDisplay(width, height);
        initGL(width, height);

        camera = new Camera();
        mouse = Mouse.init(window);
    }

    private void initDisplay(int width, int height) {
        if (glfwInit() == false) {
        }
        window = glfwCreateWindow(width, height, "BSP Viewer", NULL, NULL);
        glfwMakeContextCurrent(window);
        glfwSetKeyCallback(window, new Input());
        glfwShowWindow(window);
    }

    private void initGL(int width, int height) {
        GL.createCapabilities();
        glClearColor(0.2f, 0.2f, 0.2f, 1);
        //glEnable(GL_TEXTURE_2D);
        prepareLightmaps();

        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        GLUtil.perspectiveGL(DEFAULT_FOV, ((float)width/height), 1f, 200f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    private void prepareLightmaps() {
        Lightmap lightmap;
        faceTexCoords = bsp.getFaceTexCoords();
        for (Map.Entry<Integer, Lightmap> lightmapEntry : bsp.getLightmaps().entrySet()) {
            lightmap = lightmapEntry.getValue();
            int textureId = glGenTextures();

            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, textureId);

            // Set up Texture Filtering Parameters
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                GL_LINEAR);

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glPixelStorei(GL_PACK_ALIGNMENT, 1);
            glTexEnvf (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

            lightmap.image.position(0);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, lightmap.width, lightmap.height, 0, GL_RGB, GL_UNSIGNED_BYTE, lightmap.image);
            faceGlText.put(lightmapEntry.getKey(), textureId);
        }
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glPushMatrix();
        if ((mouse.getDelta().x != 0 || mouse.getDelta().y != 0) && mouse.isLmbPressed()) {
            camera.getAngle().y += mouse.getDelta().x * DEFAULT_SENSITIVITY;
            camera.getAngle().x += mouse.getDelta().y * DEFAULT_SENSITIVITY;
            viewVector = new Vector3f(
                (float)Math.cos(Math.toRadians(camera.getAngle().x)) * (float)Math.cos(Math.toRadians(camera.getAngle().y)),
                (float)Math.sin(Math.toRadians(camera.getAngle().x)) * (float)Math.cos(Math.toRadians(camera.getAngle().y)),
                -(float)Math.sin(Math.toRadians(camera.getAngle().y))
            );
        }

        glRotatef(camera.getAngle().x, 1, 0, 0);
        glRotatef(camera.getAngle().y, 0, 1, 0);

        glTranslatef(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
        glPushMatrix();

        glScalef(0.1f, 0.1f, 0.1f);
        glRotatef(-90, 1f, 0f, 0f);

        Random random = new Random();
        Integer edgeIndex;
        BSPFace face;
        BSPEdge edge;
        boolean isFirstEdge;
        Vector3f firstVerticle, secondVerticle;
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        //glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);

        ////////////////////////////////////////////////////////////


        // TUT

        glBindTexture(GL_TEXTURE_2D, 2);
        glBegin(GL_QUADS);
        glNormal3f(0.0f, 0.0f, 1.0f);
        glTexCoord2d(1, 1); glVertex3f(0.0f, 0.0f, 0.0f);
        glTexCoord2d(1, 0); glVertex3f(0.0f, 100.0f, 0.0f);
        glTexCoord2d(0, 0); glVertex3f(100.0f, 100.0f, 0.0f);
        glTexCoord2d(0, 1); glVertex3f(100.0f, 0.0f, 0.0f);
        glEnd();

        ////////////////////////////////////////////////////////////////

        for (int f = 0; f < bsp.getFaces().size(); f++) {
            face = bsp.getFaces().get(f);
            //Vector3f color = faceColors.computeIfAbsent(f, integer -> new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            //glColor3f(color.x, color.y, color.z);

            if (faceGlText.get(f) != null) {
                glBindTexture(GL_TEXTURE_2D, faceGlText.get(f));
                glBegin(GL_TRIANGLE_FAN);
                int firstSurfedgeIndex = face.firstEdge;
                for (int i = 0; i < face.surfedgesCount; i++) {
                    TextureCoordinates tCoordinates = faceTexCoords.get(f).get(i);
                    edgeIndex = bsp.getSurfedges().get(firstSurfedgeIndex + i);
                    if (edgeIndex >= 0) {
                        isFirstEdge = true;
                    } else {
                        isFirstEdge = false;
                        edgeIndex *= -1;
                    }
                    edge = bsp.getEdges().get(edgeIndex);

                    firstVerticle = bsp.getVerticies().get(edge.fEdge);
                    secondVerticle = bsp.getVerticies().get(edge.sEdge);

                    glTexCoord2d(tCoordinates.fS, tCoordinates.fT);
                    if (isFirstEdge) {
                        glVertex3f(firstVerticle.x, firstVerticle.y, firstVerticle.z);
                    } else
                        glVertex3f(secondVerticle.x, secondVerticle.y, secondVerticle.z);
                }

                glEnd();
            } else {

            }
        }

        glPopMatrix();
        glPopMatrix();
        glfwSwapBuffers(window);
    }

    private void update() {
        glfwPollEvents();

        if (Input.keys[GLFW_KEY_A]) {
            camera.getPosition().x += viewVector.x * movementSpeed;
            camera.getPosition().z -= viewVector.z * movementSpeed;
        }

        if (Input.keys[GLFW_KEY_D]) {
            camera.getPosition().x -= viewVector.x * movementSpeed;
            camera.getPosition().z += viewVector.z * movementSpeed;
        }

        if (Input.keys[GLFW_KEY_W]) {
            camera.getPosition().x += viewVector.z * movementSpeed;
            camera.getPosition().y += viewVector.y * movementSpeed;
            camera.getPosition().z += viewVector.x * movementSpeed;
        }

        if (Input.keys[GLFW_KEY_S]) {
            camera.getPosition().x -= viewVector.z * movementSpeed;
            camera.getPosition().y -= viewVector.y * movementSpeed;
            camera.getPosition().z -= viewVector.x * movementSpeed;
        }
        if (Input.keys[GLFW_KEY_LEFT_SHIFT]) {
            movementSpeed = DEFAULT_FAST_MOVEMENT_SPEED;
        } else {
            movementSpeed = DEFAULT_MOVEMENT_SPEED;
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
