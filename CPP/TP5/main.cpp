#include "Album.h"
#include <iostream>
#include <vector>
#include <string>

int main() {
    Album album("Mon Album Test");

    Image img1("chat_jardin.pgm", Date(1, 1, 2024), 100, 100, 255);
    img1.addMotCle("chat");
    img1.addMotCle("jardin");
    img1.addMotCle("animaux");

    Image img2("chat_maison.pgm", Date(2, 1, 2024), 100, 100, 255);
    img2.addMotCle("chat");
    img2.addMotCle("maison");
    img2.addMotCle("animaux");

    Image img3("chien_jardin.pgm", Date(3, 1, 2024), 100, 100, 255);
    img3.addMotCle("chien");
    img3.addMotCle("jardin");
    img3.addMotCle("animaux");

    Image img4("chat_jardin_animaux.pgm", Date(4, 1, 2024), 100, 100, 255);
    img4.addMotCle("chat");
    img4.addMotCle("jardin");
    img4.addMotCle("animaux");

    Image img5("voiture.pgm", Date(5, 1, 2024), 100, 100, 255);
    img5.addMotCle("voiture");
    img5.addMotCle("rue");

    album.ajouterImage(img1);
    album.ajouterImage(img2);
    album.ajouterImage(img3);
    album.ajouterImage(img4);
    album.ajouterImage(img5);

    std::cout << album << "\n";


    std::cout << "\n Test 1 : chat ET jardin\n";
    std::vector<std::string> mots1 = {"chat", "jardin"};
    std::vector<std::string> res1 = album.RequeteMultiple(mots1);


    std::cout << "Résultats (" << res1.size() << ") :\n";
    for (const auto& r : res1) {
        std::cout << " - " << r << "\n";
    }


    std::cout << "\n Test 2 : chat ET jardin ET animaux\n";
    std::vector<std::string> mots2 = {"chat", "jardin", "animaux"};
    std::vector<std::string> res2 = album.RequeteMultiple(mots2);

    std::cout << "Résultats (" << res2.size() << ") :\n";
    for (const auto& r : res2) {
        std::cout << " - " << r << "\n";
    }

    std::cout << "\n Test 3 : chat ET voiture\n";
    std::vector<std::string> mots3 = {"chat", "voiture"};
    std::vector<std::string> res3 = album.RequeteMultiple(mots3);

    std::cout << "Résultats (" << res3.size() << ") :\n";
    if (res3.empty()) {
        std::cout << " (aucun résultat)\n";
    }
    for (const auto& r : res3) {
        std::cout << " - " << r << "\n";
    }


    std::cout << "\n Test 4 : Saisie interactive\n";
    std::cout << "Combien de mots-clés ? ";
    int n;
    std::cin >> n;

    std::vector<std::string> motsUtilisateur(n);
    for (int i = 0; i < n; ++i) {
        std::cout << "Mot-clé " << (i+1) << " : ";
        std::cin >> motsUtilisateur[i];
    }

    std::vector<std::string> resUser = album.RequeteMultiple(motsUtilisateur);
    std::cout << "Résultats (" << resUser.size() << ") :\n";
    for (const auto& r : resUser) {
        std::cout << " - " << r << "\n";
    }

    return 0;
}