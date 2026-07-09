public class Cercle extends F2D{
    private double rayon;

    public Cercle(String nom, double rayon){
        super(nom);
        this.rayon = rayon;
    }

    @Override
    boolean estSymetrique() {
        return true;
    }

    @Override
    public double perimetre() {
        return 2 * Math.PI * rayon;
    }

    @Override
    public double calculeSurface() {
        return Math.PI*rayon*rayon;
    }
}