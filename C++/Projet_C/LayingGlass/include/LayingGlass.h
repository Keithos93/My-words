#ifndef LAYINGGLASS_H
#define LAYINGGLASS_H
#include "Board.h"
#include "Joueur.h"

class LayingGlass
{
public:
    LayingGlass();
    void setupGame();
    void startGame();
    std::vector<Tuile> fileTuiles;
    int couponsEchange;
    void afficherTuileActuelle(const Joueur& joueur);
    void game(); // Gère les 9 tours pour chaque joueur
    void interagirAvecTuile(Joueur& joueur);
    Tuile echangerTuile(Joueur& joueur, int index);
    void manipulerEtPlacerTuile(Joueur& joueur, Tuile& tuile);
    void determinerGagnant() const;

    virtual ~LayingGlass();


protected:

private:
    std::vector<Joueur> joueurs;
    Board* board;
    int nombreJoueurs;

    void afficherInfosJoueurs() const;
    int tailleGrille() const;
    void afficherTuiles();
    Tuile obtenirTuileCourante();
    Tuile echangerTuile(int index);
};

#endif // LAYINGGLASS_H
