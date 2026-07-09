package Modele;

import java.util.Objects;

public abstract class  Materiel {
    private String reference;
    private boolean disponiblite;

    public Materiel(String ref, boolean dispo){
        this.reference = ref;
        this.disponiblite = dispo;
        dispo = true;
    }

    public Materiel(String ref){
        this.reference = ref;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setDisponiblite(boolean disponiblite) {
        this.disponiblite = disponiblite;
    }

    public String getReference() {
        return reference;
    }

    public Boolean getDisponibilite(){
        return true;
    }

    public String toString(){
        return "REF :" + reference  + "\n" +
        "DISPONIBLE :" + disponiblite;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Materiel materiel)) return false;
        return disponiblite == materiel.disponiblite && Objects.equals(reference, materiel.reference);
    }

    public abstract double getPrixJournee();

}
