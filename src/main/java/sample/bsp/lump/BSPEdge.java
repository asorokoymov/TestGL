package sample.bsp.lump;

/**
 * Created by Flazher on 06.01.2017.
 */
public class BSPEdge {

    public short fEdge;
    public short sEdge;

    public BSPEdge(short fEdge, short sEdge) {
        this.fEdge = fEdge;
        this.sEdge = sEdge;
    }

    @Override
    public String toString() {
        return "BSPEdge{" +
            "fEdge=" + fEdge +
            ", sEdge=" + sEdge +
            '}';
    }
}
