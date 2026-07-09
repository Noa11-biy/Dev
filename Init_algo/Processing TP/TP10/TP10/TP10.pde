int N;
int laby[][];

void setup() {
  initLabyrinthe(4);
  initSerpentLabyrinthefdp(4);
  //afficheLab(laby);
  labyrintheAmateur(4);
  //afficheLab(laby);
}

void draw() {
}


void initLabyrinthe(int taille) {
  N = taille;
  laby = new int[2*N+1][2*N+1];
  for (int i = 0; i<laby.length; i++) {
    for (int j = 0; j<laby[0].length; j++) {
      if (i==0 || i==2*N || j==0 || j==2*N) {
        laby[i][j]=1;
      } else if ( i%2!=0 && j%2!=0) {
        laby[i][j] = 0;
      } else {
        laby[i][j] = 1;
      }
      //print(laby[i][j], " ");
    }
    //println();
  }
}

void initSerpentLabyrinthefdp(int taille) {
  for (int i = 0; i<laby.length; i++) {
    for (int j = 0; j<laby[0].length; j++) {
      if (i==0 || i==2*N || j==0 || j==2*N) {
        laby[i][j] = 1;
      } else if (i%2 == 0 && j%2 !=0 || i == 1 && j%4 == 0 || i == 2*N-1 && j%4 == 2 ) {
        laby[i][j] = 0;
      }
      //print(laby[i][j], " ");
    }
    //println();
  }
}

void afficheLab(int t[][]) {
  for (int i = 0; i<t.length; i++) {
    for (int j = 0; j<t[0].length; j++) {
      if (t[i][j] == 1) {
        print("#");
      } else if (i%2 == 0 && j%2 !=0 || j%2==0 && i%2 !=0) {
        print("/");
      } else {
        print(" ");
      }
      print(" ");
    }
    println();
  }
}

void labyrintheAmateur(int taille) {
  for (int i = 0; i<laby.length; i++) {
    for (int j = 0; j<laby[0].length; j++) {
      if (i==0 || i==2*N|| j==0 || j==2*N) {
        print("#");
      } else {
        print(" ");
      }
      print(" ");
    }
    println();
  }
}
