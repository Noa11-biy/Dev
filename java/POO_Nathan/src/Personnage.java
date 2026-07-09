public class Personnage {


    private String nom;
    private int pv;
    private int pvMax;
    protected int defense;

    public Personnage(String nom, int pvMax){
        this.nom = nom;
        this.pvMax = pvMax;
        this.pv = pvMax;
    }

    private Personnage(){;}

    // Accesseurs
    public String getNom(){
        return this.nom;
    }

    public int getPv(){
        return this.pv;
    }

    public int getPvMax(){
        return this.pvMax;
    }

    // Mutateur
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPv(int pv) {
        if(pv < 0){
            this.pv = 0;
        } else if (pv> pvMax) {
            this.pv = pvMax;
        } else {
            this.pv = pv;
        }
    }

    public void recevoirDegats(int degats){
        int nvPv = this.pv - degats;
        this.setPv(nvPv);
    }

    public void attaquer(Personnage cible){
        cible.recevoirDegats(2);
    }

    public void affichierEtat(){
        System.out.println(nom + pv + pvMax);
    }
}
