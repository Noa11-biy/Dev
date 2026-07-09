package fr.javacraft.world;

/**
 * Donnees brutes d'un chunk : tableau compact de blocs.
 * Format : stockage lineaire de 16x256x16 = 65 536 blocs.
 * Index = x + z*16 + y*256 (accumulateur Y majeur).
 *
 * Le bloc a l'index est stocke dans un byte (BlockType.id <= 8).
 */
public class ChunkData {

    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 256;
    public static final int SIZE_Z = 16;
    public static final int VOLUME = SIZE_X * SIZE_Y * SIZE_Z;

    public final byte[] blocks;
    public final int cx;
    public final int cz;

    public ChunkData(int cx, int cz) {
        this.cx = cx;
        this.cz = cz;
        this.blocks = new byte[VOLUME];
        java.util.Arrays.fill(blocks, (byte)0);
    }

    public BlockType getBlock(int lx, int ly, int lz) {
        if (lx < 0 || lx >= SIZE_X || ly < 0 || ly >= SIZE_Y || lz < 0 || lz >= SIZE_Z) {
            return BlockType.AIR;
        }
        return BlockType.byId(blocks[index(lx, ly, lz)]);
    }

    public void setBlock(int lx, int ly, int lz, BlockType type) {
        if (lx < 0 || lx >= SIZE_X || ly < 0 || ly >= SIZE_Y || lz < 0 || lz >= SIZE_Z) return;
        blocks[index(lx, ly, lz)] = (byte)type.id;
    }

    private static int index(int lx, int ly, int lz) {
        return lx + lz * SIZE_X + ly * (SIZE_X * SIZE_Z);
    }

    public float worldX(int lx) {
        return cx * SIZE_X + lx + 0.5f;
    }

    public float worldY(int ly) {
        return ly + 0.5f;
    }

    public float worldZ(int lz) {
        return cz * SIZE_Z + lz + 0.5f;
    }

    /**
     * Retourne le blockId aux coordonnees monde absolues.
     * Utilise par Physics pour les collisions AABB.
     * @param wx coordonnee X monde
     * @param wy coordonnee Y monde
     * @param wz coordonnee Z monde
     * @return blockId (0=AIR) ou 0 si hors chunk
     */
    public int getBlockAt(int wx, int wy, int wz) {
        int lx = wx - cx * SIZE_X;
        int lz = wz - cz * SIZE_Z;
        if (lx < 0 || lx >= SIZE_X || wy < 0 || wy >= SIZE_Y || lz < 0 || lz >= SIZE_Z) {
            return 0;
        }
        return blocks[index(lx, wy, lz)] & 0xFF;
    }
}
