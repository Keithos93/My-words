#include "Board.h"
#include <vector>
#include <iostream>
#include <algorithm>


using namespace std;

Board::Board(int largeur, int hauteur) : largeur(largeur), hauteur(hauteur)
{
    grille.resize(hauteur, vector<char>(largeur, '.'));
}

void Board::displayGrille() const
{
    // afficher les lettres en haut
    cout << "  ";
    for (int x = 0; x < largeur; x++){
        cout << char('A' + x) << " ";
    }
    cout << endl;

    // afficher les lignes avec la lettre au début
    for (int y = 0; y < hauteur; y++){
        cout << char('A' + y) << " ";
        for (int x = 0; x< largeur; ++x){
            cout << grille[y][x] << " ";
        }
        cout << endl;
    }
}

bool Board::peutPlacerTuile(const Tuile& tuile, int x, int y) const
{
    int tuileHauteur = tuile.getShape().size();
    int tuileLargeur = tuile.getShape()[0].size();


    if (x < 0 || y < 0 || x + tuileLargeur > largeur || y + tuileHauteur > hauteur){
        return false;
    }

    for (int i = 0; i < tuileHauteur; ++i)
    {
        for (int j = 0; j < tuileLargeur; ++j)
        {
            if (tuile.getShape()[i][j] == 1 && grille[y + i][x + j] != '.'){
                return false;
            }
        }
    }

    return true;
}
bool Board::PlacerTuile(const Tuile& tuile, int x, int y, char marqueur) {
    if (!peutPlacerTuile(tuile, x, y)) {
        return false; // Placement invalide
    }

    // Place chaque case de la tuile sur la grille
    const auto& shape = tuile.getShape();
    for (size_t i = 0; i < shape.size(); ++i) {
        for (size_t j = 0; j < shape[i].size(); ++j) {
            if (shape[i][j] == 1) { // Si la case fait partie de la tuile
                grille[y + i][x + j] = marqueur; // Place la marque du joueur
            }
        }
    }

    return true; // Placement réussi
}

int Board::calculerTaillePlusGrandCarre(char marqueur) const {
    std::vector<std::vector<int>> dp(hauteur, std::vector<int>(largeur, 0));
    int maxTaille = 0;

    for (int i = 0; i < hauteur; ++i) {
        for (int j = 0; j < largeur; ++j) {
            if (grille[i][j] == marqueur) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 1; // Bord du plateau
                } else {
                    dp[i][j] = std::min({dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]}) + 1;
                }
                maxTaille = std::max(maxTaille, dp[i][j]);
            }
        }
    }

    return maxTaille;
}

int Board::compterCasesOccupees(char marqueur) const {
    int total = 0;
    for (const auto& ligne : grille) {
        for (char cell : ligne) {
            if (cell == marqueur) {
                ++total;
            }
        }
    }
    return total;
}


Board::~Board()
{
    //dtor
}
