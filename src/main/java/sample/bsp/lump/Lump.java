package sample.bsp.lump;

import sample.bsp.LumpType;

/**
 * Created by Flazher on 24.11.2016.
 */
public class Lump {

    private LumpType type;
    private Integer bOffset;
    private Integer bLength;

    public Lump(LumpType type, Integer bOffset, Integer bLength) {
        this.type = type;
        this.bOffset = bOffset;
        this.bLength = bLength;
    }

    public LumpType getType() {
        return type;
    }

    public Integer getbOffset() {
        return bOffset;
    }

    public Integer getLength() {
        return bLength;
    }
}
