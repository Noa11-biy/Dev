package fr.javacraft.gui;

import fr.javacraft.item.*;

/**
 * Système d'enchantement et de potions.
 * - Table d'enchantement : sélection d'un item + paiement XP → enchantement appliqué
 * - Brewing stand : ingrédients + eau → potions avec effets temporaires
 */
public class EnchantingManager {

    public enum Enchantment {
        SHARPNESS(    "Tranchant",     10, 1 << 0, 1.5f),
        PROTECTION(   "Protection",    10, 1 << 1, 0.8f),
        EFFICIENCY(   "Efficacite",    10, 1 << 2, 1.5f),
        UNBREAKING(   "Durabilite",    10, 1 << 3, 1.0f),
        POWER(        "Puissance",     10, 1 << 4, 1.25f),
        THORNS(       "Epines",        10, 1 << 5, 0.0f);

        public final String name;
        public final int    maxLevel;
        public final int    flag;
        public final float  effectValue;

        Enchantment(String name, int max, int flag, float effect) {
            this.name = name; this.maxLevel = max; this.flag = flag; this.effectValue = effect;
        }
    }

    public enum PotionType {
        SWIFTNESS(   "Rapidite",    300, 0x01),
        STRENGTH(    "Force",       300, 0x02),
        HEALING(     "Soin",        300, 0x04),
        REGENERATION("Regeneration",300, 0x08);

        public final String name;
        public final int    duration;
        public final int    flag;

        PotionType(String n, int d, int f) { name = n; duration = d; flag = f; }
    }

    public static class ActivePotion {
        public final PotionType type;
        public int ticksRemaining;
        public int amplifier;

        public ActivePotion(PotionType t, int dur, int amp) {
            this.type = t; this.ticksRemaining = dur; this.amplifier = amp;
        }
    }

    private final java.util.List<ActivePotion> activePotions = new java.util.ArrayList<>();

    /** Applique un enchantement à une ItemStack (retourne true si possible) */
    public boolean applyEnchant(ItemStack stack, Enchantment ench, int level) {
        if (stack == null || stack.isEmpty()) return false;
        if (ench == null || level <= 0 || level > ench.maxLevel) return false;
        stack.addEnchant(ench.flag);
        System.out.println("[Enchanting] " + ench.name + " " + toRoman(level) + " sur " + stack.getItem().name);
        return true;
    }

    /** Ajoute une potion active */
    public void brewPotion(PotionType type) {
        activePotions.add(new ActivePotion(type, type.duration * 60, 0));
        System.out.println("[Potion] Brouillee : " + type.name);
    }

    /** Tick des potions (appelé chaque frame) */
    public void tick() {
        for (int i = activePotions.size() - 1; i >= 0; i--) {
            activePotions.get(i).ticksRemaining--;
            if (activePotions.get(i).ticksRemaining <= 0) {
                activePotions.remove(i);
            }
        }
    }

    public boolean hasEffect(PotionType type) {
        return activePotions.stream().anyMatch(p -> p.type == type);
    }

    public java.util.List<ActivePotion> getActivePotions() { return activePotions; }

    private static String toRoman(int n) {
        return switch(n) { case 1 -> "I"; case 2 -> "II"; case 3 -> "III"; default -> "I"; };
    }
}