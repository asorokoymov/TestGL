package sample.bsp;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.bsp.lump.BSPNode;
import sample.bsp.lump.BSPPlane;
import sample.bsp.lump.BSPVector;
import sample.bsp.lump.Lump;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flazher on 23.11.2016.
 */
public class BspFile {

    private static final Logger log = LoggerFactory.getLogger("BSPFile");

    private static final int HL_VERSION = 30;

    public static final int LUMPS_COUNT = 15;

    private Integer version;
    private List<Lump> lumps;
    private List<BSPPlane> planes = new ArrayList<>();
    private List<BSPNode> bspNodes = new ArrayList<>();
    private byte[] bspBytes;
    private ByteBuffer byteBuffer;

    public BspFile() {
    }

    public static final BspFile open(String path) throws IOException {
        BspFile file = new BspFile();
        File bspFile = new File(path);
        FileInputStream inputStream = FileUtils.openInputStream(bspFile);

        file.bspBytes = new byte[(int)bspFile.length()];
        inputStream.read(file.bspBytes);
        file.byteBuffer = ByteBuffer.wrap(file.bspBytes);
        file.byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        file.lumps = new ArrayList<>();
        return file;
    }

    public void read() throws IOException {
        // First, we're reading a header
        version = byteBuffer.getInt();
        if (version != HL_VERSION) {
            throw new IOException("Not a HL1 .bsp file");
        }

        log.info("Parsing lumps");
        loadLumps();

        processLumps();
    }

    private void loadLumps() {
        for (int i = 0; i < LUMPS_COUNT; i++) {
            int lumpOffset, lumpLength;

            lumpOffset = byteBuffer.getInt();
            lumpLength = byteBuffer.getInt();

            LumpType lumpType = LumpType.get(i);

            log.info("Lump {}: offset={}, size={}", lumpType.toString(), lumpOffset, lumpLength);

            lumps.add(new Lump(lumpType, lumpOffset, lumpLength));
        }
    }

    private void processLumps() {
        loadEntities();
        loadBspNodes();
        loadPlanes();
    }

    private void loadEntities() {
        Lump entitiesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_ENTITIES))
            .findFirst().get();

        byteBuffer.position(entitiesLump.getbOffset());
        byte[] entitiesBytes = new byte[entitiesLump.getLength()];
        byteBuffer.get(entitiesBytes, 0, entitiesLump.getLength());
        String entitiesString = new String(entitiesBytes, Charset.forName("ASCII"));
        log.info("Entities loaded");
    }

    private void loadBspNodes() {
        Lump nodesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_NODES))
            .findFirst().get();

        byteBuffer.position(nodesLump.getbOffset());
        int nodesCount = nodesLump.getLength() / 24;
        for (int i = 0; i < nodesCount; i++) {
            Integer iPlane = byteBuffer.getInt();
            Short lChild = byteBuffer.getShort();
            Short rChild = byteBuffer.getShort();

            for (int j = 0; j < 6; j++)
                byteBuffer.getShort();

            Short iFaces = byteBuffer.getShort();
            Short facesCount = byteBuffer.getShort();

            BSPNode node = new BSPNode(iPlane, lChild, rChild, iFaces, facesCount);
            bspNodes.add(node);
        }

        bspNodes.forEach(n -> log.info(n.toString()));
        log.info("Loaded {} nodes", nodesCount);
    }

    private void loadPlanes() {
        Lump planesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_PLANES))
            .findFirst()
            .get();

        Integer planesCount = planesLump.getLength() / 20;
        byteBuffer.position(planesLump.getbOffset());
        for (int i = 0; i < planesCount; i++) {
            BSPVector vector = new BSPVector(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
            Float dist = byteBuffer.getFloat();
            Integer type = byteBuffer.getInt();
            planes.add(new BSPPlane(vector, dist, type));
        }
        log.info("Loaded {} planes", planesCount);
    }
}