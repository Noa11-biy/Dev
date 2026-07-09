public class IngredientLiquide extends Ingredient{


    public IngredientLiquide(String nom, String couleur) {
        super(nom, couleur, Unite.cL);
    }

    @Override
    public double prixUnitaire() {
        return 0;
    }
}
