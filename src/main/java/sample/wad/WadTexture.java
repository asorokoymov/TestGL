package sample.wad;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Flazher on 09.01.2017.
 */
public class WadTexture {

    public String name;
    public Integer width;
    public Integer height;
    public Integer[] offsets;
    public ByteBuffer image;

    public WadTexture(String name, Integer width, Integer height, Integer[] offsets) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.offsets = offsets;
    }

    @Override
    public String toString() {
        return "WadTexture{" +
            "name='" + name + '\'' +
            ", width=" + width +
            ", height=" + height +
            ", offsets=" + Arrays.toString(offsets) +
            '}';
    }
}
