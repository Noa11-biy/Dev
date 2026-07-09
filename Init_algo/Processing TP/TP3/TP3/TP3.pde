//Exercice 1
/*
int i;
 float moy = 0;
 int max = 0;
 int min = 5001;
 int[] tab = new int[1000];
 for (i = 0; i<1000; i++) {
 tab[i] = (int)random(0, 5001);
 moy += tab[i];
 if (tab[i]<min) {
 min =tab[i];
 }
 if (tab[i]>max) {
 max=tab[i];
 }
 }
 moy = moy/1000;
 
 println("La moyenne est", moy);
 println("La valeur maximum est", max);
 println("La valeur minimum est", min);
 */

//Exercice 2
/*
size (400, 400);
 int i;
 int j;
 float distance;
 int[] tx = new int[50];
 int[] ty = new int[50];
 boolean contact = false;
 for (i = 0; i<50; i++) {
 while ( contact == false) {
 tx[i] = (int)random(20, 381);
 ty[i] = (int)random(20, 381);
 contact = true;
 for (j = 0; j<i; j++) {
 distance = dist(tx[i], ty[i], tx[j], ty[j]);
 if ( distance <20) {
 contact = false;
 }
 }
 }
 fill((int)random(256), (int)random(256), (int)random(256));
 ellipse (tx[i], ty[i], 20, 20);
 contact = false;
 }
 */
//Exercice 3

final int NBDES=3;
final int NBFACES=6;
final int NBTIRAGES=100;

int[] tirages = new int[NBTIRAGES*NBDES];
int[] frequences = new int[NBFACES*NBDES+1];
int[] sommes = new int[NBTIRAGES];


for (int i=0; i<NBFACES*NBDES+1; i++) {
  frequences[i] = 0;
}

for (int i = 0; i<NBTIRAGES*NBDES; i++) {
  tirages[i]= (int)random(NBFACES) +1;
 // frequences[tirages[i]]++;
}

for( int i= 0;i<sommes.length;i++){
  sommes[i] = 0;
  for(int j =0;j<NBDES;j++){
    sommes[i] += tirages[i*NBDES+j];
  }
  frequences[sommes[i]]++;
}
for (int i =NBDES; i<frequences.length; i++) {
  println("Nombre de", i, "tirés:", frequences[i], "soit", float(frequences[i])*100/NBTIRAGES+"%");
}

size(800,400);
