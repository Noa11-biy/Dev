package fr.javacraft.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * Gestionnaire centralisé du clavier et de la souris.
 * Utilise GLFW callbacks pour remplir des tables d'état.
 * 
 * Un seul InputManager par application (singleton).
 */
public class InputManager {

    private static InputManager INSTANCE;

    // Table d'état des touches : pressed (au frame courant) / down (depuis le démarrage)
    private final boolean[] keyPressed = new boolean[512];
    private final boolean[] keyDown    = new boolean[512];

    // Boutons souris : pressed / down
    private final boolean[] mousePressed = new boolean[8];
    private final boolean[] mouseDown    = new boolean[8];

    // Handle de la fenêtre GLFW
    private final long window;

    /**
     * Crée (ou retourne) le singleton InputManager.
     */
    public InputManager(long window) {
        if (INSTANCE != null) {
            throw new IllegalStateException("InputManager déjà instancié.");
        }
        INSTANCE = this;
        this.window = window;

        // Callback clavier
        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long w, int key, int sc, int action, int mods) {
                if (key >= 0 && key < 512) {
                    if (action == GLFW.GLFW_PRESS) {
                        keyPressed[key] = true;
                        keyDown[key]    = true;
                    } else if (action == GLFW.GLFW_RELEASE) {
                        keyDown[key]    = false;
                    }
                }
            }
        });

        // Callback souris
        GLFW.glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long w, int button, int action, int mods) {
                if (button >= 0 && button < 8) {
                    if (action == GLFW.GLFW_PRESS) {
                        mousePressed[button] = true;
                        mouseDown[button]    = true;
                    } else if (action == GLFW.GLFW_RELEASE) {
                        mouseDown[button]    = false;
                    }
                }
            }
        });
    }

    /** Retourne le singleton (appelable après construction) */
    public static InputManager getInstance() {
        return INSTANCE;
    }

    /**
     * Vrai si la touche vient d'être pressée ce frame-ci.
     * Auto-résétée après l'appel.
     */
    public boolean isKeyPressed(int key) {
        if (key < 0 || key >= 512) return false;
        return keyPressed[key];
    }

    /** Vrai si la touche est maintenue enfoncée */
    public boolean isKeyDown(int key) {
        if (key < 0 || key >= 512) return false;
        return keyDown[key];
    }

    /** Vrai si le bouton souris vient d'être pressé ce frame */
    public boolean isMousePressed(int button) {
        if (button < 0 || button >= 8) return false;
        return mousePressed[button];
    }

    /** Vrai si le bouton souris est maintenu */
    public boolean isMouseDown(int button) {
        if (button < 0 || button >= 8) return false;
        return mouseDown[button];
    }

    /**
     * Remet à zéro les drapeaux "pressed" (appelé en fin de frame).
     * À appeler dans la boucle principale après le traitement des events.
     */
    public void resetPressed() {
        for (int i = 0; i < 512; i++) keyPressed[i]    = false;
        for (int i = 0; i < 8;   i++) mousePressed[i] = false;
    }
}