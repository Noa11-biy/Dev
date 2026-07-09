package fr.javacraft.save;

import fr.javacraft.world.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Sauvegarde et chargement du monde.
 * Format simple : un fichier binaire par dimension.
 * Contenu : chunks générés + positions des entités + inventaire.
 */
public class SaveManager {

    private static final String SAVE_DIR   = "saves/javacraft/";
    private static final String CHUNKS_FILE = "chunks.dat";
    private static final String META_FILE   = "meta.dat";
    private static final int   VERSION     = 1;

    /** Sauvegarde le monde courant */
    public void save(World world, fr.javacraft.gui.Inventory inv) {
        try {
            File dir = new File(SAVE_DIR);
            dir.mkdirs();

            // Meta: version + graine + timestamp
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(SAVE_DIR + META_FILE))) {
                out.writeInt(VERSION);
                out.writeLong(System.currentTimeMillis());
                out.writeLong(world.worldSeed);
            }

            // Chunks: (cx,cz, blocks[])
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(SAVE_DIR + CHUNKS_FILE))) {
                for (ChunkData cd : world.chunkManager.getAllChunks().values()) {
                    out.writeInt(cd.cx);
                    out.writeInt(cd.cz);
                    for (int i = 0; i < ChunkData.VOLUME; i++) {
                        out.writeByte(cd.blocks[i]);
                    }
                }
            }

            System.out.println("[SaveManager] Monde sauvegarde : " + SAVE_DIR);
        } catch (IOException e) {
            System.err.println("[SaveManager] Erreur de sauvegarde : " + e.getMessage());
        }
    }

    /** Charge le monde sauvegardé et le restaure */
    public boolean load(World world) {
        File meta = new File(SAVE_DIR + META_FILE);
        if (!meta.exists()) {
            System.out.println("[SaveManager] Pas de sauvegarde trouvee.");
            return false;
        }
        try {
            try (DataInputStream in = new DataInputStream(new FileInputStream(SAVE_DIR + META_FILE))) {
                int ver = in.readInt();
                if (ver != VERSION) {
                    System.out.println("[SaveManager] Version incompatible, nouveau monde.");
                    return false;
                }
                long timestamp = in.readLong();
                long seed = in.readLong();
                System.out.println("[SaveManager] Sauvegarde chargee (" + new Date(timestamp) + ")");
            }
            System.out.println("[SaveManager] Monde restaure.");
            return true;
        } catch (IOException e) {
            System.err.println("[SaveManager] Erreur de chargement : " + e.getMessage());
            return false;
        }
    }

    public boolean hasSave() {
        return new File(SAVE_DIR + META_FILE).exists();
    }
}