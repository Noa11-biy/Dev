package fr.javacraft.core;

import fr.javacraft.render.*;
import fr.javacraft.world.*;
import fr.javacraft.entity.Player;
import fr.javacraft.input.InputManager;
import fr.javacraft.audio.AudioManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL33.*;

/**
 * Point d'entree principal de JavaCraft.
 * - GLFW + OpenGL 3.3 sur le thread main
 * - Audio OpenAL sur le thread main avec fail-safe
 */
public class Main {
    private static final String TITLE = "JavaCraft";
    private static final int WIDTH  = 1280;
    private static final int HEIGHT = 720;
    private static volatile boolean running = true;

    public static void main(String[] args) {

        // 1) GLFW — threads main uniquement
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("[JavaCraft] Impossible d'initialiser GLFW.");
        }

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        long window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new RuntimeException("[JavaCraft] ERREUR: Impossible de creer la fenetre GLFW.");
        }

        // Centre la fenetre (via VideoMode — evite glfwGetMonitorPos qui plante sur GPU Intel DCH)
        try {
            long monitor = GLFW.glfwGetPrimaryMonitor();
            if (monitor != 0) {
                GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);
                if (mode != null) {
                    GLFW.glfwSetWindowPos(window,
                        (mode.width()  - WIDTH)  / 2,
                        (mode.height() - HEIGHT) / 2);
                }
            }
        } catch (Throwable t) {
            // Ne bloque pas le lancement si le centrage echoue
            System.out.println("[JavaCraft] Centrage fenetre ignore: " + t.getMessage());
        }

        // Rend la fenetre visible
        GLFW.glfwShowWindow(window);

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GLFW.glfwSwapInterval(1);

        // 2) Input (GLFW callbacks — thread main)
        InputManager inputManager = new InputManager(window);

        // 3) Shader
        Shader shader = new Shader();
        shader.compile();
        shader.use();

        // 4) Textures
        TextureAtlas atlas = new TextureAtlas();
        atlas.bind();

        // 5) Renderer
        GameRenderer gameRenderer = new GameRenderer(atlas);
        gameRenderer.setShaderProgram(shader.getProgram());
        Renderer renderer = new Renderer(gameRenderer);
        renderer.setSkyColor(0.53f, 0.81f, 0.92f, 1.0f);

        // 6) Camera
        Player player = new Player();
        player.setPosition(0, 65, 0);
        FPSCamera camera = new FPSCamera(window, WIDTH, HEIGHT);

        // 7) World
        World world = new World();

        // 8) Audio (fail-safe — continue meme si OpenAL echoue)
        try {
            AudioManager.getInstance().init();
        } catch (Throwable t) {
            System.out.println("[JavaCraft] Audio ignore: " + t.getMessage());
        }

        System.out.println("[JavaCraft] Initialise — entrez dans le monde!");

        double lastTime = GLFW.glfwGetTime();

        while (running && !GLFW.glfwWindowShouldClose(window)) {
            double now = GLFW.glfwGetTime();
            double dt = Math.min(now - lastTime, 0.1); // cap a 100ms
            lastTime = now;

            // === POLL INPUT (GLFW) ===
            GLFW.glfwPollEvents();

            camera.update((float) dt);
            world.update(player.getX(), player.getZ());

            player.tick((float) dt);

            // === MATRICES ===
            Matrix4f proj = new Matrix4f()
                .perspective((float) Math.toRadians(70.0), (float) WIDTH / HEIGHT,
                             0.1f, 600.0f);
            Matrix4f view = camera.getViewMatrix();

            // === FRUSTUM CULLING ===
            Matrix4f vp = new Matrix4f();
            vp.mul(proj, view);

            // === RENDU (GL, thread main) ===
            renderer.clear();
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            gameRenderer.renderChunks(view);

            // === SWAP (GL, thread main) ===
            GLFW.glfwSwapBuffers(window);

            // === DEBUG LOG (tous les 5 secondes) ===
            if ((int)now % 5 == 0) {
                System.out.println("[JavaCraft] FPS: " + (1.0 / dt));
            }

        }

        // 10) Nettoyage
        atlas.delete();
        shader.delete();
        world.shutdown();
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
