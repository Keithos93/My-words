#include <iostream>
#include "Joueur.h"
#include<string>
#include "Board.h"
#include "Tuile.h"
#include "LayingGlass.h"

using namespace std;

int main()
{
    LayingGlass jeu;

    // Configuration des joueurs et du plateau
    jeu.setupGame();
    jeu.startGame();

    // Initialisation des tuiles
    jeu.fileTuiles = Tuile::genererTuiles();

    // Lancer la partie
    jeu.game();




    /*std::vector<Tuile> tuiles = Tuile::genererTuiles();

    // Afficher chaque tuile
    for (const auto& tuile : tuiles) {
        tuile.display(); // Assure-toi que la méthode display est bien définie
    }*/
    return 0;
}
