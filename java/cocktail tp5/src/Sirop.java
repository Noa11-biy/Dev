public class Sirop extends IngredientLiquide {
    private double taux;
    public Sirop(String nom, String couleur, double taux) {
        super(nom, couleur);
        this.taux = taux;
    }
}
