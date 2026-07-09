final int NB_CARTES = 32;

/*----------------------------------------------------------------*/
void initjeu(int jeu[]) /* remplit le tableau avec les 32 cartes */
{
  for (int i=0; i<jeu.length; i++) {
    jeu[i] = i;
  }
}
/*----------------------------------------------------------------*/
void permute(int jeu[], int carte1, int carte2) /* permute 2 cartes dans le jeu */
{
  int temp;
  carte1 = (int)random(jeu.length);
  carte2 = (int)random(jeu.length);
  temp = jeu[carte1];
  jeu[carte2] = jeu[carte1];
  jeu[carte2] = temp;
}
/*----------------------------------------------------------------*/
void melange(int jeu[]) /* melange les cartes */
{
  for (int i=0; i<jeu.length; i++) {
    permute(jeu, (int)random(32), (int)random(32));
  }
}
/*----------------------------------------------------------------*/
void affiche_carte(int x) /* affiche une carte (valeur,couleur)*/
{
  switch(x%8) {
  case 4 :
    print('V');
    break;
  case 5 :
    print('D');
    break;
  case 6 :
    print('R');
    break;
  case 7 :
    print('A');
    break;
  default :
    print(7+x%8);
  }
  switch(x/8) {
  case 0 :
    print('P');
    break;
  case 1 :
    print('C');
    break;
  case 2 :
    print('K');
    break;
  case 3 :
    print('T');
    break;
  }
}
/*----------------------------------------------------------------*/
void affiche(int jeu[], int nb) /* affiche nb cartes, ici nb est le nombre de cartes de jeu[] */
{
  for (int i =0; i<nb; i++) {
    affiche_carte(jeu[i]);
    print(",");
  }
  println();
}
/*----------------------------------------------------------------*/
void distribution(int jeu[], int jeu1[], int jeu2[])/* distribue les cartes aux deux joueurs */
{
  for (int i=0; i<16; i++) {
    jeu1[i] = jeu[2*i];
    jeu2[i] = jeu[2*i+1];
  }
}
/*----------------------------------------------------------------*/
//version sans bataille
int jouer1coup( int jeu1[], int jeu2[], int n1, int n2) // retourne le nombre de cartes gagnées/perdues par le joueur 1
{
  int carte1 = jeu1[0];
  int carte2 = jeu2[0];
  decalerjeu(jeu1, n1);
  decalerjeu(jeu2, n2);
  if (carte1%8 > carte2%8) {
    jeu1[n1] = carte2;
    jeu1[n1+1] = carte1;
    return 1;
  } else {
    if (carte1%8 < carte2%8) {
      jeu2[n2] = carte2;
      jeu2[n2+1] = carte1;
      return -1;
    } else {
      if ( carte1/8 > carte2/8) {
        jeu1[n1] = carte2;
        jeu1[n1+1] = carte1;
        return 1;
      }
    }
  }
  jeu2[n2]=carte1;
  jeu2[n2+1] = carte2;
  return -1;
}

/*----------------------------------------------------------------*/
void decalerjeu (int j[], int n) {
  for (int i=1; i<n; i++) {
    j[i] = j[i-1];
  }
}
/*----------------------------------------------------------------*/
//programme principal
void setup() {

  int jeu[]=new int[NB_CARTES]; /*ensemble de toutes les cartes*/
  int jeu1[]=new int[NB_CARTES]; /* cartes du joueur 1 */
  int jeu2[]=new int[NB_CARTES]; /*cartes du joueur 2*/
  int n1=15; /* indice de la derniere carte joueur 1 */
  // nombre carte -1 du joueurs
  int n2=15; /* indice de la derniere carte joueur 2 */
  // nombre carte -1 du joueurs

  //remplissage du tableau jeu avec toutes les cartes
  initjeu(jeu);
  //mélange des cartes
  melange(jeu);
  //distribution des cartes dans les tableaux jeu1 et jeu2
  distribution(jeu, jeu1, jeu2);
  //affichage des deux jeux pour tester vos premières fonctions
  println("Jeu Du Joueur 1:");
  affiche(jeu1, 16);
  println("Jeu du Joueur 2:");
  affiche(jeu2, 16);
  /* tant que la partie n'est pas terminee */
  {
    //jouer un coup
      jouer1coup(jeu1, jeu2, n1, n2);
    //mettre à jour la taille des tas en fonction du vainqueur
  }
  /* afficher qui gagne et en combien de coups */
}
