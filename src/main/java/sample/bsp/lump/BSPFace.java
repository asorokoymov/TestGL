package sample.bsp.lump;

/**
 * Created by Flazher on 06.01.2017.
 */
public class BSPFace {

    public Short plane;
    public Boolean planeSide;
    public Integer firstEdge;
    public Short surfedgesCount;
    public Short textureInfo;
    public byte[] styles;
    public Integer lightmapOffset;

    public BSPFace(Short plane, Short planeSide, Integer firstEdge, Short surfedgesCount, Short textureInfo, byte[] styles, Integer lightmapOffset) {
        this.plane = plane;
        this.planeSide = planeSide.equals(0);
        this.firstEdge = firstEdge;
        this.surfedgesCount = surfedgesCount;
        this.textureInfo = textureInfo;
        this.lightmapOffset = lightmapOffset;
        this.styles = styles;
    }

    @Override
    public String toString() {
        return "BSPFace{" +
            "plane=" + plane +
            ", planeSide=" + planeSide +
            ", firstEdge=" + firstEdge +
            ", surfedgesCount=" + surfedgesCount +
            ", textureInfo=" + textureInfo +
            ", lightmapOffset=" + lightmapOffset +
            '}';
    }
}
