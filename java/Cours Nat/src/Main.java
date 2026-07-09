public class Main {
    public static void main(String[] args) {
        Guerrier tsahal = new Guerrier("israel", 100, 50, 50);
        Guerrier mossad = new Guerrier("goat", 100, 20, 45);

        int tour = 1;

        while (tsahal.estVivant() && mossad.estVivant()){
            System.out.println(" ==================" + "TOUR" + tour + "====================");

            if(tour % 3 == 0){
                tsahal.soigner(20);
            }

            tsahal.attaquer(mossad);

            if (!mossad.estVivant()){
                break;
            }

            mossad.attaquer(tsahal);

            tour++;
        }

        System.out.println("============= FIN COMBAT ===============");

        if (tsahal.estVivant()){
            tsahal.score = tsahal.score + 100;
            System.out.println(tsahal.nom + " a gagné vive netanyahou");
            tsahal.affichier();
        } else {
            System.out.println(mossad.nom + " a gagné vive israel");
        }
    }
}
