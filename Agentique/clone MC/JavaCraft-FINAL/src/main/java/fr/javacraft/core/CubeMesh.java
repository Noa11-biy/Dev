package fr.javacraft.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL33.*;

/**
 * Mesh d'un cube unitaire (1×1×1) avec UV procéduraux.
 * Utilise un VAO/VBO avec vertex format:
 *   [x, y, z, u, v]  →  5 floats par sommet, 24 sommets (4 par face × 6 faces)
 *   + index buffer pour les triangles.
 * 
 * Les faces ne sont pas partagées pour éviter les coutures de texture
 * (batch 0 = simple, pas de shared vertices).
 */
public class CubeMesh {

    private int vaoId;
    private int vboId;
    private int eboId;
    private int vertexCount;
    private int indexCount;

    /** Répartition mémoire (en floats) */
    private static final int STRIDE = 5; // x y z u v

    /**
     * Construit le mesh CPU-side.
     * Chaque face = 4 sommets distincts pour.allow free UV mapping.
     */
    public CubeMesh() {
        // 24 sommets × 5 floats
        float[] vertices = new float[24 * STRIDE];

        // Helper: ajoute un carré (2 triangles) de taille 1×1
        // cy = centre Y de la face, my = composante normale Y, etc.
        int i = 0;
        // Face avant (+Z)
        i = addFace(vertices, i,  0f, 0f, 0f,  1f, 0f, 0f,  0f, 1f, 0f,  0f, 0f, 1f,  false);
        // Face arrière (-Z)
        i = addFace(vertices, i,  1f, 0f, 0f,  0f, 0f, 0f,  0f, 1f, 0f,  0f, 0f,-1f,  false);
        // Face gauche (-X)
        i = addFace(vertices, i,  0f, 0f, 1f,  0f, 0f, 0f,  0f, 1f, 0f, -1f, 0f, 0f,  false);
        // Face droite (+X)
        i = addFace(vertices, i,  0f, 0f, 0f,  0f, 0f, 1f,  0f, 1f, 0f,  1f, 0f, 0f,  false);
        // Face haut (+Y)
        i = addFace(vertices, i,  0f, 1f, 0f,  1f, 0f, 0f,  0f, 0f, 1f,  0f, 1f, 0f,  true);
        // Face bas (-Y)
        i = addFace(vertices, i,  0f, 0f, 1f,  1f, 0f, 1f,  1f, 0f, 0f,  0f,-1f, 0f,  false);

        // Indices : 2 triangles par face × 6 faces = 36 indices
        int[] indices = new int[36];
        int idx = 0;
        int base = 0;
        for (int f = 0; f < 6; f++) {
            // Quad → 2 triangles (triangle strip en sens horaire)
            indices[idx++] = base + 0;
            indices[idx++] = base + 1;
            indices[idx++] = base + 2;
            indices[idx++] = base + 2;
            indices[idx++] = base + 1;
            indices[idx++] = base + 3;
            base += 4;
        }

        this.vertexCount = 24;
        this.indexCount  = 36;
    }

    /** Ajoute 4 sommets d'une face carrée, renvoie le nouvel index */
    private int addFace(float[] v, int i,
                       float x0, float y0, float z0,
                       float x1, float y1, float z1,
                       float x2, float y2, float z2,
                       float nx, float ny, float nz,
                       boolean topFace) {
        // Sommets dans le sens horaire (vu de l'extérieur)
        // v0
        v[i++] = x0; v[i++] = y0; v[i++] = z0; v[i++] = 0.0f; v[i++] = topFace ? 1.0f : 0.0f;
        // v1
        v[i++] = x1; v[i++] = y1; v[i++] = z1; v[i++] = 1.0f; v[i++] = topFace ? 1.0f : 0.0f;
        // v2
        v[i++] = x2; v[i++] = y2; v[i++] = z2; v[i++] = 0.0f; v[i++] = topFace ? 0.0f : 1.0f;
        // v3
        v[i++] = x1; v[i++] = y1; v[i++] = z1; v[i++] = 1.0f; v[i++] = topFace ? 0.0f : 1.0f;
        return i;
    }

