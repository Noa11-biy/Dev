//
// Created by Noamo on 27/05/2026.
//

#ifndef C___LISTE_H
#define C___LISTE_H
#include <vector>

#include "Item.h"


class Liste {
private:
    std::vector<Item*> liste;
public:
    Liste(){;}
    Liste(const Liste& l);
    ~Liste();

    int size(){ return liste.size();}


    void ajouterItem(std::string s, bool b, Date d);
    void supprimerItemIndex(unsigned int index);
    void supprimerItemNom(const std::string& s);
    void sauverListe  (const std::string& nomFichier) const;
    void chargerListe (const std::string& nomFichier);
    bool contientNom(const std::string& nom) const;

    friend std::ostream& operator<<(std::ostream& os, const Liste& l);
    Liste& operator=(const Liste& l);
    Liste& operator+=(const Liste& l);
    Liste operator+(const Liste& l) const;
    Item* operator[](size_t index) const;

    static Liste fusion(const Liste& a, const Liste& b);

};

std::ostream &operator<<(std::ostream &os, const Liste &l);


#endif //C___LISTE_H
