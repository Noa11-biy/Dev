#include <iostream>
#include <vector>
#include <cmath>

// Petit calcul physique pour valider toute la chaîne :
// position d'un corps en chute libre, y(t) = y0 - 0.5 * g * t^2
double positionChuteLibre(double y0, double g, double t) {
    return y0 - 0.5 * g * t * t;
}

int main() {
    std::cout << "=== Test de configuration C++ ===" << std::endl;

    // Test 1 : le compilateur tourne
    std::cout << "Compilateur OK." << std::endl;

    // Test 2 : la STL fonctionne (vector)
    std::vector<int> v = {1, 2, 3, 4, 5};
    int somme = 0;
    for (int x : v) somme += x;
    std::cout << "Somme du vector {1,2,3,4,5} = " << somme
               << " (attendu : 15)" << std::endl;

    // Test 3 : un vrai petit calcul physique
    double y0 = 100.0; // hauteur initiale en metres
    double g = 9.81;   // acceleration gravitationnelle
    std::cout << "\nChute libre depuis y0 = " << y0 << " m :" << std::endl;
    for (double t = 0.0; t <= 4.0; t += 1.0) {
        double y = positionChuteLibre(y0, g, t);
        std::cout << "  t = " << t << " s -> y = " << y << " m" << std::endl;
    }

    std::cout << "\nSi tu vois ce message et les valeurs ci-dessus, "
                 "ta chaine de compilation C++ est prete." << std::endl;
    return 0;
}
