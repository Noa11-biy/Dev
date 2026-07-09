package Modele;

public class Skis extends Materiel {
    private int longueur;
    private String marque;

    Skis(String ref, int longueur, String marque){
        super(ref);
        this.longueur = longueur;
        this.marque = marque;
    }


    @Override
    public void setReference(String reference) {
        super.setReference(reference);
    }

    public void setLongueur(int longueur) {
        this.longueur = longueur;
    }

    public void setMarque(String marque){
        this.marque = marque;
    }

    public String getReference(){
        return super.getReference();
    }

    public int getLongueur(){
        return  longueur;
    }

    public String getMarque(){
        return marque;
    }

    @Override
    public String toString(){
        return "SKIS" + "\n" + "MARQUE :" + marque + "\n" + "LONGUEUR :" + longueur + "\n" +"REF :" + super.getReference() + "\n" +
                "DISPONIBLE :" + super.getDisponibilite();
    }

    @Override
    public double getPrixJournee() {
        if (marque.equals("Roussignol")) {
            return 20.0;
        } else if (marque.equals("Atomic")) {
            return 18.0;
        } else {
            return 0;
        }
    }
}
