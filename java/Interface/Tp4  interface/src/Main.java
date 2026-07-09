import java.util.ArrayList;

public class Main {
    public static void main(String args[]){

        ArrayList<Figure> liste = new ArrayList<>();

        liste.add(new Cercle("Cercle",12.7));
        liste.add(new Sphere("Sphere",7.8));
        liste.add(new Rectangle("Rectangle",11.1,22.2));
        liste.add(new Carre("Carre",9.9));
        liste.add(new Triangle("Triangle1",3.4,6.2,6.2));
        liste.add(new Parallelepipede("Parallelepipede",5.4,2.3,1.6));
        liste.add(new Triangle("Triangle2",3.4,6.2,4.1));


        System.out.println("Nombre de figures? "+liste.size());

        System.out.print("Nombre de figures avec un axe de symétrie ? ");
        int cpt=0;
        for(Figure f : liste) {
            if ( f.estSymetrique()){
                cpt++;
            }
        }

        System.out.println(cpt);

        System.out.print("Périmètre total de la scène ? ");
        double perimetreTotal=0.0;
        for(Figure f : liste) {
            if (f instanceof F2D) {
                perimetreTotal += ((F2D) f).perimetre();
            }
        }

        System.out.println(perimetreTotal);

        System.out.print("Volume total de la scène ? ");
        double volumeTotal=0.0;
        for(Figure f : liste) {
            if (f instanceof F3D) {
                volumeTotal += ((F3D) f).volume();
            }
        }

        System.out.println(volumeTotal);

        System.out.print("Surface totale de la scène ? ");
        double surfaceTotale=0.0;
        for(Figure f : liste) {
            if (f instanceof Surface) {
                surfaceTotale += ((Surface) f).calculeSurface();
            }
        }

        System.out.println(surfaceTotale);
    }
}