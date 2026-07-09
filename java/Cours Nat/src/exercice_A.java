public class exercice_A {

    public static void main(String[] args) {

        //Variable
        int score = 0;
        int multiplicateur = 1;
        int combo = 0;

        for (int i = 0; i < 5; i++) {
            score = ajouterPoints(score, 100, multiplicateur);
            combo++;
            if (combo == 3){
                multiplicateur = 2;
                System.out.println("COMBO x2");
            }

            if (combo == 5){
                multiplicateur = 3;
                System.out.println("COMBO x3");
            }

            affigerStatut(score, combo, multiplicateur);
        }
        System.out.println("Score final:" + score);

    }

     public static int ajouterPoints(int score, int points, int multiplicateur){
        return score + (points * multiplicateur);
     }

     public static void affigerStatut(int score, int combo, int multi){
         System.out.println("Score :" + score + " | combo :" + combo + " | Multi"+ multi);
     }

}
