class Grille { //<>//
  Case tab[][];

  void initGrille() {
    tab = new Case[NBLIG][NBCOL];
    for (int i = 0; i<NBLIG; i++) {
      for (int j = 0; j<NBCOL; j++) {
        tab[i][j] = new Case();
        tab[i][j].initCase(i, j);
      }
    }
  }

  void afficheConsole() {
    for (int i = 0; i<NBLIG; i++) {
      for (int j = 0; j<NBCOL; j++) {
        print(tab[i][j].id + "\t");
      }
      println(" ");
    }
  }

  void supprime(Case c) { //0: normale; 1:à supprimer; 2: sélectionnée (par exemple)
    int ligne = c.lig;
    int colonne = c.col;
    for (int i = ligne; i>0; i--) {
      tab[i][colonne].echangeIdCase(tab[i-1][colonne]);
    }
    tab[0][colonne].initCase(0, colonne);
    c.etat = 0;
  }

  //cherche un alignement d'au moins 3 images contenant c. Si cet alignement existe, marque les cases (etat=1) si marquer=true et retourne true
  boolean chercheAlignementH(Case c, boolean marquer) {
    int chercherID = c.col;
    int cpt = 0;
    int incr = chercherID;
    while ( chercherID > 0 && tab[c.lig][chercherID-1].id == c.id ) {
      chercherID --;
    }

    while (incr<NBCOL && tab[c.lig][incr].id == c.id) {
      incr ++;
      cpt ++;
    }

    if ( cpt>=3 && marquer) {
      for (int i = 0; i<cpt; i++) {
        tab[c.lig][chercherID + i].etat = 1;
      }
    }
    return cpt >= 3;
  }

  //idem pour verticalement
  boolean chercheAlignementV(Case c, boolean marquer) {
    int chercherID = c.lig;
    int cpt = 0;
    int incr = chercherID;
    while ( chercherID > 0 && tab[chercherID-1][c.col].id == c.id ) {
      chercherID --;
    }

    while (incr<NBLIG && tab[incr][c.col].id == c.id) {
      incr ++;
      cpt ++;
    }

    if ( cpt>=3 && marquer) {
      for (int i = 0; i<cpt; i++) {
        tab[chercherID + i][c.col].etat = 1;
      }
    }
    return cpt >= 3;
  }

  //supprime les cases marquées et retourne le nombre de cases supprimées
  int supprimeCasesMarquees() {
    int cpt = 0;
    for (int i = 0; i<NBLIG; i++) {
      for (int j = 0; j<NBCOL; j++) {
        if ( tab[i][j].etat == 1) {
          supprime(tab[i][j]);
          cpt ++;
        }
      }
    }
    return cpt;
  }

  //vérifie s'il existe un alignement, donc s'il y a des suppressions automatiques à faire
  boolean existeAlignement(boolean marquer) {
    boolean alig = false;
    for (int  i = 0; i<NBLIG; i++) {
      for (int  j = 0; j<NBCOL; j++) {
        if ( chercheAlignementH(tab[i][j], marquer)) alig = true;
        if ( chercheAlignementV(tab[i][j], marquer)) alig = true;
      }
    }


    return alig;
  }

  //vérifie s'il existe au moins un échange de cases possible qui engendre un alignement de trois cases
  boolean estJouable() {
    for (int  i = 0; i<NBLIG; i++) {
      for (int j = 0; j<NBCOL-1; j++) {
        tab[i][j].echangeIdCase(tab[i][j+1]);
        if (existeAlignement(false)) {
          tab[i][j].echangeIdCase(tab[i][j+1]);
          return true;
        } else tab[i][j].echangeIdCase(tab[i][j+1]);
      }
    }
    for (int  i = 0; i<NBLIG-1; i++) {
      for (int j = 0; j<NBCOL; j++) {
        tab[i][j].echangeIdCase(tab[i+1][j]);
        if (existeAlignement(false)) {
          tab[i][j].echangeIdCase(tab[i+1][j]);
          return true;
        } else tab[i][j].echangeIdCase(tab[i+1][j]);
      }
    }
    return false;
  }

  void toutMarquer() { //si aucun coup n'est jouable, met toutes les cases dans l'état 1 (à supprimer)
    for (int i = 0; i<NBLIG; i++) {
      for (int j = 0; j<NBCOL; j++) {
        tab[i][j].etat = 1;
      }
    }
  }

  //VUE

  void dessin() {
    background(0);
    for (int i = 0;i<NBLIG;i++){
      for (int j = 0;j<NBCOL;j++){
        tab[i][j].dessin();
      }
    }
    fill(0,255,0);
    textSize(16);
    text("Score :"+score,  width-195, 25);
    textAlign(LEFT);
  }
}
