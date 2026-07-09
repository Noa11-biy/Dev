////void setup() {
////  StringList liste;
////  liste = new StringList();
////  String[]file=loadStrings("villes.csv");
////  for (int i = 0; i<file.length; i++) {
////    String[]ville = splitTokens(file[i], ";");
////    liste.append(ville[1]);
////  }

////  //print(ville[1]," ");

////  liste.sort();
////  //print(liste.size());
////  //print(liste.get(59));
////  //liste.upper();

////  //String strVille=Clavier.lireString();
////  //strVille = strVille.toUpperCase();
////  //boolean StringValue = liste.hasValue(strVille);
////  //if ( StringValue){
////  //  println("dans la liste");
////  //}else{
////  //  println("pas dans la liste");
////  //}
////}

//// Exo 2

//PImage monImage;
//PImage Image = createImage(width, height, RGB);

//void settings() {
//  size(800, 600);
//}

//void setup() {
//  monImage =loadImage("imageTP11.jpg");
//  teinter(color(255, 120, 0), monImage);
//}

//void draw() {
//  image(monImage, 0, 0, width, height);
//}


//void keyPressed() {
//  if ( key == 'i') {
//    monImage.filter(INVERT);
//  }
//  if (key == 'b') {
//    monImage.filter(BLUR);
//  }
//  if (key == 's') {
//    Image = monImage.get();
//    Image.save("sauvegarde.png");
//  }
//}

//PImage teinter(color teinte, PImage source) {
//  PImage resultat = createImage(source.width, source.height, RGB);
//  float rougeCible = red(teinte);
//  float vertCible = green(teinte);
//  float bleuCible = blue(teinte);
//  source.loadPixels();
//  resultat.loadPixels();
//  for (int i = 0; i<source.pixels.length; i++) {
//    color c = source.pixels[i];
//    float r = red(c);
//    float v = green(c);
//    float b = blue(c);
//    float ar = map(r, 0, 255, 0, rougeCible);
//    float av = map(v, 0, 255, 0, vertCible);
//    float ab = map(b, 0, 255, 0, bleuCible);
//    resultat.pixels[i] = color(ar, av, ab);
//  }
//  resultat.updatePixels();
//  source.updatePixels();
//  return resultat;
//}

//Exo 3
Table table;
int row;

void setup() {
  table = loadTable("France.csv", "header, csv");
  row = table.getRowCount();
  print(row, " ");
  print(ano_m(0,1750));
}

float ano_m(int mois, int annee) {
  for (int i = 0; i<row; i++) {
    if (table.getInt(i, "Year") == annee && table.getInt(i,"Month") == mois){
      return table.getFloat(i, "M.Anomaly");
    }
  }
  return 0;
}
