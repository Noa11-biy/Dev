void initTab(boolean personnages[] ) {
  for (int i =0; i<personnages.length; i++) {
    personnages[i]=true;
  }
}

void assomme (int arbitre, boolean personnages[]) {
  int nbRestants = personnages.length;
  int cpt=0;
  int position=0;
  do {

    do{    // recherche du prochaine  voleur à assomer
      position = (++position)%(personnages.length);
      if (personnages[position]) cpt++;
    }while(cpt < arbitre);  // recherche du prochaine  voleur à assomer

    personnages[position]=false;  // on assomme //<>//
    cpt=0;
    nbRestants--;
  } while (nbRestants >2);

}

void afficheRestants (boolean personnages[]){
    for (int i =0; i<personnages.length; i++) {
      if (personnages[i]) println((i+1) + " est vivant");
  }
}

void setup() {
  int voleurs = Clavier.lireEntier("nb voleurs");
  int arbitre = Clavier.lireEntier("nb arbitre");
  boolean personnages[] = new boolean[voleurs+1];
  initTab(personnages);
  assomme(arbitre, personnages);
  afficheRestants(personnages);
  
}
