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

    public BSPFace(Short plane, Short planeSide, Integer firstEdge, Short surfedgesCount, Short textureInfo) {
        this.plane = plane;
        this.planeSide = planeSide.equals(0);
        this.firstEdge = firstEdge;
        this.surfedgesCount = surfedgesCount;
        this.textureInfo = textureInfo;
    }

    @Override
    public String toString() {
        return "BSPFace{" +
            "plane=" + plane +
            ", planeSide=" + planeSide +
            ", firstEdge=" + firstEdge +
            ", surfedgesCount=" + surfedgesCount +
            ", textureInfo=" + textureInfo +
            '}';
    }
}
