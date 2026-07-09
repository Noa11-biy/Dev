package fr.javacraft.render;

/**
 * Moteur de rendu principal de JavaCraft.
 * Gere le rendu global : sky, chunks, GUI.
 * Delegates au GameRenderer pour le rendu des chunks.
 */
public class Renderer {

    private final GameRenderer gameRenderer;

    public Renderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }

    /**
     * Configure le viewport OpenGL.
     * 
     * @param width  largeur en pixels
     * @param height hauteur en pixels
     */
    public void resize(int width, int height) {
        org.lwjgl.opengl.GL33.glViewport(0, 0, width, height);
    }

    /**
     * Efface les buffers avant chaque frame.
     */
    public void clear() {
        org.lwjgl.opengl.GL33.glClear(
            org.lwjgl.opengl.GL33.GL_COLOR_BUFFER_BIT
            | org.lwjgl.opengl.GL33.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Configure la couleur du sky (glClearColor).
     * 
     * @param r g b a  composantes [0,1]
     */
    public void setSkyColor(float r, float g, float b, float a) {
        org.lwjgl.opengl.GL33.glClearColor(r, g, b, a);
    }

    /**
     * Rendu d'un frame.
     * 
     * @param view  matrice de vue (camera)
     */
    public void renderFrame(org.joml.Matrix4f view) {
        if (gameRenderer != null) {
            gameRenderer.renderChunks(view);
        }
    }

    public GameRenderer getGameRenderer() { return gameRenderer; }
}
