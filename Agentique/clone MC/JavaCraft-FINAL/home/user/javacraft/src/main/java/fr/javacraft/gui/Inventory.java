package fr.javacraft.gui;

import fr.javacraft.item.*;

/**
 * Inventaire du joueur : 9 slots hotbar + 27 slots principaux.
 * Les slots peuvent contenir un ItemStack.
 */
public class Inventory {

    public static final int HOTBAR_SIZE   = 9;
    public static final int MAIN_SIZE     = 27;
    public static final int TOTAL_SIZE    = HOTBAR_SIZE + MAIN_SIZE;

    private final ItemStack[] slots = new ItemStack[TOTAL_SIZE];
    private int selectedSlot = 0;

    public Inventory() {
        // Starter inventory
        slots[0] = new ItemStack(Item.ofBlock("Stone",  1), 64);
        slots[1] = new ItemStack(Item.ofBlock("Dirt",   2), 64);
        slots[2] = new ItemStack(Item.ofBlock("Grass",  3), 64);
        slots[3] = new ItemStack(Item.ofBlock("Sand",   4), 64);
        slots[4] = new ItemStack(Item.simple("Stick",  Item.ItemType.MISC, 64), 64);
        slots[5] = new ItemStack(Item.simple("Diamond", Item.ItemType.MISC, 32), 32);
    }

    public ItemStack getSlot(int i) {
        if (i < 0 || i >= TOTAL_SIZE) return null;
        return slots[i];
    }

    public void setSlot(int i, ItemStack stack) {
        if (i < 0 || i >= TOTAL_SIZE) return;
        slots[i] = stack;
    }

    /** Slot actuellement sélectionné (hotbar) */
    public int getSelectedSlot()  { return selectedSlot; }
    public void setSelectedSlot(int s) { this.selectedSlot = Math.max(0, Math.min(HOTBAR_SIZE - 1, s)); }
    public ItemStack getSelectedItem() { return slots[selectedSlot]; }

    /** Ajoute un item à l'inventaire (retourne ce qui reste) */
    public int addItem(Item item, int count) {
        // Remplis les piles existantes d'abord
        for (int i = 0; i < TOTAL_SIZE; i++) {
            if (slots[i] != null && slots[i].getItem() == item) {
                int space = item.maxStackSize - slots[i].getCount();
                if (space > 0) {
                    int add = Math.min(space, count);
                    slots[i].setCount(slots[i].getCount() + add);
                    count -= add;
                    if (count == 0) return 0;
                }
            }
        }
        // Nouvelles piles
        for (int i = 0; i < TOTAL_SIZE; i++) {
            if (slots[i] == null || slots[i].isEmpty()) {
                int put = Math.min(item.maxStackSize, count);
                slots[i] = new ItemStack(item, put);
                count -= put;
                if (count == 0) return 0;
            }
        }
        return count;
    }

    /** Retire un item de l'inventaire (depuis selected slot) */
    public boolean consumeSelected(int count) {
        ItemStack sel = slots[selectedSlot];
        if (sel == null || sel.isEmpty()) return false;
        sel.setCount(sel.getCount() - count);
        if (sel.getCount() <= 0) slots[selectedSlot] = null;
        return true;
    }
}