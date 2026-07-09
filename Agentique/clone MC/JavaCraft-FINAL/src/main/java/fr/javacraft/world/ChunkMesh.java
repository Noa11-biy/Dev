package fr.javacraft.world;

import java.nio.*;
import java.util.*;

/**
 * Mesh d'un chunk : construit les donnees de sommet pour le GPU.
 * Genere un mesh avec culling de face (seules les faces exposees
 * a l'air ou a un bloc transparent sont emises).
 * 
 * Format de sommet : x y z  u v  nx ny nz (8 floats, 32 octets)
 *   Position (x,y,z)  - float×3
 *   UV                - float×2
 *   Normale           - float×3
 * 
 * Indices : unsigned int (4 octets) pour les triangles.
 */
public class ChunkMesh {

    /** Donnees du chunk associe (pour acceder aux blocs voisins pendant le meshing) */
    public final ChunkData data;

    /** Buffers de donnees ( FloatBuffer vertices, IntBuffer indices ) */
    public FloatBuffer vertices;
    public IntBuffer indices;
    public int vertexCount;
    public int indexCount;

    /** Identifiants OpenGL (0 = pas encore uploade) */
    public int vaoId = 0;
    public int vboId = 0;
    public int eboId = 0;

    /** Vrai si le mesh a ete uploade sur le GPU (main thread) */
    public boolean uploaded = false;

    /** Vrai si le mesh est en cours de telechargement (evite les races) */
    private boolean uploading = false;

    public ChunkMesh(ChunkData data) {
        this.data = data;
    }

    /**
     * Genere le mesh de ce chunk en ne dessinant que les faces exposees. CPU only — AUCUN appel OpenGL..
     * Doit etre appele depuis un thread secondaire.
     * Les vertices et indices sont stockes dans les buffers CPU.
     */
    public void build() {
        List<Float> verts = new ArrayList<>();
        List<Integer> idxs = new ArrayList<>();
        int vi = 0; // compteur de sommets

        // Parcours de tous les blocs du chunk
        for (int lx = 0; lx < 16; lx++) {
            for (int ly = 0; ly < 256; ly++) {
                for (int lz = 0; lz < 16; lz++) {
                    BlockType block = data.getBlock(lx, ly, lz);
                    if (block == BlockType.AIR || (!block.opaque && !block.transparent)) {
                        continue;
                    }

                    // Position monde absolue du bloc
                    float wx = data.worldX(lx);
                    float wy = data.worldY(ly);
                    float wz = data.worldZ(lz);

                    // Pour chaque face du bloc
                    // Face +Y (haut)
                    if (shouldRenderFace(data, lx, ly, lz, 0, 1, 0)) {
                        addFace(verts, idxs,
                            wx, wy, wz,
                            block.tileIndex,
                            0.0f, 1.0f, 0.0f,  // normale
                            0, 1, 1, 1,        // quad
                            vi);
                        vi += 4;
                    }
                    // Face -Y (bas)
                    if (shouldRenderFace(data, lx, ly, lz, 0, -1, 0)) {
                        addFace(verts, idxs,
                            wx, wy, wz,
                            block.tileIndex,
                            0.0f, -1.0f, 0.0f,
                            0, 0, 1, 0,
                            vi);
                        vi += 4;
                    }
                    // Face +Z (devant / sud)
                    if (shouldRenderFace(data, lx, ly, lz, 0, 0, 1)) {
                        addFace(verts, idxs,
                            wx, wy, wz,
                            block.tileIndex,
                            0.0f, 0.0f, 1.0f,
                            1, 0, 0, 1,
                            vi);
                        vi += 4;
                    }
                    // Face -Z (derriere / nord)
                    if (shouldRenderFace(data, lx, ly, lz, 0, 0, -1)) {
                        addFace(verts, idxs,
                            wx, wy, wz,
                            block.tileIndex,
                            0.0f, 0.0f, -1.0f,
                            0, 0, 1, 0,
                            vi);
                        vi += 4;
                    }
                    // Face +X (droite / est)
                    if (shouldRenderFace(data, lx, ly, lz, 1, 0, 0)) {
                        addFace(verts, idxs,
                            wx, wy, wz,
                            block.tileIndex,
                            1.0f, 0.0f, 0.0f,
                            1, 0, 1, 1,
                            vi);
                        vi += 4;
                    }
                    // Face -X (gauche / ouest)
                    if (shouldRenderFace(data, lx, ly, lz, -1, 0, 0)) {
                        addFace(verts, idxs,
                            wx, wy, wz,
                            block.tileIndex,
                            -1.0f, 0.0f, 0.0f,
                            0, 0, 1, 0,
                            vi);
                        vi += 4;
                    }
                }
            }
        }

        this.vertexCount = vi;
        this.indexCount = idxs.size();

        // Allocation directe des buffers NIO (utilise MemoryUtil pour etre sur du malloc)
        this.vertices = org.lwjgl.system.MemoryUtil.memAllocFloat(verts.size());
        for (float f : verts) vertices.put(f);
        vertices.flip();

        this.indices = org.lwjgl.system.MemoryUtil.memAllocInt(idxs.size());
        for (int i : idxs) indices.put(i);
        indices.flip();
    }

