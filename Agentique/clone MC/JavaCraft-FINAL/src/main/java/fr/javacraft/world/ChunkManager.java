package fr.javacraft.world;

import java.util.*;
import java.util.concurrent.*;
import org.joml.Vector3f;

/**
 * Gestionnaire de chunks.
 * - Generation et meshing sur threads secondaires (CPU only)
 * - Upload GPU sur thread principal via processPendingUploads()
 * - Dechargement GPU sur thread principal
 */
public class ChunkManager {

    private static final ChunkManager INSTANCE = new ChunkManager();
    public static ChunkManager getInstance() { return INSTANCE; }

    private final Map<String, ChunkData> chunks = new ConcurrentHashMap<>();
    private final Map<String, ChunkMesh> meshes = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> pendingGen = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ChunkMesh> pendingUpload = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ChunkMesh> pendingDelete = new ConcurrentLinkedQueue<>();
    private final ExecutorService threadPool;
    private final TerrainGenerator terrainGenerator;
    private int renderDistance = 6;
    private volatile int lastPlayerCX = Integer.MIN_VALUE;
    private volatile int lastPlayerCZ = Integer.MIN_VALUE;
    private final long worldSeed;

    private ChunkManager() {
        this.worldSeed = System.currentTimeMillis();
        this.terrainGenerator = new TerrainGenerator(worldSeed);
        int threads = Math.max(2, Runtime.getRuntime().availableProcessors() - 1);
        this.threadPool = Executors.newFixedThreadPool(threads, r -> {
            Thread t = new Thread(r, "ChunkGen");
            t.setDaemon(true);
            return t;
        });
        System.out.println("[ChunkManager] initialise. Graine monde = " + worldSeed);
    }

    /**
     * APPELERdepuis le thread principal (boucle de rendu).
     * Traite les uploads GPU en attente.
     */
    public void processPendingUploads() {
        ChunkMesh mesh;
        // Upload les nouveaux meshes
        while ((mesh = pendingUpload.poll()) != null) {
            mesh.uploadToGPU();
        }
        // Supprime les meshes decharges
        while ((mesh = pendingDelete.poll()) != null) {
            mesh.delete();
        }
    }

    public void update(float playerX, float playerZ) {
        int cx = worldToChunk(playerX);
        int cz = worldToChunk(playerZ);
        if (cx != lastPlayerCX || cz != lastPlayerCZ) {
            lastPlayerCX = cx;
            lastPlayerCZ = cz;
            updateChunks(cx, cz);
        }
    }

    public static int worldToChunk(float worldCoord) {
        return (int)Math.floor(worldCoord / 16.0f);
    }

    private void updateChunks(int playerCX, int playerCZ) {
        Set<String> targetChunks = new HashSet<>();
        for (int dx = -renderDistance; dx <= renderDistance; dx++)
            for (int dz = -renderDistance; dz <= renderDistance; dz++)
                targetChunks.add(key(playerCX + dx, playerCZ + dz));

        List<String> toRemove = new ArrayList<>();
        for (String k : chunks.keySet())
            if (!targetChunks.contains(k)) toRemove.add(k);
        for (String k : toRemove) unloadChunk(k);

        for (String ck : targetChunks) {
            if (!chunks.containsKey(ck)) {
                String[] p = ck.split(",");
                queueChunk(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
            }
        }
    }

    private void queueChunk(int cx, int cz) {
        if (pendingGen.contains(key(cx, cz))) return;
        pendingGen.offer(key(cx, cz));
        threadPool.submit(() -> generateAndMesh(cx, cz));
    }

    /**
     * Thread secondaire : generation CPU + meshing CPU uniquement.
     * AUCUN appel GL/OpenGL ici.
     */
    private void generateAndMesh(int cx, int cz) {
        try {
            ChunkData data = new ChunkData(cx, cz);
            terrainGenerator.generate(data);
            chunks.put(key(cx, cz), data);

            ChunkMesh mesh = new ChunkMesh(data);
            mesh.build();

            pendingUpload.offer(mesh);
            meshes.put(key(cx, cz), mesh);
            System.out.println("[ChunkManager] Chunk pret : (" + cx + "," + cz + ")");
        } catch (Exception e) {
            System.err.println("[ChunkManager] Erreur chunk (" + cx + "," + cz + ") : " + e);
        }
    }

    private void unloadChunk(String key) {
        ChunkMesh mesh = meshes.remove(key);
        chunks.remove(key);
        if (mesh != null) pendingDelete.offer(mesh);
        System.out.println("[ChunkManager] Chunk decharge : " + key);
    }

    private static String key(int cx, int cz) { return cx + "," + cz; }

    public ChunkData getChunk(int cx, int cz) { return chunks.get(key(cx, cz)); }
    public ChunkMesh getMesh(int cx, int cz) { return meshes.get(key(cx, cz)); }
    public Collection<ChunkMesh> getAllMeshes() { return meshes.values(); }

    public void shutdown() {
        threadPool.shutdown();
        try { threadPool.awaitTermination(5, TimeUnit.SECONDS); }
        catch (InterruptedException e) { threadPool.shutdownNow(); }
    }

    public int getLoadedChunkCount() { return chunks.size(); }

    public int getBlockAt(int wx, int wy, int wz) {
        int cx = worldToChunk(wx);
        int cz = worldToChunk(wz);
        ChunkData cd = chunks.get(key(cx, cz));
        if (cd == null) return 0;
        return cd.getBlockAt(wx, wy, wz);
    }

    public Map<String, ChunkData> getAllChunks() { return chunks; }
}
