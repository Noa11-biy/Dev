package fr.javacraft.world;

/**
 * Implementation de bruit coherant "Simplex Noise" 2D.
 * Inspiré de l'algorithme de Stefan Gustavson (2005).
 * Genere un champ de valeurs continues dans [-1, 1] a partir de (x, y).
 * 
 * Utilise pour la generation procedurale du terrain (hauteur des chunks).
 */
public class OpenSimplexNoise {

    /** Graine aleatoire pour la reproductibilite */
    private final long seed;

    /** Permutations de 256 valeurs (melangees selon la graine) */
    private final int[] perm;

    /** Vecteurs de gradient 2D (gradients de la grille) */
    private static final int[][] GRAD2 = {
        { 1, 1}, {-1, 1}, { 1,-1}, {-1,-1}
    };

    /**
     * Construit le bruit avec une graine donnee.
     * 
     * @param seed  graine entiere (deux executions avec la meme graine
     *              donnent exactement le meme terrain)
     */
    public OpenSimplexNoise(long seed) {
        this.seed = seed;
        this.perm = buildPermutationTable(seed);
    }

    /** Construit la table de permutation melangee a partir de la graine. */
    private int[] buildPermutationTable(long seed) {
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) p[i] = i;

        // Melange de Fisher-Yates avec un generateur congruentiel
        long s = seed;
        for (int i = 255; i > 0; i--) {
            s = (s * 6364136223846793005L + 1442695040888963407L);
            int j = (int)(s >>> 33) % (i + 1);
            int tmp = p[i]; p[i] = p[j]; p[j] = tmp;
        }

        // Doubler pour eviter les depassements de modulo
        int[] doubled = new int[512];
        for (int i = 0; i < 256; i++) doubled[i] = doubled[i + 256] = p[i];
        return doubled;
    }

    /** Retourne la valeur de bruit coherent en (x, y). */
    public double noise2D(double x, double y) {
        // Grille simplex 2D (triangulaire)
        final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
        final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;

        // Cellule actuelle sur la grille simplex
        double s = (x + y) * F2;
        int ix = (int)Math.floor(x + s);
        int iy = (int)Math.floor(y + s);

        double t = (ix + iy) * G2;
        double X0 = ix - t;
        double Y0 = iy - t;
        double x0 = x - X0;
        double y0 = y - Y0;

        // Determine quel triangle de la cellule on se trouve
        int i1 = (x0 > y0) ? 1 : 0;
        int j1 = (x0 > y0) ? 0 : 1;

        double x1 = x0 - i1 + G2;
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2;
        double y2 = y0 - 1.0 + 2.0 * G2;

        // Contributions de chaque coin
        double n0, n1, n2;

        double t0 = 0.5 - x0*x0 - y0*y0;
        if (t0 < 0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            int gi0 = perm[ix + perm[iy & 0xFF]] & 0x03;
            n0 = t0 * t0 * (GRAD2[gi0][0] * x0 + GRAD2[gi0][1] * y0);
        }

        double t1 = 0.5 - x1*x1 - y1*y1;
        if (t1 < 0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            int gi1 = perm[ix + i1 + perm[iy + j1 & 0xFF]] & 0x03;
            n1 = t1 * t1 * (GRAD2[gi1][0] * x1 + GRAD2[gi1][1] * y1);
        }

        double t2 = 0.5 - x2*x2 - y2*y2;
        if (t2 < 0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            int gi2 = perm[ix + 1 + perm[iy + 1 & 0xFF]] & 0x03;
            n2 = t2 * t2 * (GRAD2[gi2][0] * x2 + GRAD2[gi2][1] * y2);
        }

        // Mise a l'echelle : resultat dans [-1, 1]
        return 70.0 * (n0 + n1 + n2);
    }

    /**
     * Bruit multi-octaves (fractal Brownian motion).
     * Superpose plusieurs frequences pour un terrain plus naturel.
     *
     * @param x        coordonnee X
     * @param y        coordonnee Y
     * @param octaves  nombre d'octaves (plus = plus de detail)
     * @param lacunarity  facteur de frequence entre octaves (typiquement 2.0)
     * @param persistence  facteur d'amplitude entre octaves (typiquement 0.5)
     * @return valeur de bruit dans [-1, 1]
     */
    public double fbm(double x, double y, int octaves, double lacunarity, double persistence) {
        double value = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        double maxValue = 0.0;

        for (int i = 0; i < octaves; i++) {
            value += amplitude * noise2D(x * frequency, y * frequency);
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= lacunarity;
        }

        return value / maxValue;
    }
}
