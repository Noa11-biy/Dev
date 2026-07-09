package fr.javacraft.core;

import fr.javacraft.world.ChunkManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Camera FPS (First-Person) pour JavaCraft Batch 1.
 * Deplacement libre avec physique de vol (WASD + souris + espace/shift).
 * Utilise la position monde du joueur pour le rendu.
 */
public class PlayerCamera {

    /** Handle de la fenetre GLFW */
    private final long window;

    /** Dimensions de la fenetre (pour centrage souris) */
    private final int width;
    private final int height;

    // --- Position et rotation ---
    /** Position monde (Y = hauteur) */
    private final Vector3f position = new Vector3f();

    /** Rotation (pitch = haut/bas, yaw = gauche/droite) en radians */
    private float pitch = 0.0f;
    private float yaw   = 0.0f;

    /** Pointeur lock (curseur capture) */
    private boolean mouseLocked = false;

    // --- Vitesse ---
    private static final float MOVE_SPEED     = 12.0f;  // m/s
    private static final float SPRINT_SPEED   = 20.0f;  // m/s (shift gauche = sprint)
    private static final float LOOK_SENS      = 0.0015f;
    private static final float MOUSE_SCALE    = 0.05f;

    // --- Accumulateurs de mouvement ---
    private float moveFB = 0.0f;
    private float moveLR = 0.0f;
    private float moveUD = 0.0f;

    /**
     * Cree la camera et enregistre les callbacks.
     * 
     * @param window  handle GLFW
     * @param width   largeur ecran
     * @param height  hauteur ecran
     */
    public PlayerCamera(long window, int width, int height) {
        this.window = window;
        this.width = width;
        this.height = height;

        // Position initiale au-dessus du terrain (chunk 0,0, hauteur ~40)
        this.position.set(8.0f, 70.0f, 8.0f);

        // Callback souris (movement)
        GLFW.glfwSetCursorPosCallback(window, (long w, double xpos, double ypos) -> {
            if (mouseLocked) {
                yaw   += (float)(xpos - width / 2.0) * LOOK_SENS;
                pitch -= (float)(ypos - height / 2.0) * LOOK_SENS;
                // Clamp pitch pour ne pas se retourner
                pitch = Math.max(-(float)Math.PI / 2.0f + 0.01f,
                                 Math.min((float)Math.PI / 2.0f - 0.01f, pitch));
                // Re-centre la souris
                GLFW.glfwSetCursorPos(window, width / 2.0, height / 2.0);
            }
        });

        // Callback clavier (pour ESC qui rend le curseur normal)
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
     * Mise a jour de la camera : lecture clavier, calcul deplacement.
     * Doit etre appelee chaque frame.
     * 
     * @param dt  delta temps en secondes
     */
    public void update(float dt) {
        fr.javacraft.input.InputManager input = fr.javacraft.input.InputManager.getInstance();

        // Reinitialise les accumulateurs
        moveFB = 0.0f; moveLR = 0.0f; moveUD = 0.0f;

        // Clavier
        if (input.isKeyDown(GLFW.GLFW_KEY_W)) moveFB += 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_S)) moveFB -= 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_A)) moveLR -= 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_D)) moveLR += 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_SPACE))      moveUD += 1.0f;
        if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) moveUD -= 1.0f;

        // Vitesse de sprint
        float speed = MOVE_SPEED;
        if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) speed = SPRINT_SPEED;

        // Calcul du deplacement dans l'espace monde
        // Vecteur avant selon yaw (projection au sol)
        float sinY = (float)Math.sin(yaw);
        float cosY = (float)Math.cos(yaw);

        float dx = sinY * moveFB + cosY * moveLR;
        float dz = cosY * moveFB - sinY * moveLR;

        position.x += dx * speed * dt;
        position.z += dz * speed * dt;
        position.y += moveUD * speed * dt;
    }

    /**
     * Recupere la matrice de vue (view matrix) selon la convention FPS.
     * Ry(pitch) * Rz(yaw) * T(-pos)
     * 
     * @return matrice de vue 4x4 (JOML, column-major)
     */
    public Matrix4f getViewMatrix() {
        Matrix4f view = new Matrix4f();
        view.rotateX(pitch);
        view.rotateY(yaw);
        view.translate((float)(-position.x), (float)(-position.y), (float)(-position.z));
        return view;
    }

    /** Position monde X */
    public float x() { return position.x; }
    /** Position monde Y */
    public float y() { return position.y; }
    /** Position monde Z */
    public float z() { return position.z; }
    /** Position monde (vecteur) */
    public Vector3f position() { return position; }
    /** Pitch en radians */
    public float pitch() { return pitch; }
    /** Yaw en radians */
    public float yaw() { return yaw; }

    /**
     * Capture la souris (pointer lock).
     * Appele depuis Main lors du premier clic.
     */
    public void lockMouse() {
        if (!mouseLocked) {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            GLFW.glfwSetCursorPos(window, width / 2.0, height / 2.0);
            mouseLocked = true;
        }
    }
}
