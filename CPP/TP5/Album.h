#ifndef ALBUM_H
#define ALBUM_H

#include <string>
#include <vector>
#include <iostream>
#include <filesystem>
#include <algorithm>
#include <iterator>
#include "Image.h"

class Album {
private:
    std::string nom;
    std::vector<Image> images;

public:

    Album();
    Album(const std::string& nom);


    std::string getNom() const { return nom; }
    int getNbImages() const    { return images.size(); }
    const std::vector<Image>& getImages() const { return images; }


    void setNom(const std::string& n) { nom = n; }


    void ajouterImage(const Image& img);
    bool supprimerImage(const std::string& nomImage);
    void parcourirRepertoire(const std::string& chemin);

    std::vector<std::string> RequeteMultiple(const std::vector<std::string>& motsCles) const;
    void afficherResultats(const std::vector<std::string>& resultats, const std::vector<std::string>::iterator& fin) const;


    std::vector<Image> rechercherParMotCle(const std::string& motCle) const;


    friend std::ostream& operator<<(std::ostream& os, const Album& a);
};

#endif