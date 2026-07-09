package Modele;

public class MainClass {
    public static void main(String[] args) {
        //A décommenter pour la question 4
        Skis s=new Skis("S034",180,"Rossignol");
        Chaussures c=new Chaussures("C45",42);
        Luge l=new Luge("L233",1);

        //A decommenter pour la question 6
        System.out.println(s);
        System.out.println(c);
        System.out.println(l);

        //A decommenter pour la question 7 (equals)
        System.out.println(s.equals(c)); //doit afficher false (s et c sont différents)
        Skis s2=new Skis("S034",180,"Rossignol"); //on crée un second objet avec les même valeurs que s
        System.out.println(s==s2); //doit afficher false, opérateur == vérifie les références, ici elles sont différentes
        System.out.println(s.equals(s2)); //doit afficher true, on passe par la méthode equals, les 2 références sont sur des objets différents qui contiennent les même données


        //A decommenter pour la question 10
//        AgenceLocation agence=new AgenceLocation();
//        agence.ajouterMateriel(new Skis("S124",170,"Rossignol"));
//        agence.ajouterMateriel(new Skis("S132",170,"Atomic"));
//        agence.ajouterMateriel(new Skis("S245",180,"Decathlon"));
//        agence.ajouterMateriel(new Chaussures("C3456",43));
//        agence.ajouterMateriel(new Chaussures("C124",44));
//        agence.ajouterMateriel(new Luge("L123",2));
//        agence.ajouterMateriel(new Luge("L234",1));
//        agence.ajouterMateriel(new Luge("L777",1));
//        agence.ajouterMateriel(new Luge("L777",2)); //doit être ignorée

        //A decommenter pour question 11
//        agence.consulterFiche("L123");
//        agence.consulterFiche("S132");
//        agence.consulterFiche("S321"); // ne fait rien

        //A decommenter pour question 12
//        agence.louerLuge(2);
//        agence.louerLuge(2);//pas possible normalement car plus de luges dispo

        //A decommenter pour question 13
//        agence.louerSkisChaussures(170,43);
//        agence.louerSkisChaussures(170,43);//plus de combi disponible

        //A decommenter pour question 14
//        agence.rendreMateriel("L123");
//        agence.louerLuge(2); //doit afficher la luge L123

        //A decommenter pour question 15
//        System.out.println(agence.getMoney());

    }
}
