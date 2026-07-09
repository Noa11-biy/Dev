public class Carre extends Rectangle{

    private double cote;

    public Carre (String nom,double cote){
        super(nom, cote, cote);
        this.cote = cote;
    }

    @Override
    public double calculeSurface() {
        return super.calculeSurface();
    }

    @Override
    public double perimetre() {
        return super.perimetre();
    }

    @Override
    public boolean estSymetrique(){
        return true;
    }
}