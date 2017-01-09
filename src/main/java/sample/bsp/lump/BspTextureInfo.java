package sample.bsp.lump;

import sample.primitives.Vector3f;

/**
 * Created by Flazher on 10.01.2017.
 */
public class BspTextureInfo {

    public Vector3f vS;
    public Float fSShift;
    public Vector3f vT;
    public Float fTShift;
    public Integer iMiptex;
    public Integer nFlags;

    public BspTextureInfo(Vector3f vS, Float fSShift, Vector3f vT, Float fTShift, Integer iMiptex, Integer nFlags) {
        this.vS = vS;
        this.fSShift = fSShift;
        this.vT = vT;
        this.fTShift = fTShift;
        this.iMiptex = iMiptex;
        this.nFlags = nFlags;
    }

    @Override
    public String toString() {
        return "BspTextureInfo{" +
            "vS=" + vS +
            ", fSShift=" + fSShift +
            ", vT=" + vT +
            ", fTShift=" + fTShift +
            ", iMiptex=" + iMiptex +
            ", nFlags=" + nFlags +
            '}';
    }
}
