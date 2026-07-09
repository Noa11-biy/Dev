package fr.javacraft.world;

/**
 * Enum des types de blocs disponibles dans JavaCraft.
 * Chaque bloc a un identifiant unique (ordinal) et des propriétés visuelles
 * (utilisé pour le texture atlas).
 */
public enum BlockType {
    AIR(    0, false, false, false, -1),
    STONE(  1, true,  true,  false,  1),
    DIRT(   2, true,  true,  false,  0),
    GRASS(  3, true,  true,  false,  2),
    SAND(   4, true,  true,  false,  4),
    WATER(  5, true,  false, true,   3),
    WOOD(   6, true,  true,  false,  5),
    LEAVES( 7, true,  false, true,   6),
    BEDROCK(8, true,  true,  false,  7);

    /** Identifiant numérique unique du bloc (0-255 stockable dans un byte) */
    public final int id;
    /** Vrai si le bloc occupe un volume (sinon, transparent/AIR) */
    public final boolean solid;
    /** Vrai si la texture est opaque (affecte le rendu des faces adjacentes) */
    public final boolean opaque;
    /** Vrai si le bloc est semi-transparent (eau) — ne bloque pas le rendu des blocs derrière */
    public final boolean transparent;
    /** Index de la tuile dans le texture atlas (ligne/colonne de 0 à TILES-1) */
    public final int tileIndex;

    BlockType(int id, boolean solid, boolean opaque, boolean transparent, int tileIndex) {
        this.id = id;
        this.solid = solid;
        this.opaque = opaque;
        this.transparent = transparent;
        this.tileIndex = tileIndex;
    }

    /** Retourne le BlockType correspondant à un ID, ou AIR si hors-bornes. */
    public static BlockType byId(int id) {
        if (id < 0 || id >= values().length) return AIR;
        return values()[id];
    }
}
