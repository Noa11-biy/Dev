public class Parallelepipede extends F3D{

    private double largeur;
    private double longueur;
    private double hauteur;

    public Parallelepipede(String nom, double largeur, double longueur,double hauteur){
        super(nom);
        this.largeur = largeur;
        this.longueur = longueur;
        this.hauteur =hauteur;
    }

    @Override
    boolean estSymetrique() {
        return true;
    }

    @Override
    public double volume() {
        return largeur * longueur * hauteur;
    }

    @Override
    public double calculeSurface() {
        return 2*(largeur*longueur + largeur*hauteur + longueur*hauteur );
    }
}