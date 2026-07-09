class Grille { //<>//
  Case tab[][];

  void initGrille() {
    tab=new Case[NBLIG][NBCOL];
    for (int i=0; i<NBLIG; i++)
      for (int j=0; j<NBCOL; j++) {
        tab[i][j]=new Case();
        tab[i][j].initCase(i, j);
      }
  }

  void afficheConsole() {
    int i, j;

    //affichage n° col
    for (j=0; j<NBCOL+1; j++)
      print("---");
    println();
    print("  ");
    for (j=0; j<NBCOL; j++)
      print(j+" ");
    println();
    for (j=0; j<NBCOL+1; j++)
      print("---");
    println();
    //donnees
    for (i=0; i<NBLIG; i++) {
      print(i+"|");
      for (j=0; j<NBCOL; j++) {
        print(tab[i][j].id+" ");
      }
      println();
    }
    //fin
    for (j=0; j<NBCOL+1; j++)
      print("---");
    println();
  }

  void supprime(Case c) {
    int i=c.lig;
    int col=c.col;
    while (i>=1) {
      tab[i][col].id=tab[i-1][col].id;
      i--;
    }
    tab[0][col].initCase(0, col);
    c.etat=0;
  }

  //cherche un alignement d'au moins 3 images contenant c. Si cet alignement existe, marque les cases (etat=2) si marquer=true et retourne true
  boolean chercheAlignementH(Case c, boolean marquer) {
    int i, cpt=0;
    int j, debut;
    //à partir de c, on cherche la case accolée la plus à gauche
    j=c.col-1;
    while (j>=0 && tab[c.lig][j].id==c.id) {
      j--;
    }
    j++;//on met l'indice sur la case la plus a gauche de l'alignement
    //on compte combien il y a de cases de mm id que c, consécutives, à partir de celle trouvée au-dessus
    debut=j;
    while (j<NBCOL && tab[c.lig][j].id==c.id) {
      cpt++;
      j++;
    }
    //si au moins 3 et qu'il faut les marquer, on les marque
    if (cpt>=3 && marquer==true) {
      j=debut;
      while (j<NBCOL && tab[c.lig][j].id==c.id) {
        tab[c.lig][j].etat=1;
        j++;
      }
    }
    return (cpt>=3);//retourne vrai s'il y a au moins trois cases dans l'alignement
  }

  //idem pour verticalement
  boolean chercheAlignementV(Case c, boolean marquer) {
    int i, cpt=0;
    int debut;
    debut=c.lig;
    while (debut>0 && tab[debut-1][c.col].id==c.id) {
      debut=tab[debut-1][c.col].lig;
    }
    i=debut;
    while (i<NBLIG && tab[i][c.col].id==c.id) {
      cpt++;
      i++;
    }
    if (cpt>=3 && marquer) {
      i=debut;
      while (i<NBLIG && tab[i][c.col].id==c.id) {
        tab[i][c.col].etat=1;
        i++;
      }
      return true;
    }
    return (cpt>=3);
  }

  //supprime les cases marquées
  int supprimeCasesMarquees() {
    int i, j, cpt=0;
    for (i=0; i<NBLIG; i++)
      for (j=0; j<NBCOL; j++)
        if (tab[i][j].etat==1) {
          supprime(tab[i][j]);
          cpt++;
        }
    return cpt;
  }

  //vérifie s'il existe un alignement, donc s'il y a des suppressions automatiques à faire
  boolean existeAlignement(boolean marquer) {
    int i, j;
    for (i=0; i<NBLIG; i++)
      for (j=0; j<NBCOL; j++)
        if (chercheAlignementH(tab[i][j], marquer) || chercheAlignementV(tab[i][j], marquer))
          return true;
    return false;
  }

   boolean estJouable() {
    int i, j;
    for (i=0; i<NBLIG; i++) {
      for (j=0; j<NBCOL-1; j++) {
        //échange horizontal
        tab[i][j].echangeIdCase(tab[i][j+1]);
        if (chercheAlignementH(tab[i][j], false) || chercheAlignementH(tab[i][j+1], false) ||
          chercheAlignementV(tab[i][j], false) || chercheAlignementV(tab[i][j+1], false)) {
          tab[i][j].echangeIdCase(tab[i][j+1]);
          return true;
        }
        tab[i][j].echangeIdCase(tab[i][j+1]);
      }
    }
    for (i=0; i<NBLIG-1; i++) {
      for (j=0; j<NBCOL; j++) {
        //échange vertical
        tab[i][j].echangeIdCase(tab[i+1][j]);
        if (chercheAlignementH(tab[i][j], false) || chercheAlignementH(tab[i+1][j], false) ||
          chercheAlignementV(tab[i][j], false) || chercheAlignementV(tab[i+1][j], false)) {
          tab[i][j].echangeIdCase(tab[i+1][j]);
          return true;
        }
        tab[i][j].echangeIdCase(tab[i+1][j]);
      }
    }
    return false;
  }

  void toutMarquer() {
    //on marque toutes les cases à supprimer
    for (int i=0; i<NBLIG; i++)
      for (int j=0; j<NBCOL; j++)
        tab[i][j].etat=1;
  }


  //VUE

  void dessin() {
    
  }
}
