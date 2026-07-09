class Case {
  int id; //id de l'image contenue dans cette case
  int lig, col; //numéros de ligne et colonne dans la grille
  int etat; //0: normale; 1:à supprimer; 2: sélectionnée (par exemple)

  void initCase(int l, int c) {
    this.id = (int)random(0, NBFIG);
    this.lig = l;
    this.col = c;
    this.etat = 0;
  }

  void echangeIdCase(Case b) {
    int tmp = this.id;
    this.id = b.id;
    b.id = tmp;
  }

  //VUE
  void dessin() {
    if (etat == 0) fill(255);
    if (etat == 1) fill(255, 0, 0);
    if (etat == 2) fill(255, 255, 0);
    
    rect(col*TAILLECASE, lig*TAILLECASE, TAILLECASE, TAILLECASE);
    image(tabimages[this.id], col*TAILLECASE, lig*TAILLECASE, TAILLECASE, TAILLECASE);
  }
}
