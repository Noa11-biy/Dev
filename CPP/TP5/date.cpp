#include "date.h"
using namespace std;

int Date::nbJours[13] = {0,31,28,31,30,31,30,31,31,30,31,30,31};

Date::Date(int j, int m, int a)
    : j(j), m(m), a(a)
{}

Date::~Date()
{
    ;
}

Date::Date(const Date& d)
    : j(d.j), m(d.m), a(d.a)
{}

Date& Date::operator=(const Date& d) {
    if (this != &d) {
        j=d.j;
        m=d.m;
        a=d.a;
    }

    return *this;
}

int Date::getJour() const {
    return j; }

void Date::setJour(int newJ){
    j = newJ; }

int Date::getMois() const {
    return m; }

void Date::setMois(int newM) {
    m = newM; }

int Date::getAn() const {
    return a; }

void Date::setAn(int newA) {
    a = newA; }

bool Date::bissextile() const {
    // tous les 4 ans, sauf tous les 100 ans, mais tous les 400 ans
    return ( ((a%4==0) && (a%100!=0)) || (a%400==0) );
}

void Date::incremente() {
    int inc=0;
    if (m==2 && bissextile()) inc=1;
    j+=1;
    if (j>(nbJours[m]+inc)){
        j=1;
        m+=1;
    }
    if (m>12) {
        m=1;
        a+=1;
    }
}

Date& Date::operator++() {
    incremente(); // reutilisation de incremente sur this
    return *this;
}

Date Date::operator++(int) {
    Date result(*this);      // copie
     ++(*this);              // prefix version
     return result;          // retourne la valeur (ancienne) de la copie.
}

bool operator< (const Date& d1, const Date& d2) {
    // compare les annees
    if (d1.a < d2.a)
        return true;
    else if (d1.a > d2.a)
        return false;

    // meme annee, compare les mois
    if (d1.m < d2.m)
        return true;
    else if (d1.m > d2.m)
        return false;

    // meme mois, compare les jours
    return(d1.j < d2.j);
}

bool operator> (const Date& d1, const Date& d2) { return d2 < d1; }
bool operator<=(const Date& d1, const Date& d2) { return !(d1 > d2); }
bool operator>=(const Date& d1, const Date& d2) { return !(d1 < d2); }

bool operator==(const Date& d1, const Date& d2) {
    return (d1.a==d2.a && d1.m==d2.m && d1.j==d2.j);
}
bool operator!=(const Date& lhs, const Date& rhs) { return !(lhs == rhs); }


std::ostream& operator<<(std::ostream& os, const Date& d) {
    os << std::setfill('0') << std::setw(2) << d.j  << "/" << std::setw(2) << d.m  << "/" << std::setw(4) << d.a ;
    return os;
}
