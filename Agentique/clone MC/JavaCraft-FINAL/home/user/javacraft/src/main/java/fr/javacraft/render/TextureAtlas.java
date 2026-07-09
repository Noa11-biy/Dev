package fr.javacraft.render;

import org.lwjgl.opengl.GL33;
import static org.lwjgl.opengl.GL33.*;

/**
 * Atlas de textures procedurale 128x128.
 * Contient 8x8 = 64 tuiles de 16x16 pixels chacune.
 * Chaque BlockType reference une tuile par son tileIndex.
 * 
 * Genere a runtime pour eviter tout asset externe.
 */
public class TextureAtlas {

    /** Taille totale de l'atlas en pixels (doit etre une puissance de 2) */
    private static final int ATLAS_SIZE = 128;

    /** Nombre de tuiles par ligne/colonne */
    private static final int TILES_PER_ROW = 8;

    /** Taille d'une tuile en pixels */
    public static final int TILE_SIZE = ATLAS_SIZE / TILES_PER_ROW; // 16

    /** ID OpenGL de la texture */
    private final int textureId;

    /**
     * Genere l'atlas procedurale et cree la texture OpenGL.
     */
    public TextureAtlas() {
        // Genere l'image RGBA en memoire
        java.nio.ByteBuffer pixels = java.nio.ByteBuffer.allocateDirect(ATLAS_SIZE * ATLAS_SIZE * 4);

        for (int tile = 0; tile < 9; tile++) {
            int tx = (tile % TILES_PER_ROW) * TILE_SIZE;
            int ty = (tile / TILES_PER_ROW) * TILE_SIZE;
            generateTile(pixels, tx, ty, tile);
        }

        pixels.flip();

        // Cree la texture OpenGL
        int[] tex = new int[1];
        glGenTextures(tex);
        this.textureId = tex[0];
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, ATLAS_SIZE, ATLAS_SIZE, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        System.out.println("[TextureAtlas] genere (ID=" + textureId + "). " + TILES_PER_ROW + " tuiles.");
    }

    /**
     * Genere la tuile tileIndex a la position (tx, ty) dans le buffer pixels.
     * 
     * 0 = DIRT       (brun terreux avec bruit)
     * 1 = STONE      (gris fonce avec variation)
     * 2 = GRASS      (vert dessus, brun dessous)
     * 3 = WATER      (bleu semi-transparent)
     * 4 = SAND       (beige clair)
     * 5 = WOOD       (brun bois avec anneaux)
     * 6 = LEAVES     (vert fonce transparent)
     * 7 = BEDROCK    (gris tres fonce)
     */
    private void generateTile(java.nio.ByteBuffer pixels, int tx, int ty, int tile) {
        for (int py = 0; py < TILE_SIZE; py++) {
            for (int px = 0; px < TILE_SIZE; px++) {
                int r = 120, g = 120, b = 120, a = 255;
                long seed = (long)(tx + px) * 31337L + (long)(ty + py) * 7919L;
                double noise = Math.abs(Math.sin(seed * 0.618034) * 43758.5453) % 1.0;
                int v = (int)(noise * 45);

                switch (tile) {
                    case 0: // DIRT
                        r = 100 + v; g = 70 + v/2; b = 40 + v/3;
                        break;
                    case 1: // STONE
                        r = 90 + v; g = 88 + v; b = 85 + v;
                        break;
                    case 2: // GRASS (dessus = vert, dessous = brun)
                        if (py < 4) {
                            r = 50 + v; g = 120 + v/2; b = 30 + v/3;
                        } else {
                            r = 95 + v; g = 70 + v/2; b = 40 + v/3;
                        }
                        break;
                    case 3: // WATER (semi-transparent)
                        r = 30 + v/2; g = 100 + v/2; b = 180 + v/2; a = 180;
                        break;
                    case 4: // SAND
                        r = 200 + v; g = 180 + v; b = 120 + v;
                        break;
                    case 5: // WOOD (anneaux de bois)
                        boolean ring = (px % 4 < 1) || (py % 4 < 1);
                        r = ring ? 100 + v : 130 + v;
                        g = ring ? 65 + v/2 : 85 + v/2;
                        b = ring ? 35 + v/3 : 50 + v/3;
                        break;
                    case 6: // LEAVES
                        r = 30 + v; g = 90 + v/2; b = 20 + v/3; a = 200;
                        break;
                    case 7: // BEDROCK
                        r = 40 + v/2; g = 38 + v/2; b = 35 + v/2;
                        break;
                    default:
                        r = g = b = 128;
                }

                // Mortier (bordure entre tuiles) - assombrit les derniers pixels
                boolean mortar = (px == TILE_SIZE - 1 || py == TILE_SIZE - 1);
                if (mortar) { r = r * 6 / 10; g = g * 6 / 10; b = b * 6 / 10; }

                pixels.put((byte)r);
                pixels.put((byte)g);
                pixels.put((byte)b);
                pixels.put((byte)a);
            }
        }
    }

    /** Bind la texture de l'atlas sur l'unite GL_TEXTURE0. */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    /** Supprime la texture OpenGL. */
    public void delete() {
        int[] t = {textureId};
        glDeleteTextures(t);
    }
}
