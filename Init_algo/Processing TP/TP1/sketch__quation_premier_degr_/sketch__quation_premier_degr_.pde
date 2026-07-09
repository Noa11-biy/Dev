//Équation du premier degré //<>// //<>//
//float A=Clavier.lireReel("donne une valeur");
//float B=Clavier.lireReel("donne une valeur");
//if(A!=0); {
//println("x=", -B/A);
//}

//conversion de durées
/*
int secondes=Clavier.lireEntier("donne une valeur");
 
 int jours = secondes /(24*60*60); // nombre de jours
 secondes = secondes % (24*60*60); // reste de secondes
 
 int heures = secondes /(60*60); // nombre d'heures
 secondes = secondes % (60*60);// restes de secondes
 
 int minutes = secondes /60;
 secondes = secondes % 60;
 
 println(jours);
 println(heures);
 println(minutes);
 println(secondes);
 */
//
/*char c=Clavier.lireCaractere("donne une lettre");
 int n=Clavier.lireEntier("donne un chiffre");
 if (c=='a') {
 for (int i=0; i<n; i++)
 print('*');
 }
 if (c=='b') {
 for (int i=0; i<n; i++) {
 for (int j=0; j<n-1; j++)
 print('*');
 println('*');
 }
 }
 if (c=='c') {
 for (int i=0; i<n; i++) {
 for (int j=0; j<n; j++) {
 if (i==0 || i==n-1 || j==0 || j==n-1) {
 print('*');
 } else {
 print(' ');
 }
 }
 println();
 }
 }
 if (c=='d') {
 for (int i=0; i<n; i++)
 print(i%2 == 0 ? '*' : '_');
 }
 */
//Nombres Aléatoires
//1
/*int x = 0;
 for(int i=0; i<10; i++){
 x=(int)random(10);
 println(x);
 }
 */
//2
/*int x = 0;
 for(int i=0; i<10; i++){
 x=(int)random(11);
 println(x);
 }
 */
//3
/*int x = 0;
 for(int i=0; i<10; i++){
 x=(int)random(5,11);
 println(x);
 }
 */
//Problème juste prix
/*
int n =(int)random(0,1001);
 int prix;
 int i = 0;
 do {
 prix = Clavier.lireEntier("donne une valeur");
 if(prix<n){
 println("Trop petit");
 i = i+1;
 }
 if(prix>n){
 println("Trop grand");
 i = i+1;
 }
 }
 while(prix !=n);
 if(prix==n){
 println("BRAVO ! Vous avez gangé en", + i ,"essais");
 }
 */
//Problème juste prix versions plusieurs parties
int n;
int prix;
int i =0;
char reponse;
int nb_essais;
nb_essais = Clavier.lireCaractere("donne le nombre d'essais");
println("tu as", i, "essais");
do {
  n=(int)random(0, 1001);
  i=0;
  do {
    prix = Clavier.lireEntier("donne une valeur");
    if (prix<n) {
      println("Trop petit");
      i = i+1;
    }
    if (prix>n) {
      println("Trop grand");
      i = i+1;
    }
  } while (prix !=n && i<nb_essais);
  if (prix==n) {
    println("BRAVO ! Vous avez gangé en", + i, "essais");
    println("PERDU! vous avez espuisé le nombre d'eesais autorisés");
    println("Le prix à trouver était :", + prix);
  }
  println("Voulez-vous rejouer (O/N) ? :" );
  while ( reponse =='O')
    reponse = Clavier.lireCaractere("O/N");
  println("Merci d'avoir jouer !");
}
