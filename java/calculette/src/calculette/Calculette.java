package calculette;

public class Calculette {
    public int addition(int a, int b) {
        return a + b;
    }

    // On teste directement la classe dans un 'main' ici :
    public static void main(String[] args) {
        Calculette calc = new Calculette();

        System.out.println("Bienvenue dans la calculette!");
        System.out.println("Que fait 5+8 ?");
        System.out.println("Résultat : " + calc.addition(5,8));
    }
}