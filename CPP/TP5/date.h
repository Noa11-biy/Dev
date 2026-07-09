#ifndef DATE_H
#define DATE_H
#include <iostream>
#include <iomanip>
#include <ostream>

//typedef enum {lundi=1, mardi, mercredi, jeudi, vendredi, samedi, dimanche} Jour;
//typedef enum {janvier=1, fevrier, mars, avril, mai, juin, juillet, aout, septembre, octobre, novembre, decembre} Mois;

class Date
{

private:
    int j, m, a;
    static int nbJours[13];
public:
    Date(int j=1, int m=1, int a=2023);
    Date(const Date& d);
    Date& operator=(const Date& d);
    ~Date();

    int getJour() const;
    void setJour(int newJ);
    int getMois() const;
    void setMois(int newM);
    int getAn() const;
    void setAn(int newA);

    bool bissextile() const;
    void incremente();
    Date& operator++();    // prefix ++
    Date operator++(int);  // int, parametre utile seulement pour distinction pre/postfix

    friend bool operator< (const Date& d1, const Date& d2);
    friend bool operator> (const Date& d1, const Date& d2);
    friend bool operator<=(const Date& d1, const Date& d2);
    friend bool operator>=(const Date& d1, const Date& d2);
    friend bool operator==(const Date& d1, const Date& d2);
    friend bool operator!=(const Date& d1, const Date& d2);

    friend std::ostream& operator<<(std::ostream& os, const Date& d);

};

#endif // DATE_H
