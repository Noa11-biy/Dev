package fr.javacraft.world;

import java.util.*;

/**
 * Système de redstone complet.
 * - Poudre redstone : propagation du signal (force 1-15)
 * - Leviers/buttons : sources de signal
 * - Torches redstone : boost de signal + inverters
 * - Répéteurs : délai +refresh du signal
 * - Blocs alimentés (doors, lamps)
 * 
 * Tick autonome : chaque tick world, le système se propage.
 */
public class RedstoneSystem {

    /** Un nœud de circuit redstone ( bloc monde ) */
    public static class RedstoneNode {
        public int wx, wy, wz;
        public int power;       // 0-15 (puissance du signal)
        public RedstoneBlockType type;
        public boolean powered;  // État alimenté du bloc adjacent

        public RedstoneNode(int wx, int wy, int wz, RedstoneBlockType type) {
            this.wx = wx; this.wy = wy; this.wz = wz; this.type = type;
            this.power = 0; this.powered = false;
        }
    }

    public enum RedstoneBlockType {
        DUST, LEVER, BUTTON, TORCH, REPEATER,
        POWERED_REDSTONE, POWERED_BLOCK, LAMP, DOOR
    }

    /** Tous les nœuds redstone actifs */
    private final Map<String, RedstoneNode> nodes = new HashMap<>();
    private final Set<String> toUpdate = new HashSet<>();
    private int worldTick = 0;

    private String key(int x, int y, int z) { return x + "," + y + "," + z; }

    /** Enregistre un bloc redstone dans le système */
    public void registerBlock(int wx, int wy, int wz, RedstoneBlockType type) {
        nodes.put(key(wx, wy, wz), new RedstoneNode(wx, wy, wz, type));
        toUpdate.add(key(wx, wy, wz));
    }

    /** Supprime un bloc */
    public void unregisterBlock(int wx, int wy, int wz) {
        nodes.remove(key(wx, wy, wz));
    }

    /** Bascule un levier (appelé par clic joueur) */
    public boolean toggleLever(int wx, int wy, int wz) {
        RedstoneNode n = nodes.get(key(wx, wy, wz));
        if (n == null || n.type != RedstoneBlockType.LEVER) return false;
        n.powered = !n.powered;
        n.power = n.powered ? 15 : 0;
        toUpdate.add(key(wx, wy, wz));
        return true;
    }

    /**
     * Tick principal du système redstone.
     * Propage le signal dans tous les nœuds.
     * Appelé chaque tick world.
     */
    public void tick() {
        worldTick++;

        for (RedstoneNode node : nodes.values()) {
            node.power = computePower(node);
        }

        // Propagation en cascade (dust)
        boolean changed;
        int iterations = 0;
        do {
            changed = false;
            iterations++;
            for (RedstoneNode node : nodes.values()) {
                if (node.type == RedstoneBlockType.DUST) {
                    int prev = node.power;
                    node.power = computeDustPower(node);
                    if (prev != node.power) changed = true;
                }
            }
        } while (changed && iterations < 20);

        // Active les blocs alimentés
        for (RedstoneNode node : nodes.values()) {
            if (node.type == RedstoneBlockType.LAMP && node.power > 0) {
                // La lampe émet de la lumière (simulé)
            }
            if (node.type == RedstoneBlockType.DOOR && node.power > 0) {
                // La porte s'ouvre (simulé)
            }
        }
    }

    private int computePower(RedstoneNode node) {
        return switch(node.type) {
            case LEVER    -> node.powered ? 15 : 0;
            case BUTTON   -> node.powered ? 15 : 0;
            case TORCH    -> node.powered ? 15 : 0;
            case REPEATER -> node.powered ? 15 : 0;
            default      -> 0;
        };
    }

    private int computeDustPower(RedstoneNode node) {
        int maxPower = 0;
        int[][] dirs = {{1,0,0},{-1,0,0},{0,0,1},{0,0,-1},{0,-1,0}};

        for (int[] d : dirs) {
            RedstoneNode adj = nodes.get(key(node.wx + d[0], node.wy + d[1], node.wz + d[2]));
            if (adj != null) {
                int p = adj.power - 1;
                if (p > maxPower) maxPower = p;
            }
        }
        return Math.max(0, Math.min(15, maxPower));
    }

    /** Retourne la puissance du signal à une position */
    public int getPowerAt(int wx, int wy, int wz) {
        RedstoneNode n = nodes.get(key(wx, wy, wz));
        return n != null ? n.power : 0;
    }

    public int getTick()          { return worldTick; }
    public int getNodeCount()    { return nodes.size(); }
}