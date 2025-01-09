#ifndef JOUEUR_H
#define JOUEUR_H
#include <string>
#include <vector>
#include <utility>

class Joueur
{
    private:
        std::string name;
        std::string color;
        int id;
        static int compteurJoueur;
        int score;
        int couponsEchange; // Nombre de coupons.
        std::vector<std::pair<int, int>> cases; // Positions des cases qu'un joueur possède.

    public:
        Joueur(const std::string& joueurNom, const std::string& joueurColor);
        Joueur(); // Constructeur par défaut
        virtual ~Joueur();
        std::string getName() const;
        std::string getColor() const;
        int getScore() const;
        int getCouponsEchange() const;
        int getId() const;
        void addScore(int points);
        void addCases(int x, int y);
        void infoJoueur() const;
        bool utiliserCoupons();
        const std::vector<std::pair<int, int>>& getPositions() const;


};

#endif // JOUEUR_H
