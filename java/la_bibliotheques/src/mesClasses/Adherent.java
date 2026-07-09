package mesClasses;

import java.util.Objects;

public class Adherent {
    private String nom;
    private String prenom;
    private int age;
    private static int dernierId = 123456;
    private int id;


    public Adherent(int age, String nom, String prenom){
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
        this.id = ++dernierId;
    }


    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return nom.toUpperCase()+ " "+prenom+" "+age;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Adherent adherent)) return false;
        return age == adherent.age && Objects.equals(nom, adherent.nom) && Objects.equals(prenom, adherent.prenom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, age);
    }
}
