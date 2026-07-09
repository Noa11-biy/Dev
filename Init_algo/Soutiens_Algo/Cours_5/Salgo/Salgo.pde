//Exercice 1
/*
int S_diviseurs(int a){
 int somme = 0;
 for (int i =1;i<a;i++){
 if (a%i==0){
 somme += i;
 }
 }
 return somme;
 }
 
 void setup(){
 int a = Clavier.lireEntier();
 println(S_diviseurs(a));
 }
 */

//Exercice 2
/*
int S_diviseurs(int a) {
 int somme = 0;
 for (int i =1; i<a; i++) {
 if (a%i==0) {
 somme += i;
 }
 }
 return somme;
 }
 
 boolean nb_parfait(int a) {
 if (S_diviseurs(a)==a) {
 return true;
 }
 return false;
 }
 
 void setup() {
 int a = Clavier.lireEntier();
 println(S_diviseurs(a));
 if (nb_parfait(a)==true) {
 println("le nombre est parfait");
 }else{
 println("le nombre n'est pas parfait");
 }
 }
 */

//Exercice 3
/*
int S_diviseurs(int a) {
  int somme = 0;
  for (int i =1; i<a; i++) {
    if (a%i==0) {
      somme += i;
    }
  }
  return somme;
}

boolean nb_amicaux(int a, int m) {
  if (S_diviseurs(a)==m && S_diviseurs(m)==a) {
    return true;
  }
  return false;
}

void setup() {
  int a = Clavier.lireEntier();
  int m = Clavier.lireEntier();
  println(S_diviseurs(a));
  if (nb_amicaux(a,m)==true) {
    println("le nombre est amical");
  } else {
    println("le nombre n'est pas amical");
  }
}
*/
//Exercice 4

char[] stringToChar(String s){
  char tab[] = new char[s.length()];
  for(int i = 0;i<s.length();i++){
    tab[i]=s.charAt(i);
  }
  return tab;
}
String charToString(char tab[]){
  String s="";
  for(int i =0;i<tab.length;i++){
    s+=tab[i];
  }
  return s;
}

String melangeString(String s){
  if ( s.length()<=2){
    return s;
  }else{
    char caractere[] = stringToChar(s); 
    int alea;
    char temp;
    for (int i=1;i<caractere.length-1;i++){
      alea = (int)random(1,caractere.length-1);
      temp = caractere[i];
      caractere[i]=caractere[alea];
      caractere[alea]=temp;  
    }
    s=charToString(caractere);
  }
  return s;
}

void setup(){
  String mot = Clavier.lireString();
  println(mot);
  String t[] = split(mot," ");
  for (int i =0; i<t.length;i++){
    println(t[i]);
    t[i]= melangeString(t[i]);
    println(t[i]);
  }
   mot = join(t, " ");
   println(mot);
  }