    /**
     * Upload GPU : crée VAO/VBO/EBO et envoie les données.
     * Doit être appelé depuis un thread avec contexte OpenGL actif.
     */
    public void upload() {
        // VAO
        IntBuffer vaoBuf = IntBuffer.allocate(1);
        glGenVertexArrays(vaoBuf);
        this.vaoId = vaoBuf.get(0);
        glBindVertexArray(vaoId);

        // VBO (position + UV)
        IntBuffer vboBuf = IntBuffer.allocate(1);
        glGenBuffers(vboBuf);
        this.vboId = vboBuf.get(0);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        // Remplit le VBO (les 24 sommets sont déjà dans le tableau,
        // on reconstruira le mesh en dur pour garder le code lisible)
        float[] verts = buildVertexData();
        FloatBuffer fb = FloatBuffer.allocate(verts.length);
        fb.put(verts);
        fb.flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        // EBO (indices)
        IntBuffer eboBuf = IntBuffer.allocate(1);
        glGenBuffers(eboBuf);
        this.eboId = eboBuf.get(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);

        int[] idx = buildIndexData();
        IntBuffer ib = IntBuffer.allocate(idx.length);
        ib.put(idx);
        ib.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

        // Configure les attributs de vertex (format: x y z u v)
        // Position (loc = 0) : 3 premiers floats
        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE * 4, 0L);
        glEnableVertexAttribArray(0);
        // UV (loc = 1) : 2 derniers floats
        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE * 4, 3L * 4);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0); // Dé-attache le VAO
        System.out.println("[CubeMesh] Uploadé. VAO=" + vaoId + " VBO=" + vboId + " EBO=" + eboId);
    }

    /** Assemble les 24 sommets exacts pour le VBO */
    private float[] buildVertexData() {
        float[] verts = new float[24 * STRIDE];
        int i = 0;
        // Face +Z
        i = face(i, verts, 0,0,1, 1,0,1, 1,1,1, 0,1,1,  0,0,1);
        // Face -Z
        i = face(i, verts, 1,0,0, 0,0,0, 0,1,0, 1,1,0,  0,0,-1);
        // Face +X
        i = face(i, verts, 1,0,1, 1,0,0, 1,1,0, 1,1,1,  1,0,0);
        // Face -X
        i = face(i, verts, 0,0,0, 0,0,1, 0,1,1, 0,1,0, -1,0,0);
        // Face +Y
        i = face(i, verts, 0,1,1, 1,1,1, 1,1,0, 0,1,0,  0,1,0);
        // Face -Y
        i = face(i, verts, 0,0,0, 1,0,0, 1,0,1, 0,0,1,  0,-1,0);
        return verts;
    }

    private int face(int i, float[] v, float x0,float y0,float z0,
                     float x1,float y1,float z1,
                     float x2,float y2,float z2,
                     float x3,float y3,float z3,
                     float nx,float ny,float nz) {
        // v0
        v[i++]=x0; v[i++]=y0; v[i++]=z0; v[i++]=0f; v[i++]=0f;
        // v1
        v[i++]=x1; v[i++]=y1; v[i++]=z1; v[i++]=1f; v[i++]=0f;
        // v2
        v[i++]=x2; v[i++]=y2; v[i++]=z2; v[i++]=0f; v[i++]=1f;
        // v3
        v[i++]=x3; v[i++]=y3; v[i++]=z3; v[i++]=1f; v[i++]=1f;
        return i;
    }

    /** Assemble le tableau d'indices */
    private int[] buildIndexData() {
        int[] idx = new int[36];
        int k = 0, base = 0;
        for (int f = 0; f < 6; f++) {
            idx[k++]=base;   idx[k++]=base+1; idx[k++]=base+2;
            idx[k++]=base+2; idx[k++]=base+1; idx[k++]=base+3;
            base += 4;
        }
        return idx;
    }

    /**
     * Dessine le cube avec glDrawElements.
     * Appeler entre glUseProgram et glClear.
     */
    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L);
        glBindVertexArray(0);
    }

    /**
     * Libère les ressources GPU.
     * Appeler à l'arrêt du programme.
     */
    public void delete() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        System.out.println("[CubeMesh] Ressources libérées.");
    }
}