//
// Created by Noamo on 27/05/2026.
//

#include "Item.h"

Item::Item(std::string nom, bool estCoche, Date date) : nom(nom), estCoche(estCoche), date(date) {}
Item::~Item() {}

std::string Item::getNom() {
   return nom;
}

bool Item::getEstCoche() {
   return estCoche;
}

Date Item::getDate() {
   return date;
}

void Item::setDate(Date date) {
   this->date = date;
}

void Item::setEstCoche(bool estCoche) {
   this->estCoche = estCoche;
}

void Item::setNom(std::string nom) {
   this->nom = nom;
}

std::ostream& operator<<(std::ostream& os, Item& i) {
   os << i.getEstCoche() << " - " << i.getNom() << " - " << i.getDate() << std::endl;
   return os;
}
