import java.util.HashMap;

public class Cocktail {
    private String nom;
    private boolean alcoolise = false;
    private HashMap<Ingredient, Integer> recette = new HashMap<>();

    public Cocktail(String nom, HashMap<Ingredient, Integer> recette){
        this.nom = nom;
        this.recette = recette;
        for (Ingredient ingr : recette.keySet()){
            if(ingr instanceof Alcool) {
                alcoolise = true;
            }
        }
    }

    @Override
    public String toString() {
        return this.nom + " " + this.recette.keySet();
    }

    public double getPrixRevient(){
        return 0.0 ;
    }

    public String getNom(){
        return nom;
    }

    public double getPrixVente(){
        return 0.0;
    }
}
