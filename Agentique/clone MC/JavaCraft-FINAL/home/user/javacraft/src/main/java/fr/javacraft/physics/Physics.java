package fr.javacraft.physics;

import fr.javacraft.world.*;
import org.joml.Vector3f;

/**
 * Physique du joueur : gravité, collisions AABB.
 * Empêche de traverser les blocs solides.
 */
public class Physics {

    /** Hauteur du joueur en blocs */
    public static final float PLAYER_HEIGHT = 1.8f;
    /** Demi-largeur du joueur (pour l'AABB) */
    public static final float PLAYER_RADIUS = 0.3f;
    /** Gravité */
    private static final float GRAVITY       = -28.0f;
    /** Vitesse max de chute */
    private static final float TERMINAL_VEL  = -60.0f;

    private final Vector3f vel = new Vector3f();
    private boolean onGround = false;
    private final ChunkManager chunkManager = ChunkManager.getInstance();


    /**
     * Met à jour la physique du joueur.
     * 
     * @param pos       position actuelle du joueur (sera modifiée)
     * @param velPlayer vitesse actuelle du joueur (sera modifiée)
     * @param dt        delta temps
     * @param blockAt   fonction (wx, wy, wz) → BlockType
     */
    public void tick(float[] pos, float[] velPlayer, float dt) {
        float px = pos[0], py = pos[1], pz = pos[2];
        float vx = velPlayer[0], vy = velPlayer[1], vz = velPlayer[2];

        // Gravité
        vy += GRAVITY * dt;
        if (vy < TERMINAL_VEL) vy = TERMINAL_VEL;

        // Application tentative
        float nx = px + vx * dt;
        float ny = py + vy * dt;
        float nz = pz + vz * dt;

        // Collisions X
        float newX = resolveCollisionX(nx, py, pz);
        velPlayer[0] = 0;

        // Collisions Y
        float[] resY = resolveCollisionY(newX, ny, pz, velPlayer, vy, dt);
        velPlayer[1] = resY[1];
        pos[1] = resY[0];

        // Collisions Z
        float newZ = resolveCollisionZ(newX, pos[1], nz);
        velPlayer[2] = 0;

        pos[0] = newX;
        pos[2] = newZ;
    }

    private float resolveCollisionX(float px, float py, float pz) {
        // Test 3 points dans la colonne
        for (float yo = 0; yo < PLAYER_HEIGHT; yo += 0.6f) {
            if (!isSolid(px + PLAYER_RADIUS, py + yo, pz)
              && !isSolid(px - PLAYER_RADIUS, py + yo, pz)) {
                continue;
            }
            return px - 0.001f; // Collision
        }
        return px;
    }

    private float resolveCollisionZ(float px, float py, float pz) {
        for (float yo = 0; yo < PLAYER_HEIGHT; yo += 0.6f) {
            if (!isSolid(px, py + yo, pz + PLAYER_RADIUS)
              && !isSolid(px, py + yo, pz - PLAYER_RADIUS)) {
                continue;
            }
            return pz - 0.001f;
        }
        return pz;
    }

    private float[] resolveCollisionY(float px, float py, float pz, float[] velPlayer, float vy, float dt) {
        float ny = py;
        boolean wasOnGround = onGround;
        onGround = false;

        if (vy < 0) {
            // Chute : tester le bloc en dessous
            float testY = py - 0.001f;
            if (isSolid(px, testY, pz)
              || isSolid(px, testY, pz - PLAYER_RADIUS)
              || isSolid(px, testY, pz + PLAYER_RADIUS)
              || isSolid(px - PLAYER_RADIUS, testY, pz)
              || isSolid(px + PLAYER_RADIUS, testY, pz)) {
                ny = (float)(Math.floor(py) + 1.0f);
                vy = 0;
                onGround = true;
            }
        } else if (vy > 0) {
            // Montée : tester le bloc au-dessus
            float testY = py + PLAYER_HEIGHT + 0.001f;
            if (isSolid(px, testY, pz)
              || isSolid(px + PLAYER_RADIUS, testY, pz)
              || isSolid(px - PLAYER_RADIUS, testY, pz)) {
                ny = (float)Math.floor(py);
                vy = 0;
            }
        }

        return new float[]{ny, vy};
    }

    private boolean isSolid(float wx, float wy, float wz) {
        int bx = (int)Math.floor(wx);
        int by = (int)Math.floor(wy);
        int bz = (int)Math.floor(wz);
        int bid = chunkManager.getBlockAt(bx, by, bz);
        return bid != 0 && fr.javacraft.world.BlockType.byId(bid).solid;
    }
}