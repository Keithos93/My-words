#include "Joueur.h"
#include <iostream>
#include <vector>
#include <string>
#include <utility>

using namespace std;

int Joueur::compteurJoueur = 0;

Joueur::Joueur(const string& joueurNom, const string& joueurColor) : name(joueurNom), color(joueurColor), id(++compteurJoueur)
{
    score = 0;
    couponsEchange = 1;
}

Joueur::Joueur() : name(""), color(""), id(0), score(0), couponsEchange(0) {}


void Joueur::infoJoueur() const
{
    cout << "Nom: " << name << ", Couleur: " << color << ", coupons d'echanges: " << couponsEchange << endl;
}

string Joueur::getName() const { return name; }

string Joueur::getColor() const { return color; }

int Joueur::getScore() const { return score; }

int Joueur::getId() const { return id; }

int Joueur::getCouponsEchange() const { return couponsEchange;}

void Joueur::addScore(int points)
{
    score+= points;
}

void Joueur::addCases(int x, int y)
{
    cases.push_back({x, y});
}

bool Joueur::utiliserCoupons() // Vérifie si des coupons sont disponible
{
    if (couponsEchange > 0){
        --couponsEchange;
        return true;
    }
    return false;
}

const vector<pair<int, int>>& Joueur::getPositions() const
{
    return cases;
}

Joueur::~Joueur()
{
    //dtor
}
