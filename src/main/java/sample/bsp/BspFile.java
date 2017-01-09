package sample.bsp;

import org.apache.commons.io.FileUtils;
import org.lwjgl.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.bsp.lump.*;
import sample.primitives.Vector3f;
import sample.wad.WadTexture;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Flazher on 23.11.2016.
 */
public class BspFile {

    private static final Logger log = LoggerFactory.getLogger("BSPFile");

    private static final int HL_VERSION = 30;

    public static final int LUMPS_COUNT = 15;
    public static final int MAX_WAD_TEXTURE_NAME_LENGTH = 16;

    private Integer version;
    private List<Lump> lumps;
    private Map<Short, BSPPlane> planes = new HashMap<>();
    private List<BSPNode> bspNodes = new ArrayList<>();
    private Map<Short, Vector3f> verticies = new HashMap<>();
    private List<BSPEdge> edges = new ArrayList<>();
    private List<Integer> surfedges = new ArrayList<>();
    private List<BSPFace> faces = new ArrayList<>();
    private Map<Short, BspTextureInfo> texturesInfo = new HashMap<>();
    private Map<Short, WadTexture> textures = new HashMap<>();
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
        loadVerticies();
        loadEdges();
        loadSurfedges();
        loadFaces();
        loadTextures();
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

        log.info("Loaded {} nodes", nodesCount);
    }

    private void loadPlanes() {
        Lump planesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_PLANES))
            .findFirst()
            .get();

        Integer planesCount = planesLump.getLength() / 20;
        byteBuffer.position(planesLump.getbOffset());
        for (short i = 0; i < planesCount; i++) {
            BSPVector vector = new BSPVector(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
            Float dist = byteBuffer.getFloat();
            Integer type = byteBuffer.getInt();
            planes.put(i, new BSPPlane(vector, dist, type));
        }
        log.info("Loaded {} planes", planesCount);
    }

    private void loadVerticies() {
        Lump verticiesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_VERTEXES))
            .findFirst()
            .get();
        Integer verticiesCount = verticiesLump.getLength() / 12;
        byteBuffer.position(verticiesLump.getbOffset());

        for (short i = 0; i < verticiesCount; i++) {
            Vector3f verticle = new Vector3f(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
            verticies.put(i, verticle);
        }
        log.info("Loaded {} verticles", verticiesCount);
    }

    private void loadEdges() {
        Lump edgesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_EDGES))
            .findFirst()
            .get();
        Integer edgesCount = edgesLump.getLength() / 4;
        byteBuffer.position(edgesLump.getbOffset());

        for (int i = 0; i < edgesCount; i++) {
            BSPEdge edge = new BSPEdge(byteBuffer.getShort(), byteBuffer.getShort());
            edges.add(edge);
        }
        log.info("Loaded {} edges", edgesCount);
    }

    private void loadSurfedges() {
        Lump surfedgesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_SURFEDGES))
            .findFirst()
            .get();
        Integer surfedgesCount = surfedgesLump.getLength() / 4;
        byteBuffer.position(surfedgesLump.getbOffset());

        for (int i = 0; i < surfedgesCount; i++) {
            surfedges.add(byteBuffer.getInt());
        }
        log.info("Loaded {} surfedges", surfedgesCount);
    }

    private void loadFaces() {
        Lump facesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_FACES))
            .findFirst()
            .get();

        Integer facesCount = facesLump.getLength() / 20;
        byteBuffer.position(facesLump.getbOffset());

        for (int i = 0; i < facesCount; i++) {
            BSPFace face = new BSPFace(
                byteBuffer.getShort(), byteBuffer.getShort(), byteBuffer.getInt(),
                byteBuffer.getShort(), byteBuffer.getShort()
            );
            byteBuffer.getInt();
            byteBuffer.getInt();
            faces.add(face);
        }
        log.info("Loaded {} faces", facesCount);
    }

    private void loadTextures() {
        Lump texturesInfoLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_TEXINFO))
            .findFirst()
            .get();

        Integer texturesInfoCount = texturesInfoLump.getLength() / 40;
        byteBuffer.position(texturesInfoLump.getbOffset());
        for (short i = 0; i < texturesInfoCount; i++) {
            BspTextureInfo info = new BspTextureInfo(
                new Vector3f(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat()),
                byteBuffer.getFloat(),
                new Vector3f(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat()),
                byteBuffer.getFloat(),
                byteBuffer.getInt(),
                byteBuffer.getInt()
            );
            texturesInfo.put(i, info);
        }

        Lump texturesLump = lumps.stream()
            .filter(l -> l.getType().equals(LumpType.LUMP_TEXTURES))
            .findFirst()
            .get();
        Integer texturesLumpOffset = texturesLump.getbOffset();
        byteBuffer.position(texturesLumpOffset);
        Integer texturesCount = byteBuffer.getInt();
        List<Integer> texturesOffsets = new ArrayList<>(texturesCount);
        for (int i = 0; i < texturesCount; i++) {
            texturesOffsets.add(byteBuffer.getInt());
        }

        short t = 0;
        for (Integer offset : texturesOffsets) {
            byteBuffer.position(texturesLumpOffset + offset);

            byte[] textureNameBytes = new byte[MAX_WAD_TEXTURE_NAME_LENGTH];
            byteBuffer.get(textureNameBytes, 0, MAX_WAD_TEXTURE_NAME_LENGTH);
            String textureName = new String(textureNameBytes);

            WadTexture texture = new WadTexture(textureName, byteBuffer.getInt(), byteBuffer.getInt(),
                new Integer[]{byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt(), byteBuffer.getInt()});

            Integer textureLength = texture.width * texture.height;
            byte[] image = new byte[textureLength];

            // Пока я маленький и глупый, буду брать самую жирную пикчу из мипмапа
            byteBuffer.position(texturesLumpOffset + offset + texture.offsets[0]);
            byteBuffer.get(image, 0, textureLength);
            ByteBuffer buffer = BufferUtils.createByteBuffer(texture.width * texture.height);
            buffer.put(image);
            texture.image = buffer;
            textures.put(t, texture);
            t++;
        }
        log.info("{} textures info entities and {} BSPMIPTEX structures found", texturesInfoCount, texturesCount);
    }

    public Map<Short, Vector3f> getVerticies() {
        return verticies;
    }

    public List<BSPEdge> getEdges() {
        return edges;
    }

    public List<BSPFace> getFaces() {
        return faces;
    }

    public List<Integer> getSurfedges() {
        return surfedges;
    }

    public Map<Short, BspTextureInfo> getTexturesInfo() {
        return texturesInfo;
    }

    public Map<Short, WadTexture> getTextures() {
        return textures;
    }
}
