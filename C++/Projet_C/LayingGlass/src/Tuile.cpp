#include "Tuile.h"
#include <vector>
#include <iostream>
#include <algorithm>

using namespace std;

Tuile::Tuile(int id, vector<vector<int>> shape, int owner) : id(id), shape(shape), owner(owner), position({-1, -1}), orientation("normal")
{
    taille = {1, 1};
}

void Tuile::display(bool inlineDisplay) const {
    if (inlineDisplay) {
        // Affichage sur une seule ligne
        for (const auto& row : shape) {
            for (int cell : row) {
                std::cout << (cell ? "a" : ".") << " "; // Affiche "a" pour 1 et "." pour 0
            }
        }
        std::cout << "  "; // Séparation entre les tuiles
    } else {
        // Affichage standard avec sauts de ligne
        std::cout << "Tile ID: " << id << std::endl;
        for (const auto& row : shape) {
            for (int cell : row) {
                std::cout << (cell ? "a" : ".") << " ";
            }
            std::cout << std::endl;
        }
        std::cout << std::endl;
    }
}


void Tuile::rotation()
{
    int ligne = shape.size();
    int colonne = shape[0].size();

    vector<vector<int>> rotated(colonne, vector<int>(ligne));

    for (int i=0; i < ligne; ++i)
    {
        for (int j=0; j < colonne; ++j)
        {
            rotated[j][colonne - 1 - i] = shape[i][j];
        }
    }

    shape = rotated;
    taille = {colonne, ligne};
    orientation = "rotated";
}

Tuile Tuile::creerTuileDepart(int id, int owner)
{
    std::vector<std::vector<int>> shape = {{1}}; // Tuile 1x1
    return Tuile(id, shape, owner);
}


const vector<vector<int>>& Tuile::getShape() const {
    return shape;
}

void Tuile::retourner() {
    // Parcours chaque ligne de la matrice et inverse l'ordre des éléments (symétrie horizontale)
    for (auto& row : shape) {
        reverse(row.begin(), row.end());
    }

    // Met à jour l'orientation
    orientation = (orientation == "normal") ? "flipped" : "normal";
}


vector<Tuile> Tuile::genererTuiles() {
    vector<Tuile> tuiles;

    tuiles.emplace_back(1, std::vector<std::vector<int>>{
        {1, 0, 0},
        {1, 1, 1}
    });

    tuiles.emplace_back(2, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1}
    });

    tuiles.emplace_back(3, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1},
        {0, 1, 0}
    });

    tuiles.emplace_back(4, std::vector<std::vector<int>>{
        {0, 0, 1},
        {1, 1, 1},
        {1, 0, 0}
    });

    tuiles.emplace_back(5, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1}
    });

    tuiles.emplace_back(6, std::vector<std::vector<int>>{
        {1, 1},
        {1, 1}
    });

    tuiles.emplace_back(7, std::vector<std::vector<int>>{
        {1, 0, 1},
        {1, 1, 1}
    });

    tuiles.emplace_back(8, std::vector<std::vector<int>>{
        {1, 1, 1}
    });

    tuiles.emplace_back(9, std::vector<std::vector<int>>{
        {0, 1},
        {1, 1},
        {1, 0}
    });

    tuiles.emplace_back(10, std::vector<std::vector<int>>{
        {1, 0},
        {1, 1}
    });

    tuiles.emplace_back(11, std::vector<std::vector<int>>{
        {0, 0, 1},
        {0, 1, 1},
        {1, 1, 0}
    });

    tuiles.emplace_back(12, std::vector<std::vector<int>>{
        {1, 1}
    });

    tuiles.emplace_back(13, std::vector<std::vector<int>>{
        {0, 1},
        {1, 1},
        {1, 0},
        {1, 0},
        {1, 1}
    });

    tuiles.emplace_back(14, std::vector<std::vector<int>>{
        {1, 1, 1},
        {1, 0, 0},
        {1, 0, 0},
        {1, 0, 0},
        {1, 1, 0}
    });

    tuiles.emplace_back(15, std::vector<std::vector<int>>{
        {0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 1, 1},
        {0, 1, 1, 1, 1, 0},
        {1, 1, 0, 0, 0, 0}
    });

    tuiles.emplace_back(16, std::vector<std::vector<int>>{
        {0, 0, 1},
        {0, 1, 1},
        {1, 1, 0},
        {1, 0, 0}
    });

    tuiles.emplace_back(17, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1},
        {1, 1, 0}
    });

    tuiles.emplace_back(18, std::vector<std::vector<int>>{
        {1, 1, 1}
    });

    tuiles.emplace_back(19, std::vector<std::vector<int>>{
        {1, 0, 0},
        {1, 1, 1}
    });

    tuiles.emplace_back(20, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1}
    });

    tuiles.emplace_back(23, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1},
        {0, 1, 0}
    });

    tuiles.emplace_back(24, std::vector<std::vector<int>>{
        {0, 0, 1},
        {1, 1, 1},
        {1, 0, 0}
    });

    tuiles.emplace_back(25, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1}
    });

    tuiles.emplace_back(26, std::vector<std::vector<int>>{
        {1, 1},
        {1, 1}
    });

    tuiles.emplace_back(27, std::vector<std::vector<int>>{
        {1, 0, 1},
        {1, 1, 1}
    });

    tuiles.emplace_back(28, std::vector<std::vector<int>>{
        {1, 1, 1}
    });

    tuiles.emplace_back(29, std::vector<std::vector<int>>{
        {0, 1},
        {1, 1},
        {1, 0}
    });

    tuiles.emplace_back(30, std::vector<std::vector<int>>{
        {1, 0},
        {1, 1}
    });

    tuiles.emplace_back(21, std::vector<std::vector<int>>{
        {0, 0, 1},
        {0, 1, 1},
        {1, 1, 0}
    });

    tuiles.emplace_back(22, std::vector<std::vector<int>>{
        {1, 1}
    });

    tuiles.emplace_back(31, std::vector<std::vector<int>>{
        {0, 1},
        {1, 1},
        {1, 0},
        {1, 0},
        {1, 1}
    });

    tuiles.emplace_back(32, std::vector<std::vector<int>>{
        {1, 1, 1},
        {1, 0, 0},
        {1, 0, 0},
        {1, 0, 0},
        {1, 1, 0}
    });

    tuiles.emplace_back(33, std::vector<std::vector<int>>{
        {0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 1, 1},
        {0, 1, 1, 1, 1, 0},
        {1, 1, 0, 0, 0, 0}
    });

    tuiles.emplace_back(34, std::vector<std::vector<int>>{
        {0, 0, 1},
        {0, 1, 1},
        {1, 1, 0},
        {1, 0, 0}
    });

    tuiles.emplace_back(35, std::vector<std::vector<int>>{
        {0, 1, 0},
        {1, 1, 1},
        {1, 1, 0}
    });

    tuiles.emplace_back(36, std::vector<std::vector<int>>{
        {1, 1, 1}
    });

    return tuiles;
}

Tuile::~Tuile()
{
    //dtor
}
