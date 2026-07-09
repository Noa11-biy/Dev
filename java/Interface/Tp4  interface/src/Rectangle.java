public class Rectangle extends F2D{

    private double longueur;
    private double largeur;

    public Rectangle(String nom, double longueur, double largeur) {
        super(nom);
        this.longueur = longueur;
        this.largeur = largeur;
    }


    @Override
    boolean estSymetrique() {
        return true;
    }

    @Override
    public double perimetre() {
        return 2 * (longueur + largeur);
    }

    @Override
    public double calculeSurface() {
        return longueur*largeur;
    }
}