    /**
     * Determine si une face doit etre rendue.
     * Une face n'est pas rendue si le bloc adjacent est:
     *  - un bloc AIR (visible)
     *  - un bloc semi-transparent (eau) (visible a travers)
     * 
     * @param data  chunk actuel
     * @param lx    coordonnee X locale du bloc courant
     * @param ly    coordonnee Y locale du bloc courant
     * @param lz    coordonnee Z locale du bloc courant
     * @param dx    direction X de la face
     * @param dy    direction Y de la face
     * @param dz    direction Z de la face
     * @return true si la face doit etre rendue
     */
    private boolean shouldRenderFace(ChunkData data, int lx, int ly, int lz, int dx, int dy, int dz) {
        BlockType adj = getAdjacentBlock(data, lx, ly, lz, dx, dy, dz);
        return adj == BlockType.AIR || adj.transparent;
    }

    /**
     * Recupere le bloc adjacent (meme chunk ou chunk voisin).
     */
    private BlockType getAdjacentBlock(ChunkData data, int lx, int ly, int lz, int dx, int dy, int dz) {
        int nx = lx + dx;
        int ny = ly + dy;
        int nz = lz + dz;

        // Hors du chunk actuel — demander au ChunkManager
        if (nx < 0 || nx >= 16 || nz < 0 || nz >= 16) {
            ChunkData adj = fr.javacraft.world.ChunkManager.getInstance()
                .getChunk(data.cx + dx, data.cz + dz);
            if (adj == null) return BlockType.AIR;
            return adj.getBlock(nx < 0 ? nx + 16 : (nx >= 16 ? nx - 16 : nx),
                                ny,
                                nz < 0 ? nz + 16 : (nz >= 16 ? nz - 16 : nz));
        }

        // Dans le meme chunk
        if (ny < 0 || ny >= 256) return BlockType.AIR;
        return data.getBlock(nx, ny, nz);
    }

    /**
     * Ajoute un quad (deux triangles) au mesh.
     * 
     * @param verts  liste des vertices
     * @param idxs   liste des indices
     * @param wx     position monde X du bloc
     * @param wy     position monde Y du bloc
     * @param wz     position monde Z du bloc
     * @param tile   index de tuile dans le texture atlas [0..7]
     * @param nx ny nz  normale de la face
     * @param s0 t0 s1 t1  coordonnees UV des 4 coins (smin, tmin, smax, tmax)
     * @param baseIdx  indice de base du premier sommet ajoute
     */
    private void addFace(List<Float> verts, List<Integer> idxs,
                         float wx, float wy, float wz,
                         int tile, float nx, float ny, float nz,
                         float s0, float t0, float s1, float t1,
                         int baseIdx) {
        // Tiles disposes en ligne sur l'atlas (TILES=8 dans un atlas de 128×128)
        int TILES_PER_ROW = 8;
        int ATLAS_TEXELS = 128;
        int tileSize = ATLAS_TEXELS / TILES_PER_ROW; // 16 texels par tuile

        // UV normalises [0,1] pour la tuile dans l'atlas
        float u0 = ((tile % TILES_PER_ROW) * tileSize) / (float)ATLAS_TEXELS;
        float u1 = u0 + (tileSize - 1) / (float)ATLAS_TEXELS;
        float v0 = ((tile / TILES_PER_ROW) * tileSize) / (float)ATLAS_TEXELS;
        float v1 = v0 + (tileSize - 1) / (float)ATLAS_TEXELS;

        // Applique les UV min/max de l'appelant


        // Les 4 sommets selon l'orientation
        // Vertices format: x y z u v nx ny nz (8 floats = 32 octets)
        if (ny > 0) { // haut (+Y)
            addVertex(verts, wx - 0.5f, wy + 0.5f, wz - 0.5f, u0, v1, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy + 0.5f, wz - 0.5f, u1, v1, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy + 0.5f, wz + 0.5f, u1, v0, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy + 0.5f, wz + 0.5f, u0, v0, nx, ny, nz);
        } else if (ny < 0) { // bas (-Y)
            addVertex(verts, wx - 0.5f, wy - 0.5f, wz + 0.5f, u0, v0, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy - 0.5f, wz + 0.5f, u1, v0, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy - 0.5f, wz - 0.5f, u1, v1, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy - 0.5f, wz - 0.5f, u0, v1, nx, ny, nz);
        } else if (nz > 0) { // face sud (+Z)
            addVertex(verts, wx + 0.5f, wy - 0.5f, wz + 0.5f, u1, v1, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy - 0.5f, wz + 0.5f, u0, v1, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy + 0.5f, wz + 0.5f, u0, v0, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy + 0.5f, wz + 0.5f, u1, v0, nx, ny, nz);
        } else if (nz < 0) { // face nord (-Z)
            addVertex(verts, wx - 0.5f, wy - 0.5f, wz - 0.5f, u0, v1, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy - 0.5f, wz - 0.5f, u1, v1, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy + 0.5f, wz - 0.5f, u1, v0, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy + 0.5f, wz - 0.5f, u0, v0, nx, ny, nz);
        } else if (nx > 0) { // face est (+X)
            addVertex(verts, wx + 0.5f, wy - 0.5f, wz - 0.5f, u1, v1, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy - 0.5f, wz + 0.5f, u0, v1, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy + 0.5f, wz + 0.5f, u0, v0, nx, ny, nz);
            addVertex(verts, wx + 0.5f, wy + 0.5f, wz - 0.5f, u1, v0, nx, ny, nz);
        } else { // face ouest (-X)
            addVertex(verts, wx - 0.5f, wy - 0.5f, wz + 0.5f, u0, v1, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy - 0.5f, wz - 0.5f, u1, v1, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy + 0.5f, wz - 0.5f, u1, v0, nx, ny, nz);
            addVertex(verts, wx - 0.5f, wy + 0.5f, wz + 0.5f, u0, v0, nx, ny, nz);
        }

        // Deux triangles (6 indices) par quad
        idxs.add(baseIdx);
        idxs.add(baseIdx + 1);
        idxs.add(baseIdx + 2);
        idxs.add(baseIdx);
        idxs.add(baseIdx + 2);
        idxs.add(baseIdx + 3);
    }

