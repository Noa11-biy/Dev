//exemple 1 //<>//
/*
size(300,300); //dimensionne la fenêtre à la taille 300x300
 point(150,150); //place un point au milieu de la fenêtre
 */

//exemple 2
/*
size(300,300);
 for(int x=50;x<=250;x++){
 point(x,100);
 }
 */
//Exercice 1
/*
size(200, 200);
 background(255);
 for (int i=0; i<=2000; i++) {
 point((int)random(0, +200), (int)random(0, +200));
 }
 */

//Exercice 2
/*
size(300, 300);
 for (int i=1; i<=200; i++) {
 line(150,150,(int)random(0, 300),(int)random(0, 300));
 stroke((int)random(256),(int)random(256),(int)random(256));
 
 }
 */
//Exercice 3
/*
size(300,300);
 background(255);
 stroke(0,255);
 fill((int)random(256),(int)random(256),(int)random(256));
 triangle(150,150,150,0,0,0);
 fill((int)random(256),(int)random(256),(int)random(256));
 triangle(300,150,300,0,150,150);
 fill((int)random(256),(int)random(256),(int)random(256));
 triangle(300,300,150,300,150,150);
 fill((int)random(256),(int)random(256),(int)random(256));
 triangle(150,150,0,150,0,300);
 */

//Exercice 4
/*
size(200,200);
 fill(255,0,0);
 quad(100,100,0,0,100,50,200,0);
 fill(0,0,255);
 noStroke();
 quad(100,100,200,200,100,150,0,200);
 stroke(0);
 noFill();
 quad(100,75,150,100,100,125,50,100);
 */

//Exercice 5
/*
size(320, 320);
 int H = 40;
 int L= 40;
 int i;
 int j;
 for (i = 0; i<8; i++)
 {
 for (j= 0; j<8; j++)
 {
 if ((i+j)%2==1) {
 fill(0);
 } else {
 fill(255);
 }
 rect(i*L, j*H, H, L);
 }
 }
 */

//Exercice 6
/*
size(240,240);
 fill(0,255,0);
 ellipse(120,120,240,240);//vert
 fill(0,0,255);
 ellipse(120,120,180,180);//bleu
 fill(255,255,0);
 ellipse(120,120,120,120);//jaune
 fill(255,0,0);
 ellipse(120,120,60,60);//rouge
 */
//Exercice 7
size(300, 300);
fill(0,255,0);
arc(150,150,300,300,0,2*PI/20,PIE);
fill(255,255,0);
arc(150,150,(300-15),(300-15),0,2*PI/20,PIE);
