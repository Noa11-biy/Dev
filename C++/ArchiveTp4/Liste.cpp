//
// Created by Noamo on 27/05/2026.
//

#include "Liste.h"

#include <fstream>

Liste::~Liste() {
    for (Item* item : liste) {
        delete item;
    }
    std::cout<<"[-]Liste"<<std::endl;
}

Liste::Liste(const Liste &l) {
   for (Item* item : l.liste) {
       liste.push_back(new Item(*item));
   }
}


void Liste::ajouterItem(std::string s, bool b, Date d) {
    Item* item = new Item(s, b, d);
    liste.push_back(item);
}

void Liste::supprimerItemIndex(unsigned int index) {
    auto it = liste.begin() + index;
    delete *it;
    liste.erase(it);
}

void Liste::supprimerItemNom(const std::string& s) {
    for (int i = 0; i< liste.size(); i++) {
        if (liste[i]-> getNom() == s) {
            delete liste[i];
            liste.erase(liste.begin()+i);
        }
    }
}

Liste& Liste::operator=(const Liste& l) {
    if (this != &l) {
        for (Item* it : liste) delete it;
        liste.clear();
        for (Item* it : l.liste)
            liste.push_back(new Item(*it));
    }
    return *this;
}



std::ostream& operator<<(std::ostream& os, const Liste& l) {
    for (Item* it : l.liste)
        os << *it << '\n';
    return os;
}

void Liste::sauverListe(const std::string &nomFichier) const {
    std::ofstream fichier(nomFichier);

    for (Item* item : liste) {
        fichier << item->getEstCoche() << " "
                << std::setfill('0')
                << std::setw(2) << item->getDate().getJour() << "/"
                << std::setw(2) << item->getDate().getMois() << "/"
                << std::setw(4) << item->getDate().getAn()   << " "
                << item->getNom() << "\n";
    }
    std::cout << "Save : " << nomFichier << std::endl;
}

void Liste::chargerListe(const std::string& nomFichier) {
    std::ifstream fichier(nomFichier);

    liste.clear();
    std::string ligne;
    while (std::getline(fichier, ligne)) {
        if (ligne.empty()) continue;

        std::istringstream iss(ligne);
        bool coche;
        std::string dateStr, nom;

        iss >> coche >> dateStr;
        std::getline(iss, nom);
        if (!nom.empty() && nom[0] == ' ')
            nom = nom.substr(1);

        int j = std::stoi(dateStr.substr(0, 2));
        int m = std::stoi(dateStr.substr(3, 2));
        int a = std::stoi(dateStr.substr(6, 4));

        liste.push_back(new Item(nom, coche, Date(j, m, a)));
    }
    std::cout << "Chargée depuis : " << nomFichier << std::endl;
}

Liste Liste::operator+(const Liste &l) const {
    Liste res(*this);
    res += l;
    return res;
}

Liste &Liste::operator+=(const Liste &l) {
    size_t n = l.liste.size();
    if (&l == this) {
        for (size_t i = 0; i<n; i++) {
            liste.push_back((new Item(*liste[i])));
        }
    } else {
        for (size_t i = 0; i <n; i++) {
            liste.push_back(new Item(*l.liste[i]));
        }
    }
    return *this;
}

Item *Liste::operator[](size_t index) const {
    if (index >= liste.size()) return nullptr;
    return liste[index];
}

bool Liste::contientNom(const std::string &nom) const {
    for (Item* it : liste) {
        if (it->getNom() == nom) return true;
    }
    return false;
}

Liste Liste::fusion(const Liste &a, const Liste &b) {
    Liste res(a);
    for (Item* it : b.liste) {
        if (!res.contientNom(it->getNom())) {
            res.liste.push_back(new Item(*it));
        }
    }
    return res;
}
