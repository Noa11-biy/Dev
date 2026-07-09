//Exercice 1
/*
int ligne = 7;
 int colonne = 8;
 int tab[][] = new int[ligne][colonne];
 int largeurCase;
 int hauteurCase;
 
 void setup() {
 size(800, 600);
 largeurCase = width/colonne;
 hauteurCase = height/ligne;
 init_Tab(tab);
 affichage2D(tab);
 }
 
 // Dessiner la grille de couleur
 void draw() {
 affichage2D(tab);
 }
 
 
 //Initialiser le tableau
 void init_Tab(int tab[][]) {
 for (int i = 0; i<tab.length; i++) {
 for (int j =0; j<tab[i].length; j++) {
 tab[i][j] = int(random(0, (256*256*256)));
 }
 }
 }
 
 //Afficher le tableau
 void affichage2D(int tab[][]) {
 for (int i = 0; i<tab.length; i++) {
 for (int j = 0; j<tab[i].length; j++) {
 print(tab[i][j], +' ');
 afficheCase(i, j);
 }
 }
 println();
 }
 
 //Convertir les entiers en couleurs
 int colorToInt(color c) {
 int resultat = int(red(c))*256*256 + int(green(c))*256 + int(blue(c));
 return resultat;
 }
 
 color intToColor(int value) {
 int bpfB = (value%256);
 int bpfG = (value/256)%256;
 int bpfR = value/(256*256);
 color c = color(bpfR, bpfG, bpfB);
 return c;
 }
 
 //Afficher la grille
 void afficheCase(int lig, int col) {
 stroke(255);
 fill(intToColor(tab[lig][col]));
 int x = col * largeurCase;
 int y = lig * hauteurCase;
 int l = largeurCase;
 int h = hauteurCase;
 rect(x, y, l, h);
 }
 
 void clickToChangeColor(int lig, int col){
 tab[lig][col] = int(random(0, (256*256*256)));
 }
 
 void mousePressed() {
 if (mousePressed) {
 int x_click = mouseX / largeurCase;
 int y_click = mouseY / hauteurCase;
 clickToChangeColor(y_click,x_click);
 }
 }
 */

//Exercice 2
final int TAILLE=120; // taille du tableau
final int AGRANDISSEMENT = 10; // largeur (en pixels) d'une ligne correspondant à une case du tableau
final int LARGEUR_FENETRE = TAILLE*AGRANDISSEMENT; //largeur en pixels de la fenetre
final int HAUTEUR_FENETRE = 600;
int[]tab=new int[TAILLE];
int ind_echange = 0;

void settings() {
  size (LARGEUR_FENETRE, HAUTEUR_FENETRE);
}

void setup() {
  color couleurDepart = color(255, 0, 0);
  color couleurArrivee = color(0);
  initTab(couleurDepart, couleurArrivee);
  melange(tab);
}

void draw() {
  //affichage(tab);
  if (etapeTriBulle2()) {
    affichage(tab);
  }
}

void permute(int tab[], int index, int index2) {
  int temp;
  temp = tab[index];
  tab[index] = tab[index2];
  tab[index2] = temp;
}

//Convertir les entiers en couleurs
int colorToInt(color c) {
  int resultat = int(red(c))*256*256 + int(green(c))*256 + int(blue(c));
  return resultat;
}

color intToColor(int value) {
  int bpfB = (value%256);
  int bpfG = (value/256)%256;
  int bpfR = value/(256*256);
  color c = color(bpfR, bpfG, bpfB);
  return c;
}

void initTab(color depart, color arrivee) {
  for (int i = 0; i< TAILLE; i++) {
    int R = int(red(depart) + ((red(arrivee) - red(depart)) / TAILLE)*i);
    int G = int(green(depart) + ((green(arrivee) - green(depart)) / TAILLE)*i);
    int  B = int(blue(depart) + ((blue(arrivee) - blue(depart)) / TAILLE)*i);
    tab[i] = colorToInt(color(R, G, B));
  }
}

void affichage(int tab[]) {
  for (int i = 0; i<TAILLE; i++) {
    int a_x = i * AGRANDISSEMENT;
    color c = intToColor(tab[i]);
    fill(c);
    rect (a_x, 0, LARGEUR_FENETRE, HAUTEUR_FENETRE);
  }
}

void melange(int tab[]) {
  for (int i = 0; i<3*TAILLE; i++) {
    permute(tab, int(random(tab.length)), int(random(tab.length)));
  }
}
/*
int recherche_ind_min(int tab[], int ind_echange) {
  int ind_min = ind_echange;
  for (int i = ind_echange; i<TAILLE-1; i++) {
    ind_min = i;
  }
}
*/
boolean etapeTriBulle() {
  for ( int i = 0; i<TAILLE-1; i++) {
    if (tab[i]>tab[i+1]) {
      permute(tab, i, i+1);
      return true;
    }
  }
  return false;
}

boolean etapeTriBulle2() {
  boolean perm =false;
  for (int i = 0; i<TAILLE-1; i++) {
    if ( tab[i] > tab[i+1]) {
      permute(tab, i, i+1);
      perm = true;
    }
  }
  return perm;
}
/*
boolean etapeTriSelectif() {
  boolean perm = false;
  for (int i = 0; i<TAILLE-1; i++) {
    permute(tab,
  }
}
*/
