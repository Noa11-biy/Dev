Case c1, c2; //<>//
boolean modePause = false;
final int NBCOL=10; //nombre de colonnes de la grille
final int NBLIG=8; //nombre de lignes de la grille
final int TAILLECASE=64; //taille d'une case en pixels
final int NBFIG=1; //nombre de figures (max 12)
int score = 0;

PImage tabimages[]=new PImage[12];
Grille g;


void settings() {
  size(TAILLECASE*NBCOL+200, TAILLECASE*NBLIG);
}

void setup() {
  chargerImages();
  g = new Grille();
  g.initGrille();
  g.afficheConsole();
  //g.supprime(g.tab[5][0]);
  //println();
  //g.afficheConsole();
  c1 = null;
  c2 = null;
}

void draw() {
  if ( g.existeAlignement(true)) {
   score += g.supprimeCasesMarquees();
    if (!g.estJouable()){
      g.toutMarquer();
      g.supprimeCasesMarquees();
    }
    if(modePause == false){
      
    }
  }
  g.dessin();
}

void mousePressed() {
  if ( mouseY/TAILLECASE<NBLIG && mouseX/TAILLECASE<NBCOL) {
    if (c1 == null) {
      c1 = g.tab[mouseY/TAILLECASE][mouseX/TAILLECASE];
      c1.etat = 2;
    } else {
      c2 = g.tab[mouseY/TAILLECASE][mouseX/TAILLECASE];
      c2.etat = 2;

      if ( abs(c1.lig-c2.lig)+abs(c1.col-c2.col)==1 ) {
        c1.echangeIdCase(c2);

        boolean alig = g.chercheAlignementH(c1, false) || g.chercheAlignementV(c1, false) || g.chercheAlignementH(c2, false) || g.chercheAlignementV(c2, false);

        if (!alig) {
          c1.echangeIdCase(c2);
        }
      }
      c1.etat = 0;
      c2.etat = 0;
      c1 = null;
      c2 = null;
    }
  }
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
