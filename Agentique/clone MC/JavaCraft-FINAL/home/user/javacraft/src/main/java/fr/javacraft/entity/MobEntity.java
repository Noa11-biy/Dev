package fr.javacraft.entity;

import org.joml.Vector3f;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Mob avec IA de navigation A* sur la grille voxel.
 * États : WANDER → CHASE → ATTACK → FLEE
 * Les mobs passifs ne chase/attack que si provoqués.
 */
public class MobEntity extends Entity {

    public enum MobState { WANDER, CHASE, ATTACK, FLEE, IDLE }
    public enum MobType  { PASSIVE, HOSTILE }

    private final MobType mobType;
    private MobState state = MobState.IDLE;
    private int tickCounter = 0;

    // A* pathfinding
    private final List<int[]> currentPath = new ArrayList<>();
    private int pathIndex = 0;

    // Suivi de destination
    private Vector3f targetPos = new Vector3f();
    private float moveSpeed = 2.5f;
    private float followRange = 16.0f;
    private float attackRange = 1.5f;
    private float attackCooldown = 0f;

    // Wander aléatoire
    private int wanderTimer = 0;
    private int wanderInterval = 120; // 2s à 60fps

    public MobEntity(EntityType type, Vector3f spawn, MobType mobType) {
        super(type, spawn);
        this.mobType = mobType;
        this.moveSpeed = (mobType == MobType.HOSTILE) ? 3.0f : 2.0f;
    }

    @Override
    public void tick(float dt) {
        tickCounter++;
        attackCooldown = Math.max(0, attackCooldown - dt);

        switch (state) {
            case WANDER  -> tickWander(dt);
            case CHASE   -> tickChase(dt);
            case ATTACK  -> tickAttack(dt);
            case FLEE    -> tickFlee(dt);
            case IDLE    -> tickIdle(dt);
        }

        super.tick(dt);
    }

    private void tickIdle(float dt) {
        wanderTimer++;
        if (wanderTimer > wanderInterval) {
            state = MobState.WANDER;
            wanderTimer = 0;
        }
    }

    private void tickWander(float dt) {
        if (currentPath.isEmpty() || tickCounter % 60 == 0) {
            pickRandomWanderTarget();
        }
        followPath(dt);
    }

    private void tickChase(float dt) {
        if (targetPos == null || !isInRange(targetPos, followRange)) {
            state = MobState.WANDER;
            return;
        }
        navigateTo(targetPos);
        followPath(dt);
    }

    private void tickAttack(float dt) {
        if (targetPos == null || !isInRange(targetPos, attackRange)) {
            state = MobState.CHASE;
            return;
        }
        // Frappe le joueur si cooldown prêt
        if (attackCooldown <= 0) {
            attackCooldown = 1.0f;
            // Le joueur prend des dégâts via World/Game
            System.out.println("[" + type + "] Frappe !");
        }
    }

    private void tickFlee(float dt) {
        if (targetPos == null) {
            state = MobState.WANDER;
            return;
        }
        navigateTo(targetPos);
        followPath(dt);
    }

    // ── A* Pathfinding ──────────────────────────────────────────────────────────

