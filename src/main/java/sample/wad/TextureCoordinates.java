package sample.wad;

/**
 * Created by Flazher on 14.01.2017.
 */
public class TextureCoordinates {

    public Float fS;
    public Float fT;

    public TextureCoordinates(Float fS, Float fT) {
        this.fS = fS;
        this.fT = fT;
    }

    @Override
    public String toString() {
        return "TextureCoordinates{" +
            "fS=" + fS +
            ", fT=" + fT +
            '}';
    }
}
