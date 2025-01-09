#ifndef BOARD_H
#define BOARD_H
#include <vector>
#include "Tuile.h"

class Board
{
public:
    Board(int largeur, int hauteur);
    void displayGrille() const;
    bool peutPlacerTuile(const Tuile& tuile, int x, int y) const;
    bool PlacerTuile(const Tuile& tuile, int x, int y, char marqueur);
    int calculerTaillePlusGrandCarre(char marqueur) const;
    int compterCasesOccupees(char marqueur) const;
    virtual ~Board();

protected:

private:
    int largeur, hauteur;
    std::vector<std::vector<char>> grille;
};

#endif // BOARD_H
