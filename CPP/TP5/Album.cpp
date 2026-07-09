#include "Album.h"
#include <filesystem>

namespace fs = std::filesystem;

Album::Album() : nom("Sans nom") {}

Album::Album(const std::string& nom) : nom(nom) {}

void Album::ajouterImage(const Image& img) {
    images.push_back(img);
}

bool Album::supprimerImage(const std::string& nomImage) {
    for (auto it = images.begin(); it != images.end(); ++it) {
        if (it->getNom() == nomImage) {
            images.erase(it);
            return true;
        }
    }
    return false;
}

std::vector<Image> Album::rechercherParMotCle(const std::string& motCle) const {
    std::vector<Image> resultats;
    for (const auto& img : images) {
        for (const auto& mc : img.getMotsCles()) {
            if (mc == motCle) {
                resultats.push_back(img);
                break;
            }
        }
    }
    return resultats;
}

std::ostream& operator<<(std::ostream& os, const Album& a) {
    os << "=== Album : " << a.nom << " ===\n";
    os << "Nombre d'images : " << a.images.size() << "\n";
    for (const auto& img : a.images) {
        os << "---\n" << img;
    }
    return os;
}

void Album::parcourirRepertoire(const std::string& chemin) {
    std::cout << "=== Parcours du repertoire : " << chemin << " ===\n";
    for (const auto& entry : fs::directory_iterator(chemin)) {
        const auto filenameStr = entry.path().filename().string();

        if (entry.is_regular_file()) {
            // On ne garde que les .pgm
            if (entry.path().extension() == ".pgm") {
                std::cout << "Image PGM trouvee : " << filenameStr << "\n";

                // Création d'une image (avec valeurs par défaut)
                Image img(filenameStr, Date(1, 1, 2024), 100, 100, 255);
                ajouterImage(img);
            }
        }
    }
}

void Album::afficherResultats(const std::vector<std::string> &resultats, const std::vector<std::string>::iterator &fin) const {
    std::cout << "=== Résultats de la requête ===\n";

    int count = 0;
    for (auto it = resultats.begin(); it != fin; ++it) {
        std::cout << " - " << *it << "\n";
        count++;
    }

    if (count == 0) {
        std::cout << " (aucun résultat)\n";
    } else {
        std::cout << "Total : " << count << " image(s)\n";
    }
}

std::vector<std::string> Album::RequeteMultiple(const std::vector<std::string> &motsCles) const {
    if (motsCles.empty()) {
        std::cout << "Aucun mot-clé fourni.\n";
        return {};
    }

    std::vector<Image> imagesTemp = rechercherParMotCle(motsCles[0]);

    std::vector<std::string> courant;
    for (const auto& img : imagesTemp) {
        courant.push_back(img.getNom());
    }

    std::sort(courant.begin(), courant.end());
    for (size_t i = 1; i < motsCles.size(); ++i) {


        std::vector<Image> imgSuivantes = rechercherParMotCle(motsCles[i]);

        std::vector<std::string> suivant;
        for (const auto& img : imgSuivantes) {
            suivant.push_back(img.getNom());
        }

        std::sort(suivant.begin(), suivant.end());


        std::vector<std::string> cr;
        cr.resize(std::min(courant.size(), suivant.size()));

        std::vector<std::string> crDynamique;
        auto finIter = std::set_intersection(
            courant.begin(), courant.end(),    // 1er ensemble
            suivant.begin(), suivant.end(),    // 2ème ensemble
            std::back_inserter(crDynamique)    // résultat (auto-dimensionné)
        );


        courant = crDynamique;
    }

    return courant;
}
