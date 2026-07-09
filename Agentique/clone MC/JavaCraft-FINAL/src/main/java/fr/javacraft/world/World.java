package fr.javacraft.world;

import org.joml.Vector3f;

/**
 * Le monde voxel de JavaCraft.
 * Gere le ChunkManager et l'ensemble des entites/joueurs.
 * Point d'entree pour le tick et le rendu.
 */
public class World {

    /** Le ChunkManager (singleton) */
    public final ChunkManager chunkManager = ChunkManager.getInstance();

    /** Graine du monde (partagee avec ChunkManager) */
    public final long worldSeed;

    /** Position de spawn (mise a jour par la premiere generation) */
    private float spawnX = 8.0f;
    private float spawnY = 80.0f;
    private float spawnZ = 8.0f;

    /**
     * Construit le monde et lance la generation initiale.
     */
    public World() {
        this.worldSeed = System.currentTimeMillis();
        System.out.println("[World] Monde cree. Graine = " + worldSeed);
    }

    /**
     * Met a jour le monde (appelee chaque frame).
     * Met a jour le ChunkManager autour du joueur.
     * 
     * @param playerX  position X monde du joueur
     * @param playerY  position Y monde du joueur
     * @param playerZ  position Z monde du joueur
     */
    public void update(float playerX, float playerZ) {
        chunkManager.update(playerX, playerZ);
    }

    /** Methode de tick (appele chaque frame, meme si pas de mouvement). */
    public void tick() {
        // ChunkManager.tick() serait ici (deja dans update)
    }

    /**
     * Recupere le type de bloc a une position monde absolue.
     * 
     * @param wx  coordonnee X monde
     * @param wy  coordonnee Y monde
     * @param wz  coordonnee Z monde
     * @return le BlockType (ou AIR si pas genere)
     */
    public BlockType getBlockAt(int wx, int wy, int wz) {
        int cx = ChunkManager.worldToChunk((float)wx);
        int cz = ChunkManager.worldToChunk((float)wz);
        ChunkData chunk = chunkManager.getChunk(cx, cz);
        if (chunk == null) return BlockType.AIR;
        int lx = ((wx % 16) + 16) % 16;
        int lz = ((wz % 16) + 16) % 16;
        return chunk.getBlock(lx, wy, lz);
    }

    /** Arrete le monde (eteint le pool de threads). */
    public void shutdown() {
        chunkManager.shutdown();
        System.out.println("[World] Monde ferme.");
    }
}
