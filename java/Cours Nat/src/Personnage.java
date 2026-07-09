public class Personnage {

    // LES ATTRIBUTS
    // variables propres un objets

    public String nom;
    public int pv;
    public int attaque;
    public int score;

    // LE CONSTRUCTEURS
    public Personnage(String unNom, int desPv, int uneAttaque){
        this.nom = unNom;
        this.pv = desPv;
        this.attaque = uneAttaque;
        this.score = 0;
    }
    public Personnage(String nom){
        this.nom = nom;
    }

    // LES MÉTHODES
    // C'est des action des persos
    public void affichierStatut(){
        System.out.println("Personnahe :" + nom);
        System.out.println("PV :" + pv);
        System.out.println("Attaque :" + attaque);
        System.out.println("Score :" + score);
    }

    public boolean estVivant(){
        return pv > 0;
    }

    public void recevoirDegats(int degats){
        pv = pv - degats;
        if (pv < 0){
            pv = 0;
        }
    }

    public void gagnerPoints(int points){
        score = score + points;
    }
}
