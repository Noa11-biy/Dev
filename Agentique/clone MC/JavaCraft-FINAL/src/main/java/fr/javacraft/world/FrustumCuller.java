package fr.javacraft.world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Culling de frustum (pyramide de vue camera).
 * Determine quels chunks sont visible depuis la camera actuelle
 * pour eviter d'envoyer des draw calls inutiles.
 * 
 * Utilise la technique des 6 plans de clipping (Hessian Normal Form).
 */
public class FrustumCuller {

    /** Les 6 plans du frustum (gauche, droite, bas, haut, near, far) */
    private final float[][] planes = new float[6][4]; // a,b,c,d de ax+by+cz+d=0

    /** Tampon pour les produits matriciels */
    private final float[] clip = new float[16];

    /**
     * Recalcule les plans du frustum a partir de la matrice de vue-projection.
     * A appeler chaque frame apres la mise a jour de la camera.
     * 
     * @param viewProjection  matrice 4x4 vue * projection (column-major, 16 floats)
     */
    public void update(float[] viewProjection) {
        // Extraction des 6 plans depuis la matrice VP
        // Voir Real-Time Rendering, 3rd ed., p. 739
        copyRow(0, viewProjection, 0);
        copyRow(1, viewProjection, 4);
        copyRow(2, viewProjection, 8);
        copyRow(3, viewProjection, 12);

        // Plan gauche
        planes[0][0] = clip[3] + clip[0];
        planes[0][1] = clip[7] + clip[4];
        planes[0][2] = clip[11] + clip[8];
        planes[0][3] = clip[15] + clip[12];
        normalizePlane(planes[0]);

        // Plan droit
        planes[1][0] = clip[3] - clip[0];
        planes[1][1] = clip[7] - clip[4];
        planes[1][2] = clip[11] - clip[8];
        planes[1][3] = clip[15] - clip[12];
        normalizePlane(planes[1]);

        // Plan bas
        planes[2][0] = clip[3] + clip[1];
        planes[2][1] = clip[7] + clip[5];
        planes[2][2] = clip[11] + clip[9];
        planes[2][3] = clip[15] + clip[13];
        normalizePlane(planes[2]);

        // Plan haut
        planes[3][0] = clip[3] - clip[1];
        planes[3][1] = clip[7] - clip[5];
        planes[3][2] = clip[11] - clip[9];
        planes[3][3] = clip[15] - clip[13];
        normalizePlane(planes[3]);

        // Plan near
        planes[4][0] = clip[3] + clip[2];
        planes[4][1] = clip[7] + clip[6];
        planes[4][2] = clip[11] + clip[10];
        planes[4][3] = clip[15] + clip[14];
        normalizePlane(planes[4]);

        // Plan far
        planes[5][0] = clip[3] - clip[2];
        planes[5][1] = clip[7] - clip[6];
        planes[5][2] = clip[11] - clip[10];
        planes[5][3] = clip[15] - clip[14];
        normalizePlane(planes[5]);
    }

    private void copyRow(int row, float[] src, int offset) {
        clip[row]     = src[offset];
        clip[row + 1] = src[offset + 1];
        clip[row + 2] = src[offset + 2];
        clip[row + 3] = src[offset + 3];
    }

    /** Normalise un plan (normale de longueur 1). */
    private void normalizePlane(float[] p) {
        float len = (float)Math.sqrt(p[0]*p[0] + p[1]*p[1] + p[2]*p[2]);
        if (len > 1e-6f) {
            p[0] /= len; p[1] /= len; p[2] /= len; p[3] /= len;
        }
    }

    /**
     * Teste si un AABB (Axis-Aligned Bounding Box) est dans le frustum.
     * 
     * @param minX minY minZ  coin inferieur de la boite
     * @param maxX maxY maxZ  coin superieur de la boite
     * @return true si la boite intersecte le frustum
     */
    public boolean intersectsAABB(float minX, float minY, float minZ,
                                   float maxX, float maxY, float maxZ) {
        // Teste chaque plan
        for (int i = 0; i < 6; i++) {
            float a = planes[i][0], b = planes[i][1], c = planes[i][2], d = planes[i][3];
            // Point le plus negatif dans la direction de la normale (coin oppose)
            float px = a < 0 ? maxX : minX;
            float py = b < 0 ? maxY : minY;
            float pz = c < 0 ? maxZ : minZ;
            // Distance du point au plan
            float dist = a * px + b * py + c * pz + d;
            if (dist < 0) return false; // Completement dehors
        }
        return true; // Au moins partiellement dedans
    }

    /**
     * Teste si un chunk est visible (simplifie : teste l'AABB du chunk).
     * 
     * @param cx  coordonnee X chunk
     * @param cz  coordonnee Z chunk
     * @return true si le chunk est potentiellement visible
     */
    public boolean isChunkVisible(int cx, int cz) {
        return intersectsAABB(
            cx * 16.0f, 0.0f, cz * 16.0f,
            (cx + 1) * 16.0f, 256.0f, (cz + 1) * 16.0f
        );
    }
}
