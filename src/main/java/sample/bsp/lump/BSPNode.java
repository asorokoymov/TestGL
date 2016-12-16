package sample.bsp.lump;

/**
 * Created by Flazher on 24.11.2016.
 */
public class BSPNode {

    private Integer iPlane;
    private Short iLChildren;
    private Short iRChildren;
    private Short iFaces;
    private Short facesCount;

    public BSPNode(Integer iPlane, Short iLChildren, Short iRChildren, Short iFaces, Short facesCount) {
        this.iPlane = iPlane;
        this.iLChildren = iLChildren;
        this.iRChildren = iRChildren;
        this.iFaces = iFaces;
        this.facesCount = facesCount;
    }

    public Integer getiPlane() {
        return iPlane;
    }

    public Short getiLChildren() {
        return iLChildren;
    }

    public Short getiRChildren() {
        return iRChildren;
    }

    @Override
    public String toString() {
        return "BSPNode{" +
            "iPlane=" + iPlane +
            ", iLChildren=" + iLChildren +
            ", iRChildren=" + iRChildren +
            ", iFaces=" + iFaces +
            ", facesCount=" + facesCount +
            '}';
    }
}
