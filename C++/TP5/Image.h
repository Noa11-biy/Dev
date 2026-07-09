//
// Created by Noamo on 03/06/2026.
//

#ifndef TP5_IMAGE_H
#define TP5_IMAGE_H
#include <vector>
#include <fstream>

#include "date.h"


class Image {
private:
    std::string nom;
    Date date;
    int taille;
    int width;
    int height;
    int maxVal;
    int** data;
    std::vector<std::string> motsCles;
public:
    Image();
    Image(const std::string& nom, const Date& date, int taille, int width, int height);
    Image(const Image& other);

    ~Image();


    std::string getNom() const { return nom; }
    Date getDate() const { return date; }
    int getTaille() const { return taille; }
    int getWidth() const { return width; }
    int getHeight() const { return height; }
    int getMaxVal() const { return maxVal; }
    const std::vector<std::string>& getMotsCles() const { return motsCles; }


    void setNom(const std::string& n) { nom = n; }
    void setDate(const Date& d) { date = d; }
    void setTaille(int t) { taille = t; }
    void setWidth(int w) { width = w; }
    void setHeight(int h) { height = h; }

    void addMotCle(const std::string& mot);

    bool loadPPM(const std::string& filename);
    void Histogramme() const;
    void draw() const;
    friend std::ostream& operator<<(std::ostream& os, const Image& img);
    Image& operator=(const Image& other);
};


#endif //TP5_IMAGE_H
