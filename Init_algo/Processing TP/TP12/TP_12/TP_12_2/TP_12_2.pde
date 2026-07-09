Commune commune;
Commune tab[];

int nbL;

void setup() {
  //commune = new Commune();
  //commune.initCommune(01, "Bourg-en-Bresse", "01000");
  //commune.affiche();
  chargerCommunes("villes.csv");
  afficherTab(20);
  //rechercheCP(Clavier.lireString());
  tri();
  //afficherTab(20);
}

void chargerCommunes (String nom_fichier) {
  String []s = loadStrings(nom_fichier);
  nbL = s.length;
  tab = new Commune[nbL];
  //print(nbL);
  for (int i = 0; i<nbL; i++) {
    String []l = splitTokens(s[i], ";");
    tab[i] = new Commune();
    tab[i].initCommune(int(l[0]), l[1], l[2]);
  }
}

void afficherTab(int k) {
  for (int i = 0; i<k; i++) {
    tab[i].affiche();
  }
}

void rechercheCP(String nomVille) {
  for (int i = 0; i<nbL; i++) {
    if (tab[i].nom.toLowerCase().equals(nomVille.toLowerCase())) {
      tab[i].affiche();
    }
  }
}

void tri() {
  boolean perm =true;
  while (perm) {
    perm = false;
    for (int i = 0; i<nbL-1; i++) {
      if (tab[i].nom.compareTo(tab[i+1].nom)>0) {
        Commune tmp = tab[i];
        tab[i] = tab[i+1];
        tab[i+1] = tmp;
        perm = true;
      }
    }
  }
}
