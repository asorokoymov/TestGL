package sample.bsp;

public enum LumpType {

    LUMP_UNKNOWN(-1),
    LUMP_ENTITIES(0),
    LUMP_PLANES(1),
    LUMP_TEXDATA(2),
    LUMP_VERTEXES(3),
    LUMP_VISIBILITY(4),
    LUMP_NODES(5),
    LUMP_TEXINFO(6),
    LUMP_FACES(7),
    LUMP_LIGHTING(8),
    LUMP_OCCLUSION(9),
    LUMP_LEAFS(10),
    LUMP_UNDEFINED(11),
    LUMP_EDGES(12),
    LUMP_SURFEDGES(13),
    LUMP_MODELS(14),
    LUMP_HEADERLUMPS(15);

    private final int index;

    LumpType(int index) {
        this.index = index;
    }

    public static LumpType get(String name) {
        return get(name);
    }

    public static LumpType get(int index) {
        for (LumpType type : values()) {
            if (type.index == index) {
                return type;
            }
        }
        return LUMP_UNKNOWN;
    }

    public int getIndex() {
        return index;
    }

}