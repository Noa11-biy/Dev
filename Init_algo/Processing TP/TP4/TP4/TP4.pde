//Exercice 1
/*
int y;
 int racDeux(int x){
 for (int i =0;i<=x/2;i++){
 if ( x==i*i){
 return(i);
 }
 }
 return -1;
 }
 
 void setup(){
 int y=Clavier.lireEntier();
 println(racDeux(y));
 }
 */

//Exercice 2
/*
boolean premier(int n){
 for (int i =2 ; i<n ;i++){
 if( n%i ==0){
 return false;
 }
 }
 return true;
 }
 
 void setup(){
 int n =Clavier.lireEntier();
 println(premier(n));
 }
 */

//Exercice 3
/*
boolean premiersEntreEux(int n, int m){
 for ( int i = 2;i<n && i<m;i++){
 if((n%i==0 && m%i==0)){
 return false;
 }
 }
 return true;
 }
 
 void setup(){
 int n = Clavier.lireEntier();
 int m = Clavier.lireEntier();
 println (premiersEntreEux(n, m));
 }
 */

//Exercice 4
/*
float perimetre(float rayon){
 return rayon*2*PI;
 }
 float surface(float rayon){
 return PI*(rayon*rayon);
 }
 
 
 void setup(){
 float rayon = Clavier.lireReel();
 println(perimetre(rayon));
 println(surface(rayon));
 }
 */

//Exercice 5
/*
float perimetre(float rayon) {
 return rayon*2*PI;
 }
 float surface(float rayon) {
 return PI*(rayon*rayon);
 }
 
 
 void setup() {
 // float rayon = Clavier.lireReel();
 int n = Clavier.lireEntier();
 float tab[]=new float[n];
 for (int i = 0; i<n; i++) {
 tab[i] = random(5, 100);
 }
 for ( int i =0; i<n; i++) {
 println(tab[i]);
 println(perimetre(tab[i]));
 println(surface(tab[i]));
 println();
 }
 }
 */

//Exercice 6
/*
int données(int a, int b) {
 int d=b;
 int e=a;
 int c=1;
 
 while (d>0) {
 if (d%2 ==0) {
 d = d/2;
 } else {
 d = (d-1)/2;
 c = c*e;
 }
 e = e*e;
 }
 return c;
 }
 
 void setup() {
 for ( int a =1; a<=10; a++) {
 for ( int b =0; b<=5; b++) {
 int resultat=données(a, b);
 println("a =", a, ", b =", b, "=>résultat =", resultat);
 }
 }
 }
 // revient à faire a*b
 */


//Exercice 7
void setup() {
  int pas = Clavier.lireEntier();
  int nb_personnes = 41;
  boolean vivants[] = new boolean[nb_personnes];
  for (int j=0; j<nb_personnes; j++) {
    vivants[j]=true;
  }
  int reste = nb_personnes;
  int cpt =0;
  int i = 0;

  while (reste>2) {
    if (vivants[i]) {
      cpt++;
      if (cpt == pas) {
        println(i+1);
        vivants[i] = false;
        reste = reste-1 ;
        cpt = 0;
      }
    }
    i = (i+1)/nb_personnes;
  }
  println("Elimines :", i);
}
