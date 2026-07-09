public class Alcool extends IngredientLiquide {
    private String nom;
    private int degree;

    public Alcool(String nom, String couleur, int degree) {
        super(nom, couleur);
        this.degree = degree;
    }

    @Override
    public String toString() {

    }
}
