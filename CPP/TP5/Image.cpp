//
// Created by Noamo on 03/06/2026.
//

#include "Image.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <vector>

Image::Image()
: nom(""), date(), taille(0), width(0), height(0), maxVal(255), data(nullptr) {}

Image::~Image() {
    if (data != nullptr) {
        for (int i = 0; i < height; ++i) {
            delete[] data[i];
        }
        delete[] data;
        data = nullptr;
    }
}

Image::Image(const Image& other)
    : nom(other.nom), date(other.date), taille(other.taille),
      width(other.width), height(other.height), maxVal(other.maxVal),
        data(nullptr),
      motsCles(other.motsCles)
{
    if (other.data != nullptr) {
        data = new int*[height];
        for (int i = 0; i < height; ++i) {
            data[i] = new int[width];
            for (int j = 0; j < width; ++j) {
                data[i][j] = other.data[i][j];
            }
        }
    }
}

Image::Image(const std::string &nom, const Date &date, int taille, int width, int height)
: nom(nom), date(date), taille(taille), width(width), height(height),maxVal(255), data(nullptr){}

void Image::addMotCle(const std::string& mot) {
    motsCles.push_back(mot);
}


std::ostream &operator<<(std::ostream &os, const Image &img) {
    os << "Image : " << img.nom << '\n'
    << " Date : " << img.date << '\n'
    << "Taille  : " << (img.taille / 1024.0) << " Ko\n"
    << "Largeur : " << img.width << '\n'
    << "Hauteur : " << img.height << '\n'
    << "MaxVal : " << img.maxVal << '\n'
    << "Mot-Cles :";

    if (img.motsCles.empty()) {
        os << "(aucun)";
    } else {
        for (size_t i = 0; i < img.motsCles.size(); ++i) {
            os << img.motsCles[i];
            if (i + 1 < img.motsCles.size()) os << ", ";
        }
    }
    os << "\n";
    return os;
}

Image& Image::operator=(const Image& other) {
    if (this == &other) return *this;


    if (data != nullptr) {
        for (int i = 0; i < height; ++i) delete[] data[i];
        delete[] data;
        data = nullptr;
    }


    nom      = other.nom;
    date     = other.date;
    taille   = other.taille;
    width    = other.width;
    height   = other.height;
    maxVal = other.maxVal;
    motsCles = other.motsCles;


    if (other.data != nullptr) {
        data = new int*[height];
        for (int i = 0; i < height; ++i) {
            data[i] = new int[width];
            for (int j = 0; j < width; ++j) {
                data[i][j] = other.data[i][j];
            }
        }
    }

    return *this;
}

bool Image::loadPPM(const std::string &filename) {
    std::ifstream file(filename);
    if (!file.is_open()) {
        std::cerr << "Erreur : impossible d'ouvrir" << filename << std::endl;
        return false;
    }

    std::string magic;
    file >> magic;
    if (magic != "P2") {
        std::cerr << "Erreur : Format incorrecte (P2 attendu)" << filename << std::endl;
        return false;
    }

    auto skipComments = [&]() {
        char c;
        file >> std::ws;
        while (file.peek() == '#') {
            file.get(c);
            while (file.get(c) && c != '\n');
            file >> std::ws;
        }
    };

    skipComments();
    file >> width;
    skipComments();
    file >> height;
    skipComments();
    file >> maxVal;

    if (data != nullptr) {
        for (int i = 0; i < height; ++i) delete[] data[i];
        delete[] data;
    }

    data = new int*[height];
    for (int i = 0; i < height; ++i) {
        data[i] = new int[width];
    }

    for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
            file >> data[i][j];
        }
    }

    file.close();

    taille = (width * height * sizeof(int)) / 1024;

    nom = filename;

    return true;

}

void Image::Histogramme() const {
    if (data == nullptr) {
        std::cout << "Image vide.\n";
        return;
    }

    std::vector<int> histo(256, 0);

    for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
            int val = data[i][j];
            if (val >= 0 && val < 256) {
                histo[val]++;
            }
        }
    }

    // Trouver le max pour normaliser
    int maxCount = 0;
    for (int v = 0; v < 256; ++v) {
        if (histo[v] > maxCount) maxCount = histo[v];
    }

    const int LARGEUR_MAX = 60; // nb max d'étoiles par ligne

    std::cout << "=== Histogramme de " << nom << " ===\n";
    for (int v = 0; v < 256; ++v) {
        if (histo[v] > 0) {
            // Normalisation : valeur entre 0 et LARGEUR_MAX
            int nbStars = (histo[v] * LARGEUR_MAX) / maxCount;
            if (nbStars == 0) nbStars = 1; // au moins une étoile si présent

            std::cout << " " << v << " (" << histo[v] << ") : ";
            for (int k = 0; k < nbStars; ++k) std::cout << "*";
            std::cout << "\n";
        }
    }
}

void Image::draw() const {
    if (data == nullptr) {
        std::cout << "Image vide.\n";
        return;
    }

    std::string asciiChars = " `.-':_,^=;><+!rc*/z?sLTv)J7(|Fi{C}fI31tlu[neoZ5Yxjya]2ESwqkP6h9d4VpOGbUAKXHm8RD#$Bg0MNWQ%&@";
    int nbChars = asciiChars.size();

    // Largeur max du terminal (en caractères)
    const int LARGEUR_MAX = 120;

    // Calcul du pas de sous-échantillonnage
    // On multiplie par 2 car on affiche 2 chars par pixel (correction ratio)
    int stepX = std::max(1, (width * 2) / LARGEUR_MAX);
    // Les caractères sont ~2x plus hauts que larges → on saute plus en hauteur
    int stepY = stepX * 2;

    std::cout << "=== ASCII Art de " << nom
              << " (echantillonnage 1/" << stepX << ") ===\n";

    for (int i = 0; i < height; i += stepY) {
        for (int j = 0; j < width; j += stepX) {
            int pixel = data[i][j];
            int idx = (pixel * (nbChars - 1)) / maxVal;

            if (idx < 0) idx = 0;
            if (idx >= nbChars) idx = nbChars - 1;

            std::cout << asciiChars[idx] << asciiChars[idx]; // 2x pour ratio
        }
        std::cout << "\n";
    }
}