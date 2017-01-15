package sample.util;

import sample.wad.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Flazher on 29.11.2016.
 */
public class GLUtil {

    public static void perspectiveGL(float fov, float aspect, float zNear, float zFar) {
        float fH = (float) Math.tan(fov / 360 * Math.PI) * zNear;
        float fW = fH * aspect;
        glFrustum( -fW, fW, -fH, fH, zNear, zFar );
    }

    public static void adjustToPowerOfTwo(Texture texture) {
        if (MathUtil.isPowerOfTwo(texture.width) && MathUtil.isPowerOfTwo(texture.height))
            return;

        glPixelStorei(GL_PACK_ALIGNMENT, 1);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        int nPOT = 1;
        while (nPOT < texture.height || nPOT < texture.width)
            nPOT *= 2;
       // GLU.gluScaleImage(GL_RGB, texture.width, texture.height, GL_UNSIGNED_BYTE, texture.image, nPOT, nPOT, GL_UNSIGNED_BYTE, texture.image);
        //gluScaleImage(pImg->nChannels == 4 ? GL_RGBA : GL_RGB, pImg->nWidth, pImg->nHeight, GL_UNSIGNED_BYTE, pImg->pData, nPOT, nPOT, GL_UNSIGNED_BYTE, pNewData);
        texture.width = nPOT;
        texture.height = nPOT;
    }

}
