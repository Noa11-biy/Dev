package Modele;

public class Luge extends Materiel{
    private int nb_places;

    public Luge(String ref, int places){
        super(ref);
        this.nb_places = places;
    }

    @Override
    public void setReference(String reference) {
        super.setReference(reference);
    }

    public void setNb_places(int places) {
        this.nb_places = places;
    }

    public String getReference(){
        return super.getReference();
    }

    public int getNb_places(){
        return nb_places;
    }

    @Override
    public String toString() {
        return "LUGES" + "\n" + "NB_PLACES :" + nb_places + "\n" + "REF :" + super.getReference() + "\n" +
                "DISPONIBLE :" + super.getDisponibilite();
    }

    @Override
    public double getPrixJournee() {
        if (nb_places == 1) {
            return 8.0;
        }else{
            return 12.0;
        }
    }
}
