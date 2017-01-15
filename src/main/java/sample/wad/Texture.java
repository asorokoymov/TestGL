package sample.wad;

import java.nio.ByteBuffer;

/**
 * Created by Flazher on 15.01.2017.
 */
public class Texture {

    public Integer width;
    public Integer height;
    public ByteBuffer image;

    public Texture(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }
}
