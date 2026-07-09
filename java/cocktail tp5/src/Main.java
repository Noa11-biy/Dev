import java.util.HashMap;

public class Main {
    public static void main(String Args[]){
        System.out.println("------TP Cocktails--------");

        // Ingredients de base permettant de réaliser plusieurs cocktails
        Ingredient jusOrange = new Jus("jus d'orange","orange", "orange");
        Ingredient jusCitron= new Jus("jus de citron","jaune pale","citron");
        Ingredient jusCitronVert = new Jus("jus de citron vert","vert", "citron vert");
        Ingredient jusAnanas = new Jus("jus d'ananas","jaune", "ananas");
        Ingredient jusFruitPassion = new Jus("jus de fruit passion","jaune","fruit de la passion");
        Ingredient siropGrenadine = new Sirop("sirop de grenadine", "rouge",5);
        Ingredient schweppes = new Eau("Schweppes","gazeuse");
        Ingredient badoit = new Eau("Badoit","gazeuse");
        Ingredient mentheFeuille = new IngredientSolide("menthe","vert",Unite.feuille);
        Ingredient rhumBlanc = new Alcool("Rhum blanc","transparent",40);
        Ingredient rhumVieux = new Alcool("Rhum vieux","ambré",40);
        Ingredient curacao = new Alcool("Curaçao","bleu",30);
        Ingredient vodka = new Alcool("Vodka","transparent",40);
        Ingredient tequila = new Alcool("Tequila","ambré",35);
        Ingredient tripleSec = new Alcool("Triple Sec","transparent",40);
        Ingredient gin = new Alcool("Gin","transparent",40);
        Ingredient sucre = new IngredientSolide("sucre","transparent", Unite.g);
        Ingredient citronTranche = new IngredientSolide("tranche de citron ","jaune", Unite.tranche);
        Ingredient citronVertTranche = new IngredientSolide("tranche de citron vert","vert", Unite.tranche);


        // Creation des cocktails (avec leurs recettes)

        /**  Blue Hagoon  **/
//         création de la liste des ingrédients et  création du cocktail (question 6)
        HashMap<Ingredient, Integer> mapBlueLagoon = new HashMap<>();
        mapBlueLagoon.put(vodka,4);
        mapBlueLagoon.put(curacao,2);
        mapBlueLagoon.put(badoit,10);
//         ou création du cocktail et ajout des ingrédients
        Cocktail blueLagoon = new Cocktail("Blue Lagoon", mapBlueLagoon);


        /**  Bora Bora **/
        // création de la liste des ingrédients et du cocktail (question 7)
        HashMap<Ingredient, Integer> mapBoraBora = new HashMap<>();
        mapBoraBora.put(jusAnanas, 10);
        mapBoraBora.put(jusFruitPassion, 6);
        mapBoraBora.put(jusCitron, 1);
        mapBoraBora.put(siropGrenadine, 1);
        // ou création du cocktail et ajout des ingrédients
        Cocktail boraBora = new Cocktail("Bora Bora", mapBoraBora);


        /**  Mojito **/
        // création de la liste des ingrédients et du cocktail (question 8)
        HashMap<Ingredient, Integer> mapMojito = new HashMap<>();
        mapMojito.put(rhumVieux,6);
        mapMojito.put(jusCitronVert,5);
        mapMojito.put(sucre,2);
        mapMojito.put(mentheFeuille,6);
        mapMojito.put(citronVertTranche,1);
        mapMojito.put(siropGrenadine,1);
        // ou création du cocktail et ajout des ingrédients
        Cocktail mojito = new Cocktail("Mojito", mapMojito);


        //affichage des cocktails (question 9)
        System.out.println(blueLagoon);
        System.out.println(boraBora);
        System.out.println(mojito);


        // affichage du prix de revient et de vente (question 10)
//        affichePrix(blueLagoon);
//        System.out.println();
//        affichePrix(boraBora);
//        System.out.println();
//        affichePrix(mojito);
//        System.out.println();


        // creation de la carte des cocktails (question 11)
//        CarteCocktails carte=new CarteCocktails("Bar à Cocktails");
//        carte.addCocktail(mojito);
//        carte.addCocktail(boraBora);
//        carte.addCocktail(blueLagoon);
//        System.out.println(carte);

    }


    // fonction d'affichage des prix de revient et de vente (question 9)
    private static void affichePrix(Cocktail cocktail) {
        System.out.println(cocktail.getNom());
        System.out.print("\tprix de revient de "+ cocktail.getNom() + " : ");
        System.out.format("%.2f", +cocktail.getPrixRevient());
        System.out.println("€");
        System.out.print("\tprix de vente de "+ cocktail.getNom() + " : ");
        System.out.println(cocktail.getPrixVente()+"€");
    }
}
