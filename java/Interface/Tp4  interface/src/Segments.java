public class Segments extends Figure{
    private double longueur;

    public Segments(String nom, double longueur) {
        super(nom);
        this.longueur = longueur;
    }

    @Override
    boolean estSymetrique() {
        return true;
    }
}