    /**
     * Calcule un chemin A* de la position actuelle à target.
     * Utilise la grille voxel 1×1×1 (chaque bloc = nœud).
     * Limité à 200 nœuds max pour les performances.
     */
    public boolean computePath(Vector3f target) {
        if (!alive) return false;

        int sx = (int)Math.floor(pos.x);
        int sy = (int)Math.floor(pos.y);
        int sz = (int)Math.floor(pos.z);
        int tx = (int)Math.floor(target.x);
        int ty = (int)Math.floor(target.y);
        int tz = (int)Math.floor(target.z);

        // Distance manhattan max
        int manhattan = Math.abs(tx - sx) + Math.abs(ty - sy) + Math.abs(tz - sz);
        if (manhattan > 60) return false;

        // A* avec heap (priority queue)
        PriorityQueue<int[]> open = new PriorityQueue<>(
            Comparator.comparingInt(a -> heuristic(a[0], a[1], a[2], tx, ty, tz)));
        Map<String, int[]> cameFrom = new java.util.HashMap<>();
        Map<String, Integer> gScore = new java.util.HashMap<>();
        Set<String> closed = new java.util.HashSet<>();

        String startKey = key(sx, sy, sz);
        open.add(new int[]{sx, sy, sz, 0});
        gScore.put(startKey, 0);

        int[][] dirs = {
            {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0},
            {0,0,1}, {0,0,-1},
            {1,1,0}, {-1,1,0}, {1,-1,0}, {-1,-1,0},
            {1,0,1}, {-1,0,1}, {1,0,-1}, {-1,0,-1},
            {0,1,1}, {0,1,-1}, {0,-1,1}, {0,-1,-1}
        };

        int iterations = 0;
        while (!open.isEmpty() && iterations < 400) {
            iterations++;
            int[] cur = open.poll();
            int cx = cur[0], cy = cur[1], cz = cur[2];
            String ck = key(cx, cy, cz);

            if (cx == tx && cy == ty && cz == tz) {
                // Chemin trouvé → reconstituer
                reconstructPath(cameFrom, closed, tx, ty, tz);
                return true;
            }

            if (closed.contains(ck)) continue;
            closed.add(ck);

            int cg = gScore.getOrDefault(ck, Integer.MAX_VALUE);

            for (int[] d : dirs) {
                int nx = cx + d[0], ny = cy + d[1], nz = cz + d[2];
                String nk = key(nx, ny, nz);
                if (closed.contains(nk)) continue;

                int stepCost = (d[1] != 0) ? 2 : 1;
                int ng = cg + stepCost;
                if (ng < gScore.getOrDefault(nk, Integer.MAX_VALUE)) {
                    gScore.put(nk, ng);
                    open.add(new int[]{nx, ny, nz, ng + heuristic(nx, ny, nz, tx, ty, tz)});
                    cameFrom.put(nk, cur);
                }
            }
        }
        return false;
    }

    private int heuristic(int x, int y, int z, int tx, int ty, int tz) {
        return Math.abs(x - tx) + Math.abs(y - ty) + Math.abs(z - tz);
    }

    private void reconstructPath(Map<String, int[]> cameFrom, Set<String> closed, int tx, int ty, int tz) {
        currentPath.clear();
        List<int[]> rev = new ArrayList<>();
        int cx = tx, cy = ty, cz = tz;
        while (true) {
            rev.add(new int[]{cx, cy, cz});
            String k = key(cx, cy, cz);
            int[] prev = cameFrom.get(k);
            if (prev == null) break;
            cx = prev[0]; cy = prev[1]; cz = prev[2];
        }
        for (int i = rev.size() - 1; i >= 0; i--) {
            currentPath.add(rev.get(i));
        }
        pathIndex = 0;
    }

    private void followPath(float dt) {
        if (currentPath.isEmpty() || pathIndex >= currentPath.size()) {
            state = MobState.IDLE;
            return;
        }
        int[] next = currentPath.get(pathIndex);
        float dx = next[0] + 0.5f - pos.x;
        float dy = next[1] + 0.5f - pos.y;
        float dz = next[2] + 0.5f - pos.z;
        float dist = (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
        if (dist < 0.5f) {
            pathIndex++;
            return;
        }
        vel.x = (dx / dist) * moveSpeed;
        vel.z = (dz / dist) * moveSpeed;
        vel.y = (dy / dist) * moveSpeed * 0.5f;
    }

    private void navigateTo(Vector3f target) {
        if (!currentPath.isEmpty() && pathIndex < currentPath.size()) return;
        computePath(target);
    }

    private void pickRandomWanderTarget() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int dx = rnd.nextInt(-8, 9);
        int dz = rnd.nextInt(-8, 9);
        int sx = (int)Math.floor(pos.x);
        int sz = (int)Math.floor(pos.z);
        currentPath.clear();
        currentPath.add(new int[]{sx + dx, (int)pos.y, sz + dz});
        pathIndex = 0;
    }

    private boolean isInRange(Vector3f other, float range) {
        float dx = other.x - pos.x, dz = other.z - pos.z;
        return (dx*dx + dz*dz) < (range * range);
    }

    private static String key(int x, int y, int z) { return x + "," + y + "," + z; }

    public void setState(MobState s)  { this.state = s; }
    public void setTarget(Vector3f t) { this.targetPos.set(t); }
    public MobState getState()        { return state; }
    public MobType getMobType()        { return mobType; }
}