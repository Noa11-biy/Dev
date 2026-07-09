package fr.javacraft.gui;

import fr.javacraft.item.*;
import org.lwjgl.opengl.GL33;
import static org.lwjgl.opengl.GL33.*;

/**
 * Interface graphique (HUD, inventaire, GUI des blocs).
 * Rendu 2D via OpenGL (ortho) avec blend.
 * Mode inventaire déclenché par E, GUI de fourneau par clic droit sur un four.
 */
public class GUI {

    private static GUI INSTANCE;
    private final Inventory inventory;
    private final CraftingManager crafting;
    private final EnchantingManager enchanting;

    private boolean inventoryOpen = false;
    private int selectedSlot = 0;
    private int craftingGrid = 0; // 0 = inventaire, 1 = grid 3x3

    // Grille de craft GUI (3×3)
    private final ItemStack[] craftGrid = new ItemStack[9];
    private ItemStack craftResult = null;

    private GUI() {
        this.inventory = new Inventory();
        this.crafting = new CraftingManager();
        this.enchanting = new EnchantingManager();
    }

    public static GUI getInstance() {
        if (INSTANCE == null) INSTANCE = new GUI();
        return INSTANCE;
    }

    public void toggleInventory() {
        inventoryOpen = !inventoryOpen;
        System.out.println("[GUI] Inventaire " + (inventoryOpen ? "ouvert" : "ferme"));
    }

    public boolean isInventoryOpen() { return inventoryOpen; }

    /** Appelée chaque frame pour rendre l'HUD */
    public void render(float playerHealth, float maxHealth, int slot) {
        this.selectedSlot = slot;
        renderHUD(playerHealth, maxHealth);

        if (inventoryOpen) {
            renderInventoryScreen();
        }
    }

    private void renderHUD(float health, float maxHealth) {
        // Fond transparent pour le HUD (box 0.7, 0.85, 0.95, 0.95)
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float ratio = health / maxHealth;
        int fullBars = (int)(ratio * 10);

        // Barre de vie (10 cœurs) — bas gauche
        float heartX = 0.05f, heartY = 0.88f, heartSize = 0.025f;
        for (int i = 0; i < 10; i++) {
            boolean full = i < fullBars;
            boolean half = !full && i == fullBars;
            renderHeart(heartX + i * (heartSize * 2.2f), heartY, heartSize,
                        full ? 2 : (half ? 1 : 0));
        }

        glDisable(GL_BLEND);
    }

    private void renderHeart(float x, float y, float size, int state) {
        // Draw heart using OpenGL quads
        float r = 0, g = 0, b = 0;
        switch(state) {
            case 2 -> { r = 0.9f; g = 0.1f; b = 0.1f; } // Full red
            case 1 -> { r = 0.9f; g = 0.5f; b = 0.1f; } // Half
            default -> { r = 0.3f; g = 0.2f; b = 0.2f; } // Empty
        }
        glColor4f(r, g, b, 1.0f);
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + size, y);
        glVertex2f(x + size, y + size);
        glVertex2f(x, y + size);
        glEnd();
        glColor4f(1, 1, 1, 1);
    }

    private void renderInventoryScreen() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Fond semi-transparent
        glColor4f(0.2f, 0.2f, 0.2f, 0.85f);
        renderQuad(0.1f, 0.1f, 0.8f, 0.8f);

        glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
        renderQuad(0.25f, 0.05f, 0.5f, 0.9f); // Zone hotbar

        glColor4f(1, 1, 1, 1);
        glDisable(GL_BLEND);
    }

    private void renderQuad(float x1, float y1, float x2, float y2) {
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
    }

    /** Gère l'interaction slot (clic gauche = sélectionner, clic droit = déposer) */
    public void onSlotClick(int slot, int button) {
        if (slot < 0 || slot >= Inventory.TOTAL_SIZE) return;
        if (button == 0) inventory.setSelectedSlot(slot);
    }

    public Inventory getInventory()     { return inventory; }
    public CraftingManager getCrafting(){ return crafting; }
    public EnchantingManager getEnchanting() { return enchanting; }

    // ── Stubs promus — encore partiels ─────────────────────────────────────────
    public void renderFurnaceUI()   { /*TODO: gui fourneau*/ }
    public void renderCraftingUI()  { /*TODO: gui table de craft*/ }
    public void renderEnchantUI()   { /*TODO: gui enchancie*/ }
}