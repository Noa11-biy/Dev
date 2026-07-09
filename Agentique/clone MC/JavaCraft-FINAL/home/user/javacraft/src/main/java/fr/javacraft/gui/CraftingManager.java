package fr.javacraft.gui;

import fr.javacraft.item.*;

/**
 * Système de crafting avec recettes.
 * Supporte les recettes shaped (en forme) et shapeless (sans forme).
 * Intégré à l'inventaire.
 */
public class CraftingManager {

    /** Une recette de crafting */
    public static class Recipe {
        public final ItemStack[] grid;     // 9 slots (3×3)
        public final ItemStack output;
        public final boolean shapeless;

        public Recipe(ItemStack[] grid, ItemStack output, boolean shapeless) {
            this.grid = grid;
            this.output = output;
            this.shapeless = shapeless;
        }
    }

    private final java.util.List<Recipe> recipes = new java.util.ArrayList<>();

    public CraftingManager() {
        registerDefaultRecipes();
    }

    private void registerDefaultRecipes() {
        // Plan de bois (4 bois → 4 planks) — shapeless
        Item planks = Item.ofBlock("Planks", 9);
        ItemStack ws = new ItemStack(Item.ofBlock("Wood", 6), 1);
        ItemStack[] r = new ItemStack[9];
        r[4] = ws;
        recipes.add(new Recipe(r, new ItemStack(planks, 4), true));

        // Bâton (2 planks en colonne → 4 sticks) — shapeless
        Item stick = Item.simple("Stick", Item.ItemType.MISC, 64);
        ItemStack ps = new ItemStack(planks, 1);
        r = new ItemStack[9];
        r[1] = ps; r[4] = ps;
        recipes.add(new Recipe(r, new ItemStack(stick, 4), true));

        // Table de craft basique
        Item craftTable = Item.ofBlock("CraftTable", 10);
        r = new ItemStack[9];
        r[0] = ps; r[1] = ps;
        r[3] = ps; r[4] = ps;
        recipes.add(new Recipe(r, new ItemStack(craftTable, 1), false));

        // Four (8 cobblestones en anneau)
        Item furnace = Item.ofBlock("Furnace", 11);
        Item cobble = Item.ofBlock("Stone", 1);
        r = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            if (i != 4) r[i] = new ItemStack(cobble, 1);
        }
        recipes.add(new Recipe(r, new ItemStack(furnace, 1), false));

        // Pioche (shaped)
        Item pick = Item.simple("Pickaxe", Item.ItemType.TOOL, 1);
        r = new ItemStack[9];
        r[0] = new ItemStack(cobble, 1); r[1] = new ItemStack(cobble, 1); r[2] = new ItemStack(cobble, 1);
        r[3] = new ItemStack(stick, 1);
        r[4] = new ItemStack(stick, 1);
        recipes.add(new Recipe(r, new ItemStack(pick, 1), false));

        // Epée (shaped)
        Item sword = Item.simple("Sword", Item.ItemType.WEAPON, 1);
        r = new ItemStack[9];
        r[1] = new ItemStack(cobble, 1);
        r[4] = new ItemStack(stick, 1);
        recipes.add(new Recipe(r, new ItemStack(sword, 1), false));
    }

    /**
     * Tente de trouver une recette correspondante.
     * 
     * @param grid  les 9 slots de la grille de craft
     * @return l'ItemStack résultat, ou null si aucune recette ne correspond
     */
    public ItemStack findRecipe(ItemStack[] grid) {
        for (Recipe recipe : recipes) {
            if (matches(recipe, grid)) return recipe.output;
        }
        return null;
    }

    private boolean matches(Recipe recipe, ItemStack[] grid) {
        if (recipe.shapeless) {
            return matchesShapeless(recipe, grid);
        }
        return matchesShaped(recipe, grid);
    }

    private boolean matchesShapeless(Recipe recipe, ItemStack[] grid) {
        // Compte les items de la recette
        java.util.Map<Item, Integer> need = new java.util.HashMap<>();
        java.util.Map<Item, Integer> have = new java.util.HashMap<>();
        for (ItemStack s : recipe.grid) {
            if (s != null && !s.isEmpty()) {
                need.merge(s.getItem(), s.getCount(), Integer::sum);
            }
        }
        for (ItemStack s : grid) {
            if (s != null && !s.isEmpty()) {
                have.merge(s.getItem(), s.getCount(), Integer::sum);
            }
        }
        return need.equals(have);
    }

    private boolean matchesShaped(Recipe recipe, ItemStack[] grid) {
        // Cherche le rectangle minimal
        int minR = 3, maxR = -1, minC = 3, maxC = -1;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                ItemStack s = recipe.grid[r * 3 + c];
                if (s != null && !s.isEmpty()) {
                    minR = Math.min(minR, r); maxR = Math.max(maxR, r);
                    minC = Math.min(minC, c); maxC = Math.max(maxC, c);
                }
            }
        }
        if (maxR < 0) return false;
        int height = maxR - minR + 1;
        int width  = maxC - minC + 1;

        // Cherche la même forme dans la grille
        outer:
        for (int offR = 0; offR <= 3 - height; offR++) {
            for (int offC = 0; offC <= 3 - width; offC++) {
                boolean ok = true;
                for (int r = 0; r < height && ok; r++) {
                    for (int c = 0; c < width && ok; c++) {
                        ItemStack need = recipe.grid[(minR + r) * 3 + (minC + c)];
                        ItemStack have = grid[(offR + r) * 3 + (offC + c)];
                        if (need == null || need.isEmpty()) {
                            if (have != null && !have.isEmpty()) ok = false;
                        } else {
                            if (have == null || have.isEmpty()) ok = false;
                            else if (have.getItem() != need.getItem()) ok = false;
                        }
                    }
                }
                if (ok) return true;
            }
        }
        return false;
    }

    public java.util.List<Recipe> getAllRecipes() { return recipes; }
}