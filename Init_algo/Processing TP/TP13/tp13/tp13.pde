//int incr =0;

//void setup(){
//  //println(pgcd(124,28));
//  //println(pgcd(244,151));
//  println(pgcdMax(244));
//  //print (incr);
//  //println(244,55);
//}



//int pgcd(int a, int b){
//  incr =0;
//  if (b==0){
//    incr +=1;
//    return a;
//  }
//  incr +=1;
//  return pgcd(b,a%b);
//}

//int pgcdMax(int a){
//  int bMax = -1;
//  int Max = -1;
//  for (int i = 0; i<a;i++){
//    incr =0;
//    pgcd (a, i);
//    if (incr>Max){
//      Max=incr;
//      bMax=i;
//    }
//  }
//  return Max;
//}


//int t[]={21, -432, -77, 84, 91, 74, 45, 899, 301, 2, -1, -987, -456, 87, 745, 79, -4, -56, 754, 32, 145, 156, 54, -2, -89, 963, 71, -222, -333, 7};

//void tri() {
//  boolean perm = true;
//  while (perm) {
//    perm =false;
//    for (int i = 0; i<t.length-1; i++) {
//      if (t[i]>t[i+1]) {
//        int tmp =t[i];
//        t[i] = t[i+1];
//        t[i+1] =tmp;
//        perm =true;
//      }
//    }
//  }
//}


//int recherche_dicho(int t[], int v, int indice_debut, int indice_fin) {
//  if ( indice_fin<indice_debut) {
//    return -1;
//  }
//  int Val_Mid = (indice_debut+indice_fin)/2;
//  if (t[Val_Mid] == v) {
//    return Val_Mid;
//  } else if (v <t[Val_Mid]) {
//    return recherche_dicho(t, v, indice_debut, Val_Mid-1);
//  } else {
//    return  recherche_dicho(t, v, Val_Mid+1, indice_fin);
//  }
//}



//void setup() {
//  tri();
//  for (int i = 0; i<t.length; i++) {
//    println(t[i]);
//  }
//  print(recherche_dicho(t, -987, 0, 0));
//}

int orientationD =0;
int longueurTrait = 5;
float x;
float y;



String calculerMot(int n){
  if ( n == 0){
    return "D";
  }
  String Sm1 = calculerMot(n-1);
  return Sm1+ "D"+inverseMot(Sm1);
}

String inverseMot(String s){
  String resultat="";
  for(int i = 0;i<s.length();i++){
    if ( s.charAt(i)=='G'){
      resultat = "D"+resultat;
    }else{
      resultat ="G"+resultat;
    }
  }
  return resultat;
}

void dessinerCourbe(String mot){
  
}

void orientation(String lettre){
  if (lettre=="D"){
    orientationD++;
    if(orientationD>3){
      orientationD=0;
    }
    if (lettre=="G"){
      orientationD--;
      if(orientationD<0){
        orientationD=3;
      }
    }
  }
}

void setup(){
  print(calculerMot(3));
}

void settings(){
  size(800,800);
}
