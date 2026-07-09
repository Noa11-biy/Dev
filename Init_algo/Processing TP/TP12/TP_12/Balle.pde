class Balle {

  float x; //position x
  float y; // position y
  int rayon;// rayon du disque
  color couleur; // couleur de la balle
  float dx; // vecteur de déplacement en x
  float dy;// vecteur de déplacement en y

  void initBalle() {
    rayon = int(random(10, 51));
    x = random(rayon, width-rayon);
    y = random(rayon, height-rayon);
    couleur = color(random(0, 256), random(0, 256), random(0, 256));
    dx = random(-6, 6);
    dy = random(-6, 6);
    float mag = sqrt(dx*dx + dy*dy);
    if (mag !=0) {
      dx /= mag;
      dy /= mag;
    }
  }
  void affiche(){
    noStroke();
    fill(couleur);
    ellipse( x,y, rayon, rayon);
  }
  void deplace(int vitesse){
    x += vitesse*dx;
    y += vitesse*dy;
  }
  
  void verifieRebond(){
    if (x-rayon/2 <= 0 || x+rayon/2>=width){
      dx = -dx;
    }
    if (y-rayon/2 <= 0 || y+rayon/2>=height){
      dy = -dy;
    }
  }
}
