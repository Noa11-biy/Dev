package modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class MainClass {
    public static void main(String args[]){
        ArrayList<String> liste1 = new ArrayList<>();
        int cpt = 0;
        liste1.add("Bleu");
        liste1.add("Vert");
        liste1.add("Rouge");
        liste1.add("Violet");
        liste1.add("Orange");
        for(String objet : liste1) {
            cpt ++;
            System.out.println(objet);
        }
        System.out.println(cpt);
        liste1.add(0,"Noir");
        System.out.println(liste1);
        System.out.println(liste1.get(3));
        System.out.println(liste1.remove(2));
        System.out.println(liste1.contains("Blanc"));
        System.out.println(liste1.contains("Rouge"));
        Collections.swap(liste1, 0,3);
        System.out.println(liste1);

        ArrayList<String> liste2 = (ArrayList<String>) liste1.clone();
        System.out.println(liste2);
        liste1.clear();
        System.out.println(liste1);
        liste1.addAll(liste2);
        Collections.sort(liste1);
        System.out.println(liste1);
        Collections.shuffle(liste1);
        System.out.println(liste1);

        HashMap <String, Double> map = new HashMap<>();
        map.put("Algo", 11.9);
        map.put("Maths", 9.5);
        map.put("Anglais", 12.3);
        System.out.println(map);
        System.out.println(map.get("Maths"));
        System.out.println(map.keySet());
        System.out.println(map.values());
        for (String key : map.keySet()) {
            System.out.println(key+" "+ map.get(key));
        }



        }

    }

