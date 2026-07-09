package Modele;

public class Chaussures extends Materiel {
    private int pointure;

    public Chaussures(String ref, int pointure) {
        super(ref);
        this.pointure = pointure;
    }

    @Override
    public void setReference(String reference) {
        super.setReference(reference);
    }

    public void setMarque(String marque){
        this.pointure = pointure;
    }

    public String getReference(){
        return super.getReference();
    }

    public int getPointure(){
        return pointure;
    }

    @Override
    public String toString() {
        return "CHAUSSURES" + "\n" + "POINTURE :" + pointure + "\n" +"REF :" + super.getReference() + "\n" +
                "DISPONIBLE :" + super.getDisponibilite();
    }

    @Override
    public double getPrixJournee() {
        return 12.0;
    }
}
