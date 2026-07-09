public abstract class Figure {
    protected String nom;

    abstract boolean estSymetrique();

    public Figure(String nom){
        this.nom = nom;
    }
}
