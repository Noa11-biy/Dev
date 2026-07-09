/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package packTP3;

/**
 *
 * @author Brice Effantin
 */
public class Pizza {
    /**
     * Attributs
     */
    private String nom; //nom de la pizza
    private String ingredients; //liste des ingrédients (pour simplifier on utilisera une simple String avec la liste des ingrédients)
    private double tarif; //tarif de la pizza

    /**
     * Constructeur
     * @param nom
     * @param ingredients
     * @param tarif 
     */
    public Pizza(String nom, String ingredients, double tarif) {
        this.nom = nom;
        this.ingredients = ingredients;
        this.tarif = tarif;
    }

    /**
     * Mutateur du nom
     * @param nom 
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Mutateur du tarif
     * @param tarif 
     */
    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    /**
     * Mutateur des ingrédients
     * @param ingredients 
     */
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Accesseur au nom
     * @return 
     */
    public String getNom() {
        return nom;
    }

    /**
     * Accesseur au tarif
     * @return 
     */
    public double getTarif() {
        return tarif;
    }

    /**
     * Accesseur aux ingrédients
     * @return 
     */
    public String getIngredients() {
        return ingredients;
    }

}
