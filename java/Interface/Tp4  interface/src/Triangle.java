public class Triangle extends F2D{
    private double cote1;
    private double cote2;
    private double cote3;

    public Triangle(String nom, double cote1, double cote2, double cote3){
        super(nom);
        this.cote1 = cote1;
        this.cote2 = cote2;
        this.cote3 = cote3;
    }


    @Override
    boolean estSymetrique() {
        return cote1 == cote2 || cote2 == cote3 || cote1 == cote3;
    }

    @Override
    public double perimetre() {
        return cote1 + cote2 + cote3;
    }

    @Override
    public double calculeSurface() {
        double s = (cote1 + cote2 + cote3) / 2;
        return Math.sqrt(s * (s - cote1) * (s - cote2) * (s - cote3));
    }
}