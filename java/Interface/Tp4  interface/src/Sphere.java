public class Sphere extends F3D{

    private double rayon;

    public Sphere(String nom, double rayon){
        super(nom);
        this.rayon = rayon;
    }

    @Override
    boolean estSymetrique() {
        return true;
    }

    @Override
    public double volume() {
        return (4.0 / 3.0) * Math.PI * rayon * rayon * rayon;
    }

    @Override
    public double calculeSurface() {
        return 4*Math.PI*rayon*rayon;
    }
}