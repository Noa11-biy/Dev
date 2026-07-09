public class IngredientSolide extends Ingredient{
    private Unite unite;

    public IngredientSolide(String nom, String couleur, Unite unite) {
        super(nom, couleur, unite);
    }


    @Override
    public double prixUnitaire() {
        return 0;
    }

}
