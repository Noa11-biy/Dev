//Exercice 1
/*
float racEntierApro(float b) {
 float x0=1;
 float x1=0.5*(x0+b/x0);
 while (abs(x1-x0)>0.000001) {
 x0=x1;
 x1 = 0.5*(x0+b/x0);
 }
 return x1;
 }
 
 
 void setup() {
 int b = Clavier.lireEntier();
 println(racEntierApro(b));
 }
 */
//Exercice 2
/*
int Listev9a(int n) {
  int a0= n%10;
  int a1=n/10;
  return a0*10+a1;
}
void Listev9(int N) {
  if (N<10 || N>99) {
    println("erreur, le nombre n'as pas 2 chiffres");
    return ;
  }
  if (N%10==N/10) {
    println("erreur, le nombres à 2 même chiffres");
    return;
  }
  int N1=N;
  println(N1);
  while (N1!=9) {
    N1=abs(N1-Listev9a(N1));
    print("->",N1);
  }
}

void setup() {
  int n = Clavier.lireEntier();
  Listev9(n);
}
*/

//Exercice 4
/*
int[] tab(int n){ //<>//
  int t[] = new int[n];
  t[0] = 1;
  for ( int i =1;i<t.length;i++){
    t[i] = 0;
  }
  return t;
}
 void affichage (int t2[], int nb){
   for( int i =0;i<nb;i++){
     print(t2[i], "");
   }
   println();
 }

void trianglePascal(int n){
 int ligne[]= tab(n);
 for (int i =0;i<n;i++){
   affichage (ligne, i);
    for ( int k =i;k>0;k--){
      ligne[k] = ligne[k] + ligne[k-1];
    }
 }
}

void setup(){
  int n = Clavier.lireEntier();
  trianglePascal(n);
}
*/

//Exercice 5
