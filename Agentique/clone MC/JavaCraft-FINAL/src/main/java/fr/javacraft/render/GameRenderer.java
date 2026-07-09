package fr.javacraft.render;

import fr.javacraft.world.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Moteur de rendu pour le monde voxel.
 * Gere le rendu de tous les chunks avec culling de frustum
 * et l'atlas de textures partage.
 */
public class GameRenderer {

    /** Atlas de textures */
    private final TextureAtlas atlas;

    /** Culling de frustum */
    private final FrustumCuller frustumCuller = new FrustumCuller();

    /** Shader program (recupere depuis Shader.compile) */
    private int shaderProgram = 0;

    /** Uniform locations */
    private int uProjection = -1;
    private int uView       = -1;
    private int uModel      = -1;
    private int uTex        = -1;

    /** Compteur de chunks rendus ce frame */
    private int chunksRendered = 0;

    /**
     * Cree le rendu avec l'atlas de textures.
     * 
     * @param atlas  l'atlas procedural genere au prealable
     */
    public GameRenderer(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    /**
     * Configure le program shader (appele apres shader.compile() dans Main).
     * 
     * @param program  program ID OpenGL
     */
    public void setShaderProgram(int program) {
        this.shaderProgram = program;
        if (program > 0) {
            this.uProjection = org.lwjgl.opengl.GL33.glGetUniformLocation(program, "projection");
            this.uView       = org.lwjgl.opengl.GL33.glGetUniformLocation(program, "view");
            this.uModel      = org.lwjgl.opengl.GL33.glGetUniformLocation(program, "model");
            this.uTex        = org.lwjgl.opengl.GL33.glGetUniformLocation(program, "tex");
        }
    }

    /**
     * Met a jour les plans du frustum depuis la matrice VP.
     * 
     * @param viewProjection  matrice vue*projection (16 floats)
     */
    public void updateFrustum(float[] viewProjection) {
        frustumCuller.update(viewProjection);
    }

    /**
     * Upload les matrices projection et view au shader chaque frame.
     * Sans cet appel, le terrain est invisible (matrices nulles → vertees hors ecran).
     * 
     * @param projection  matrice de projection
     * @param view        matrice de vue
     */
    public void uploadMatrices(Matrix4f projection, Matrix4f view) {
        if (shaderProgram <= 0) return;
        org.lwjgl.opengl.GL33.glUseProgram(shaderProgram);
        float[] projArr = projection.get(new float[16]);
        float[] viewArr = view.get(new float[16]);
        org.lwjgl.opengl.GL33.glUniformMatrix4fv(uProjection, false, projArr);
        org.lwjgl.opengl.GL33.glUniformMatrix4fv(uView, false, viewArr);
    }

    /**
     * Rend tous les chunks visibles (frustum culled).
     * Doit etre appele chaque frame apres updateFrustum.
     * 
     * @param view  matrice de vue
     */
    public void renderChunks(Matrix4f view) {
        if (shaderProgram <= 0) return;

        atlas.bind();
        org.lwjgl.opengl.GL33.glUseProgram(shaderProgram);

        // Unit texture 0
        org.lwjgl.opengl.GL33.glUniform1i(uTex, 0);

        chunksRendered = 0;
        int culled = 0;

        // Matrice model = identite (les chunks sont deja en position monde)
        Matrix4f model = new Matrix4f();
        org.lwjgl.opengl.GL33.glUniformMatrix4fv(uModel, false, model.get(new float[16]));

        Matrix4f vp = new Matrix4f();
        vp.mul(view, new Matrix4f());

        for (ChunkMesh mesh : ChunkManager.getInstance().getAllMeshes()) {
            if (!mesh.uploaded || mesh.vaoId == 0) continue;

            // Culling de frustum
            int cx = mesh.data.cx;
            int cz = mesh.data.cz;
            if (!frustumCuller.isChunkVisible(cx, cz)) {
                culled++;
                continue;
            }

            org.lwjgl.opengl.GL33.glBindVertexArray(mesh.vaoId);
            org.lwjgl.opengl.GL33.glDrawElements(
                org.lwjgl.opengl.GL33.GL_TRIANGLES,
                mesh.indexCount,
                org.lwjgl.opengl.GL33.GL_UNSIGNED_INT,
                0L);
            chunksRendered++;
        }

        org.lwjgl.opengl.GL33.glBindVertexArray(0);
    }

    /** Compteur de chunks rendus ce frame. */
    public int getChunksRendered() { return chunksRendered; }
}
