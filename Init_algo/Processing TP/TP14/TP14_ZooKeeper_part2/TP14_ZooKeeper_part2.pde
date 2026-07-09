final int NBCOL=10; //nombre de colonnes de la grille //<>//
final int NBLIG=8; //nombre de lignes de la grille
final int TAILLECASE=64; //taille d'une case en pixels
final int NBFIG=6; //nombre de figures (max 12)

PImage tabimages[]=new PImage[12];
Grille g;

void settings() {
  size(TAILLECASE*NBCOL+200, TAILLECASE*NBLIG);
}

void setup() {
  g=new Grille();
  g.initGrille();
  g.afficheConsole();
  chargerImages();
  
}

void draw() {
 
}

void mousePressed(){
  
}


void chargerImages() {
  tabimages[0]=loadImage("chauvesouris.png");
  tabimages[1]=loadImage("coccinelle.png");
  tabimages[2]=loadImage("cochon.png");
  tabimages[3]=loadImage("dauphin.png");
  tabimages[4]=loadImage("elephant.png");
  tabimages[5]=loadImage("grenouille.png");
  tabimages[6]=loadImage("lion.png");
  tabimages[7]=loadImage("panda.png");
  tabimages[8]=loadImage("pigeon.png");
  tabimages[9]=loadImage("poisson.png");
  tabimages[10]=loadImage("poussin.png");
  tabimages[11]=loadImage("serpent.png");

  for (int i=0; i<tabimages.length; i++) {
    tabimages[i].resize(TAILLECASE, TAILLECASE);
  }
}
