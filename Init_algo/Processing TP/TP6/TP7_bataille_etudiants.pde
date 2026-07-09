final int NB_CARTES = 32;

/*----------------------------------------------------------------*/
void initjeu(int jeu[]) /* remplit le tableau avec les 32 cartes */
{
  
}
/*----------------------------------------------------------------*/
void permute(int jeu[], int carte1, int carte2) /* permute 2 cartes dans le jeu */
{
 
}
/*----------------------------------------------------------------*/
void melange(int jeu[]) /* melange les cartes */
{  
  
}
/*----------------------------------------------------------------*/
void affiche_carte(int x) /* affiche une carte (valeur,couleur)*/
{
  
}
/*----------------------------------------------------------------*/
void affiche(int jeu[],int n) /* affiche n cartes, ici n est le nombre de cartes de jeu[] */
{
  
}
/*----------------------------------------------------------------*/
void distribution(int jeu[], int jeu1[], int jeu2[])/* distribue les cartes aux deux joueurs */
{  
  
}
/*----------------------------------------------------------------*/
//version sans bataille
int jouer1coup( int jeu1[], int jeu2[], int n1, int n2) // retourne le nombre de cartes gagnées/perdues par le joueur 1
{
  return 0; //à modifier en fonction du vainqueur du coup
}

/*----------------------------------------------------------------*/
//programme principal
void setup(){
  
  int jeu[]=new int[NB_CARTES]; /*ensemble de toutes les cartes*/
  int jeu1[]=new int[NB_CARTES]; /* cartes du joueur 1 */
  int jeu2[]=new int[NB_CARTES]; /*cartes du joueur 2*/
  int n1=15; /* indice de la derniere carte joueur 1 */
  int n2=15; /* indice de la derniere carte joueur 2 */
  
  
  //remplissage du tableau jeu avec toutes les cartes
  
  //mélange des cartes
  
  //distribution des cartes dans les tableaux jeu1 et jeu2
  
  //affichage des deux jeux pour tester vos premières fonctions
  
  /* tant que la partie n'est pas terminee */
  {
    //jouer un coup
    
    //mettre à jour la taille des tas en fonction du vainqueur

  }
  /* afficher qui gagne et en combien de coups */

}
