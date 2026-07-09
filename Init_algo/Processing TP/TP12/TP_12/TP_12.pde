Balle balle;
int taille = 10000;
Balle tabBalle[] = new Balle[taille];

void setup() {
  background(0);
  //size(800, 600);
  fullScreen();
  balle = new Balle();
  balle.initBalle();
  balle.affiche();
  for (int i = 0; i<taille; i++) {
    tabBalle[i] = null;
  }
}

void draw() {
  background(0);
  balle.deplace(5);
  balle.verifieRebond();
  balle.affiche();
  for (int i = 0; i<taille; i++) {
    if ( tabBalle[i] != null) {
      tabBalle[i].deplace(5);
      tabBalle[i].verifieRebond();
      tabBalle[i].affiche();
    }
  }
}

void keyPressed() {
  if (key == '+') {
    for (int i = 0; i<taille; i++) {
      if ( tabBalle[i] == null) {
        tabBalle[i] = new Balle();
        tabBalle[i].initBalle();
        break;
      }
    }
  }
}

void mousePressed() {
  for (int i = 0; i<taille; i++) {
    if (tabBalle[i] !=null) {
      if (dist(mouseX,mouseY,tabBalle[i].x,tabBalle[i].y)<=tabBalle[i].rayon/2) {
        tabBalle[i] = null;
      }
    }
  }
}
