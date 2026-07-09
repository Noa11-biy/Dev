#include <iostream>
#include <string>
#include "date.h"
#include "Item.h"
#include "Liste.h"

const std::string FICHIER_DEFAUT = "listeDB.txt";

void afficherAide(const std::string& prog) {
    std::cout << "Usage :\n"
              << "  " << prog << " --add \"<description>\" <true|false> <jj/mm/aaaa>\n"
              << "  " << prog << " --list\n"
              << "  " << prog << " --del <index>\n"
              << "  " << prog << " --help\n";
}

bool parseBool(const std::string& s) {
    // On accepte "true","1","yes" → true
    // Tout le reste                → false
    return (s == "true" || s == "1" || s == "yes");
}

Date parseDate(const std::string& s) {
    // Format attendu : jj/mm/aaaa  (exactement 10 caractères)
    if (s.size() != 10 || s[2] != '/' || s[5] != '/') {
        throw std::invalid_argument(
            "Format de date invalide : \"" + s + "\"  (attendu jj/mm/aaaa)");
    }

    int j = std::stoi(s.substr(0, 2));
    int m = std::stoi(s.substr(3, 2));
    int a = std::stoi(s.substr(6, 4));

    return Date(j, m, a);
}

int cmdAdd(int argc, char* argv[], Liste& liste) {
    // Vérification du nombre d'arguments
    if (argc != 5) {
        std::cerr << "[ERREUR] --add attend exactement 3 arguments :\n"
                  << "         --add \"<description>\" <true|false> <jj/mm/aaaa>\n";
        return 1;   // code d'erreur
    }

    std::string description = argv[2];
    bool        coche       = parseBool(argv[3]);
    Date        date;

    // Parsing de la date avec gestion d'erreur
    try {
        date = parseDate(argv[4]);
    } catch (const std::invalid_argument& e) {
        std::cerr << "[ERREUR] " << e.what() << '\n';
        return 1;
    }

    liste.ajouterItem(description, coche, date);

    std::cout << "[+] Tâche ajoutée : \"" << description << "\"\n";
    return 0;
}

int cmdList(const Liste& liste) {
    // Vérifie si la liste est vide via l'opérateur []
    if (liste[0] == nullptr) {
        std::cout << "(liste vide)\n";
        return 0;
    }

    std::cout << "─────────────────────────────────────────\n";

    // Parcours par index grâce à operator[]
    size_t index = 0;
    while (Item* it = liste[index]) {
        // Affichage : index | [X] ou [ ] | date | description
        std::cout << "[" << index << "] "
                  << (it->getEstCoche() ? "[X]" : "[ ]")
                  << " | "
                  << std::setfill('0')
                  << std::setw(2) << it->getDate().getJour() << "/"
                  << std::setw(2) << it->getDate().getMois() << "/"
                  << std::setw(4) << it->getDate().getAn()
                  << " | "
                  << it->getNom()
                  << '\n';
        ++index;
    }

    std::cout << "─────────────────────────────────────────\n";
    std::cout << index << " tâche(s) au total.\n";
    return 0;
}

int cmdDel(int argc, char* argv[], Liste& liste) {
    if (argc != 3) {
        std::cerr << "[ERREUR] --del attend exactement 1 argument : --del <index>\n";
        return 1;
    }

    // Conversion de l'index avec gestion d'erreur
    int index = -1;
    try {
        index = std::stoi(argv[2]);
    } catch (...) {
        std::cerr << "[ERREUR] L'index \"" << argv[2] << "\" n'est pas un entier valide.\n";
        return 1;
    }

    // Vérification que l'index est positif et pointe sur un item existant
    if (index < 0 || liste[static_cast<size_t>(index)] == nullptr) {
        std::cerr << "[ERREUR] Index " << index << " hors limites.\n";
        return 1;
    }

    // On affiche ce qu'on supprime pour confirmer
    std::cout << "[-] Suppression de la tâche [" << index << "] : \""
              << liste[static_cast<size_t>(index)]->getNom() << "\"\n";

    liste.supprimerItemIndex(static_cast<unsigned int>(index));
    return 0;
}

int main(int argc, char* argv[]) {
    Liste liste;
    liste.chargerListe(FICHIER_DEFAUT);

    if (argc < 2) {
        afficherAide(argv[0]);
        // On sauvegarde quand même (cas première exécution)
        liste.sauverListe(FICHIER_DEFAUT);
        return 0;
    }

    std::string commande = argv[1];
    int codeRetour = 0;

    if (commande == "--add") {
        codeRetour = cmdAdd(argc, argv, liste);

    } else if (commande == "--list") {
        codeRetour = cmdList(liste);

    } else if (commande == "--del") {
        codeRetour = cmdDel(argc, argv, liste);

    } else if (commande == "--help") {
        afficherAide(argv[0]);

    } else {
        // Commande inconnue
        std::cerr << "[ERREUR] Commande inconnue : \"" << commande << "\"\n";
        afficherAide(argv[0]);
        codeRetour = 1;
    }

    liste.sauverListe(FICHIER_DEFAUT);

    return codeRetour;
}
