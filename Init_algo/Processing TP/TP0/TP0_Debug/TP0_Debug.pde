int a =Clavier.lireEntier("saisir un entier"); //<>//
int b =Clavier.lireEntier("saisir un entier");
int c =Clavier.lireEntier("saisir un entier");
int nbvraies = 0;

if((a<b)&&(b<c)) {
  println("Les nombres sont dans l'ordre croissant");
  nbvraies++;
}

else {
  println("Les nombres ne sont pas dans l'ordre croissant");
}
if ((a==b) && (b==c)) {
  println("Les nombres sont égaux");
  nbvraies++;
}

else {
  println("Les nombres ne sont pas égaux");
}
if ((a!=b) && (b!=c) && (c!=a)) {
  println("Les nombres sont distincts deux à deux");
  nbvraies++;
}

else {
  println ("Les nombres ne sont pas distincts deux à deux");
}
if ((a>b+c) || (b>a+c) || (c>a+b)) {
  println("L'un des trois nombres est plus grand que la somme des deux autres");
  nbvraies++;
}

else {
  println("L'un des trois nombres n'est pas plus grand que la somme des deux autres");
}
if ((a%2 !=0) && (b%2 !=0) && (c%2 !=0)) {
  println("Tous les nombres sont impairs");
  nbvraies++;
}

else {
  println("Aucun des nombres ne sont impairs");
}
println(nbvraies);
