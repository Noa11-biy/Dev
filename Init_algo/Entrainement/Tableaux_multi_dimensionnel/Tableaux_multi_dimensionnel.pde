//Variable globale
int taille_ligne = 10;
int taille_colone = 10;

void setup(){
  int t[][] = new int[taille_ligne][taille_colone];
  // Tableau Brut
  tableau2D(t);
  afficher(t);
  //Max du tableau
  maxTab2D(t);
  ind_maxTab2D(t);
  //Min du tableau
  minTab2D(t);
  ind_minTab2D(t);
}


// initialiser le tableaux
void tableau2D(int t[][]){
  for (int i = 0;i<t.length;i++){
    for (int j = 0;j<t[i].length;j++){
      t[i][j] = int(random(-200,201));
    }
  }
}


// afficher le tableaux
void afficher(int t[][]){
  for (int i = 0;i<t.length;i++){
    for (int j = 0;j<t[i].length;j++){
      println("t["+i+"]"+"["+j+"]",t[i][j]);
    }
  }  
}

// Récuperer le max
int maxTab2D(int t[][]){
  int max = t[0][0];
  for (int i = 0;i<t.length;i++){
    for ( int j = 0 ;j<t[i].length;j++){
      if (t[i][j]>max){
        max = t[i][j];
      }
    }
  }
  println("le max de cette matrice est :"+max);
  return max;
}

// Recuperer l'indice du max
int[] ind_maxTab2D(int t[][]){
  int max = t[0][0];
  int ind_ligne = 0;
  int ind_colone = 0;
  for(int i = 0;i<t.length;i++){
    for (int j = 0;j<t[i].length;j++){
      if (t[i][j]>max){
        max = t[i][j];
        ind_ligne = i;
        ind_colone = j;
      }
    }
  }
  println("l'indice du max de la ligne de la matrice est :"+ind_ligne,"l'indice du max de la cologne de la matrice est :"+ind_colone);
  int[] pos = {ind_ligne,ind_colone};
  return pos;
}

// Récuperer le min
int minTab2D(int t[][]){
  int min = t[0][0];
  for (int i = 0 ;i<t.length;i++){
    for(int j = 0;j<t[0].length;j++){
      if(t[i][j]<min){
        min = t[i][j];
      }
    }
  }
  println("le min de cette matrice est :"+min);
  return min;
}

//Recuperer l'indice du minimum
int[] ind_minTab2D(int t[][]){
  int min = t[0][0];
  int ind_ligne = 0;
  int ind_colone = 0;
  for(int i = 0;i<t.length;i++){
    for (int j = 0;j<t[i].length;j++){
      if (t[i][j]<min){
        min = t[i][j];
        ind_ligne = i;
        ind_colone = j;
      }
    }
  }
  println("l'indice du max de la ligne de la matrice est :"+ind_ligne,"l'indice du max de la cologne de la matrice est :"+ind_colone);
  int[] pos = {ind_ligne,ind_colone};
  return pos;
}
