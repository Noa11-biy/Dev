package fr.javacraft.item;

/**
 * Un item dans le jeu.
 * Peut être un bloc (placeable), un outil, une arme, une armure, un aliment,
 * une potion, un livre d'enchantement, etc.
 */
public class Item {

    public static final int MAX_STACK_SIZE = 64;

    /** Nom technique de l'item */
    public final String name;
    /** Taille max de la pile (1 pour outils/armes/armures) */
    public final int maxStackSize;
    /** ID du bloc associé (si l'item est placeable), sinon -1 */
    public final int blockId;
    /** Type d'item pour le traitement */
    public final ItemType type;

    public enum ItemType {
        BLOCK, TOOL, WEAPON, ARMOR, FOOD, POTION, ENCHANT_BOOK, MISC
    }

    private Item(String name, int maxStack, int blockId, ItemType type) {
        this.name = name;
        this.maxStackSize = maxStack;
        this.blockId = blockId;
        this.type = type;
    }

    /** Item placeable (équivalent à un bloc) */
    public static Item ofBlock(String name, int blockId) {
        return new Item(name, 64, blockId, ItemType.BLOCK);
    }

    /** Item simple non-placeable */
    public static Item simple(String name, ItemType type, int stackSize) {
        return new Item(name, stackSize, -1, type);
    }

    public boolean isPlaceable() { return blockId >= 0; }
    public boolean isStackable() { return maxStackSize > 1; }
}