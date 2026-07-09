package fr.javacraft.item;

/**
 * Une pile d'items dans un slot d'inventaire.
 * Combinaison d'un Item + quantité + données d'usure/enchantement.
 */
public class ItemStack {

    private final Item item;
    private int count;
    private int damage;       // Pour outils/armes/armures
    private int enchantments; // Flags d'enchantements (bitwise)

    public static final int ENCHANT_SHARPNESS    = 1 << 0;
    public static final int ENCHANT_PROTECTION   = 1 << 1;
    public static final int ENCHANT_EFFICIENCY   = 1 << 2;
    public static final int ENCHANT_UNBREAKING   = 1 << 3;
    public static final int ENCHANT_POWER       = 1 << 4;
    public static final int ENCHANT_THORNS      = 1 << 5;

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = Math.max(0, Math.min(count, item.maxStackSize));
        this.damage = 0;
        this.enchantments = 0;
    }

    public ItemStack(Item item) { this(item, 1); }

    public Item getItem()    { return item; }
    public int  getCount()   { return count; }
    public int  getDamage()  { return damage; }
    public int  getEnchants(){ return enchantments; }

    public void setCount(int c) {
        this.count = Math.max(0, Math.min(c, item.maxStackSize));
    }

    public void addEnchant(int mask) { this.enchantments |= mask; }

    public boolean hasEnchant(int mask) {
        return (enchantments & mask) != 0;
    }

    /** Retourne le bonus de dégâts d'une arme (Sharpness) */
    public float getSharpnessBonus() {
        return hasEnchant(ENCHANT_SHARPNESS) ? 1.5f : 0.0f;
    }

    /** Retourne le facteur de protection (Protection) */
    public float getProtectionFactor() {
        return hasEnchant(ENCHANT_PROTECTION) ? 0.8f : 1.0f;
    }

    /** Retourne la vitesse de minage bonus (Efficiency) */
    public float getEfficiencyBonus() {
        return hasEnchant(ENCHANT_EFFICIENCY) ? 1.5f : 1.0f;
    }

    public boolean isEmpty() { return count <= 0 || item == null; }

    /** Diminue la pile de 1 (retourne true si la pile est devenue vide) */
    public boolean consumeOne() {
        if (isEmpty()) return true;
        count--;
        return count <= 0;
    }

    /** Clone la pile */
    public ItemStack copy() {
        ItemStack copy = new ItemStack(item, count);
        copy.damage = this.damage;
        copy.enchantments = this.enchantments;
        return copy;
    }

    @Override public String toString() {
        return item.name + " x" + count
            + (enchantments != 0 ? " *ench*" : "");
    }
}