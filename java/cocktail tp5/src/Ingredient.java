import java.util.HashMap;

public abstract class Ingredient {
    private String nom;
    private String couleur;
    private int degree;
    private Unite unite;
    private HashMap<String, Unite> map = new HashMap<>();

    public Ingredient(String nom, String couleur,Unite unite) {
        this.nom = nom;
        this.couleur = couleur;
        this.unite = unite;
    }

    public abstract double prixUnitaire();
}
