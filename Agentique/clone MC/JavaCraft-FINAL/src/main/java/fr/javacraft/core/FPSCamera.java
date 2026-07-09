package fr.javacraft.core;

import fr.javacraft.input.InputManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * Caméra FPS (First-Person Shooter) inspirée de Minecraft.
 * Déplacement libre WASD + rotation à la souris, avec pointer lock.
 * 
 * Le monde est traduit autour de la caméra pour simuler un mouvement
 * classique en vue à la première personne.
 */
public class FPSCamera {

    // Fenêtre GLFW
    private final long window;

    // Position de la caméra (sera inversée pour le monde)
    private float pitch = 0.0f;   // Inclinaison haut/bas (radians)
    private float yaw = 0.0f;     // Rotation gauche/droite (radians)

    // Vecteur direction
    private final Vector3f direction = new Vector3f();

    // Position de la caméra dans le monde
    private final Vector3f position = new Vector3f();

    // Accumulateurs de mouvement
    private float forwardBack = 0.0f;
    private float leftRight   = 0.0f;
    private float upDown      = 0.0f;

    // Vitesses
    private static final float MOVE_SPEED   = 8.0f;   // m/s au sol
    private static final float LOOK_SENSITIVITY = 0.002f;

    // État du pointer lock
    private boolean mouseLocked = false;

    /**
     * Crée la caméra et enregistre les callbacks GLFW.
     * 
     * @param window      handle de la fenêtre GLFW
     * @param screenWidth  largeur de l'écran (pour centrer la souris)
     */
    public FPSCamera(long window, int screenWidth, int screenHeight) {
        this.window = window;

        // Callback de mouvement de souris
        GLFW.glfwSetCursorPosCallback(window, (long w, double xpos, double ypos) -> {
            if (mouseLocked) {
                // Formule standard FPS : deltaY → pitch, deltaX → yaw
                yaw   += (float)xpos * LOOK_SENSITIVITY;
                pitch -= (float)ypos * LOOK_SENSITIVITY;
                // Limite le pitch pour ne pas se retourner
                pitch = Math.max(-(float)Math.PI / 2.0f + 0.01f,
                                 Math.min((float)Math.PI / 2.0f - 0.01f, pitch));
            }
        });

        // Callback d'acquisition/perte du focus souris
        GLFW.glfwSetMouseButtonCallback(window, (long w, int button, int action, int mods) -> {
            if (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS) {
                if (!mouseLocked) {
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                    mouseLocked = true;
                }
            }
        });

        // Intercepte ESC pour libérer le pointer lock
        GLFW.glfwSetKeyCallback(window, (long w, int key, int sc, int action, int mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                if (mouseLocked) {
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                    mouseLocked = false;
                }
            }
        });
    }

    /**
     * Met à jour la caméra : lit le clavier, calcule le déplacement.
     * 
     * @param dt  delta temps en secondes depuis le dernier frame
     */
    public void update(float dt) {
        InputManager input = InputManager.getInstance();

        // Relâche les accumulateurs si la touche n'est plus pressée
        forwardBack = 0.0f;
        leftRight   = 0.0f;
        upDown      = 0.0f;

        if (input.isKeyDown(GLFW.GLFW_KEY_W)) forwardBack += 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_S)) forwardBack -= 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_A)) leftRight   -= 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_D)) leftRight   += 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_SPACE))      upDown += 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) upDown -= 1.0f;
    }

    /**
     * Calcule la matrice de vue (view matrix) selon la convention FPS.
     * On tourne d'abord, puis on translate, ce qui donne l'impression
     * que la caméra se déplace dans le monde.
     * 
     * @return une matrice 4×4 JOML (Column-Major pour OpenGL)
     */
    public Matrix4f getViewMatrix() {
        // Direction basées sur pitch et yaw
        direction.set(
            (float)(Math.sin(yaw) * Math.cos(pitch)),
            (float) Math.sin(pitch),
            (float)(-Math.cos(yaw) * Math.cos(pitch))
        ).normalize();

        // Matrice de vue = rotation puis translation
        // Ry(pitch) * Rz(yaw) * T(-pos)
        Matrix4f view = new Matrix4f();
        view.rotateX(pitch);
        view.rotateY(yaw);
        view.translate((float)(-position.x), (float)(-position.y), (float)(-position.z));
        return view;
    }

    /** Retourne la position actuelle de la camera. */
    public Vector3f getPosition() { return position; }
}
