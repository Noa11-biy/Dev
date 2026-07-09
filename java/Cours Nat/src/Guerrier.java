public class Guerrier {

    String nom;
    int pv;
    int pvMax;
    int attaque;
    int defense;
    int score;


    Guerrier(String nom, int desPv, int uneAttaque, int uneDefense){
        this.nom = nom;
        this.pv = desPv;
        pvMax = desPv;
        this.attaque = uneAttaque;
        this.defense = uneDefense;
        score = 0;
    }

    int calculerDegats(Guerrier cible){
        int degats = attaque - cible.defense;
        if (degats < 1){
            degats = 1;
        }
        return degats;
    }

    void attaquer(Guerrier cible){
        int degats = calculerDegats(cible);
        cible.pv = cible.pv - degats;
        if (cible.pv < 0){
            cible.pv = 0;
        }
        System.out.println(nom + " attaque " + cible.nom + " pour " + degats);
    }

    void soigner(int montant){
        pv = pv + montant;
        if (pv > pvMax){
            pv = pvMax;
        }
        System.out.println(nom + "se soigne de "+ montant+"PV ->" + pv +  "/" + pvMax);
    }

    boolean estVivant(){
        return pv > 0;
    }

    void affichier(){
        System.out.println(nom + " | PV :" + pv + "/" + pvMax +
                "| ATT :" + attaque +
                "| DEF :" + defense +
                "| score :" + score);
    }

}
