//
// Created by Noamo on 27/05/2026.
//

#ifndef C___ITEM_H
#define C___ITEM_H
#include <string>

#include "date.h"


class Item {

    private:
    std::string nom;
    bool estCoche;
    Date date;

    public:
    Item(std::string nom, bool estCoche, Date date);
    Item(){nom = "Unknown";}
    ~Item();
    std::string getNom();
    bool getEstCoche();
    Date getDate();
    void setNom(std::string nom);
    void setEstCoche(bool estCoche);
    void setDate(Date date);

};

std::ostream& operator<<(std::ostream& os, Item& i);


#endif //C___ITEM_H
