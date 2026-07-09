public class Guerrier extends Personnage{

    private int armure;

    public Guerrier(String nom, int pvMax, int armure) {
        super(nom, pvMax);
        this.armure = armure;
    }

    public int getArmure(){
        return this.armure;
    }

    public void utiliserBouclier(){
        System.out.println(getNom()+"bouclier");
        setPv(37);
        defense = 37;
        System.out.println(defense);
    }

    @Override
    public void attaquer(Personnage cible){
        System.out.println(getNom() + "attaque");
        cible.recevoirDegats(67);
    }
}