    private void addVertex(List<Float> verts, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        verts.add(x); verts.add(y); verts.add(z);
        verts.add(u); verts.add(v);
        verts.add(nx); verts.add(ny); verts.add(nz);
    }

    /**
     * Upload le mesh sur le GPU (thread OpenGL principal UNIQUEMENT)..
     * Libere les buffers CPU apres upload.
     */
    public void uploadToGPU() {
        if (uploaded || uploading) return;
        if (vertices == null || vertices.limit() == 0) {
            uploaded = true; // pas de donnees, pas d'erreur
            return;
        }
        uploading = true;

        // Cree VAO
        int[] vao = new int[1];
        org.lwjgl.opengl.GL33.glGenVertexArrays(vao);
        this.vaoId = vao[0];
        org.lwjgl.opengl.GL33.glBindVertexArray(vaoId);

        // VBO positions + uv + normales
        int[] vbo = new int[1];
        org.lwjgl.opengl.GL33.glGenBuffers(vbo);
        this.vboId = vbo[0];
        org.lwjgl.opengl.GL33.glBindBuffer(org.lwjgl.opengl.GL33.GL_ARRAY_BUFFER, vboId);
        org.lwjgl.opengl.GL33.glBufferData(
            org.lwjgl.opengl.GL33.GL_ARRAY_BUFFER,
            vertices,
            org.lwjgl.opengl.GL33.GL_STATIC_DRAW);

        // Layout 0: position (3 floats)
        org.lwjgl.opengl.GL33.glVertexAttribPointer(0, 3, org.lwjgl.opengl.GL33.GL_FLOAT, false, 32, 0L);
        org.lwjgl.opengl.GL33.glEnableVertexAttribArray(0);
        // Layout 1: UV (2 floats, offset 12 octets)
        org.lwjgl.opengl.GL33.glVertexAttribPointer(1, 2, org.lwjgl.opengl.GL33.GL_FLOAT, false, 32, 12L);
        org.lwjgl.opengl.GL33.glEnableVertexAttribArray(1);
        // Layout 2: normale (3 floats, offset 20 octets)
        org.lwjgl.opengl.GL33.glVertexAttribPointer(2, 3, org.lwjgl.opengl.GL33.GL_FLOAT, false, 32, 20L);
        org.lwjgl.opengl.GL33.glEnableVertexAttribArray(2);

        // EBO indices
        int[] ebo = new int[1];
        org.lwjgl.opengl.GL33.glGenBuffers(ebo);
        this.eboId = ebo[0];
        org.lwjgl.opengl.GL33.glBindBuffer(org.lwjgl.opengl.GL33.GL_ELEMENT_ARRAY_BUFFER, eboId);
        org.lwjgl.opengl.GL33.glBufferData(
            org.lwjgl.opengl.GL33.GL_ELEMENT_ARRAY_BUFFER,
            indices,
            org.lwjgl.opengl.GL33.GL_STATIC_DRAW);

        org.lwjgl.opengl.GL33.glBindVertexArray(0);

        // Libere memoire CPU
        org.lwjgl.system.MemoryUtil.memFree(vertices);
        org.lwjgl.system.MemoryUtil.memFree(indices);
        vertices = null;
        indices = null;

        uploaded = true;
        uploading = false;
    }

    /** Supprime les ressources GPU. Appele lors du dechargement du chunk. */
    public void delete() {
        if (vboId != 0) {
            int[] v = {vboId};
            org.lwjgl.opengl.GL33.glDeleteBuffers(v);
            vboId = 0;
        }
        if (eboId != 0) {
            int[] e = {eboId};
            org.lwjgl.opengl.GL33.glDeleteBuffers(e);
            eboId = 0;
        }
        if (vaoId != 0) {
            int[] vao = {vaoId};
            org.lwjgl.opengl.GL33.glDeleteVertexArrays(vao);
            vaoId = 0;
        }
        uploaded = false;
    }
}
