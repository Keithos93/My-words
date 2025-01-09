#include "LayingGlass.h"
#include "Board.h"
#include "Joueur.h"
#include <string>
#include <iostream>

using namespace std;

LayingGlass::LayingGlass() : board(nullptr), nombreJoueurs(0)
{
    //ctor
}

void LayingGlass::setupGame()
{
    do
    {
        cout << "Entrez le nombre de joueurs pour cette partie (2-9): ";
        cin >>nombreJoueurs;
    }
    while (nombreJoueurs < 2 || nombreJoueurs > 9);

    for (int i=0; i < nombreJoueurs; ++i)
    {
        string nom, couleur;
        cout << "Entrez le nom du joueur" << i + 1 << " : ";
        cin >> nom;
        cout << "Entrez la couleur du joueur" << i + 1 << " : ";
        cin >> couleur;
        joueurs.emplace_back(nom, couleur);

    }

}

void LayingGlass::startGame()
{
    afficherInfosJoueurs();
    int taille = tailleGrille();
    board = new Board(taille, taille);

    for (Joueur& joueur : joueurs)
    {
        cout << "\n " << joueur.getName() << ", placez votre tuile de départ\n";
        board->displayGrille();

        int x, y;
        do
        {
            cout << "Entrez les coordonnées (x y, ex : 5 5) : ";
            cin >> x >> y;

            // Créer une tuile de départ pour le joueur
            Tuile tuileDepart = Tuile::creerTuileDepart(1, joueur.getId());

            if (board->PlacerTuile(tuileDepart, x, y, joueur.getColor()[0]))
            {
                joueur.addCases(x, y); // Ajoute l'emplacement au territoire du joueur
                cout << "Tuile placée avec succès !\n";
                break;
            }
            else
            {
                cout << "Position invalide. Essayez à nouveau.\n";
            }
        }
        while (true);
    }

    cout << "\n--- Début de la partie ! ---\n";
    board->displayGrille();


}

void LayingGlass::afficherInfosJoueurs() const
{
    cout << "\n--- Informations des joueurs ---\n";
    for (const Joueur& joueur : joueurs)
    {
        joueur.infoJoueur();
    }
}

int LayingGlass::tailleGrille() const
{
    return (nombreJoueurs <= 4) ? 20 : 30;
}

void LayingGlass::afficherTuileActuelle(const Joueur& joueur) {
    // Vérifie si la file de tuiles est vide
    if (fileTuiles.empty()) {
        std::cout << joueur.getName() << " : Aucune tuile disponible !\n";
        return;
    }

    // Affiche le nom du joueur
    std::cout << joueur.getName() << " :\n";

    // Affiche la tuile actuelle
    std::cout << "Current tile:\n";
    fileTuiles.front().display(); // Affiche la première tuile normalement

    // Préparer les 5 prochaines tuiles
    std::cout << "Next tiles:\n";

    // Trouver la hauteur maximale et la largeur maximale parmi les 5 prochaines tuiles
    size_t maxHeight = 0;
    std::vector<size_t> largeurs;
    for (size_t i = 1; i <= 5 && i < fileTuiles.size(); ++i) {
        const auto& shape = fileTuiles[i].getShape();
        maxHeight = std::max(maxHeight, shape.size());
        largeurs.push_back(shape[0].size()); // Largeur de la première ligne
    }

    // Afficher les tuiles côte à côte, ligne par ligne
    for (size_t row = 0; row < maxHeight; ++row) {
        for (size_t i = 1; i <= 5 && i < fileTuiles.size(); ++i) {
            const auto& shape = fileTuiles[i].getShape();
            if (row < shape.size()) {
                // Affiche une ligne de la tuile
                for (int cell : shape[row]) {
                    std::cout << (cell ? "a" : ".") << " ";
                }
                // Ajoute des espaces pour combler la largeur maximale
                for (size_t j = shape[row].size(); j < largeurs[i - 1]; ++j) {
                    std::cout << "  ";
                }
            } else {
                // Remplissage vide si la tuile est plus petite
                for (size_t j = 0; j < largeurs[i - 1]; ++j) {
                    std::cout << "  ";
                }
            }
            std::cout << "  "; // Espacement entre les tuiles
        }
        std::cout << std::endl; // Passe à la ligne suivante
    }
}

void LayingGlass::game() {
    const int nbTours = 9;

    for (int tour = 1; tour <= nbTours; ++tour) {
        std::cout << "\n--- Tour " << tour << " ---\n";

        for (Joueur& joueur : joueurs) {
            std::cout << "\nTour de " << joueur.getName() << " :\n";
            afficherTuileActuelle(joueur);
            board->displayGrille();
            interagirAvecTuile(joueur); // Manipuler et placer la tuile
        }
    }

    std::cout << "\n--- Fin de la partie ! ---\n";
    determinerGagnant();
}




void LayingGlass::interagirAvecTuile(Joueur& joueur) {
    char choix;
    do {
        std::cout << "Options :\n";
        std::cout << "Take tile (T)\n";
        std::cout << "Exchange tile - " << joueur.getCouponsEchange() << " available (E)\n";
        std::cout << "> ";
        std::cin >> choix;

        if (choix == 'T') {
            // Le joueur prend la tuile actuelle
            manipulerEtPlacerTuile(joueur, fileTuiles.front());
            fileTuiles.erase(fileTuiles.begin()); // Retirer la tuile jouée
            break;
        } else if (choix == 'E') {
            if (joueur.getCouponsEchange() > 0) {
                int index;
                std::cout << "Choisissez une tuile parmi les 5 suivantes (index 1-5) : ";
                std::cin >> index;

                if (index >= 1 && index <= 5 && index < fileTuiles.size()) {
                    Tuile tuileChoisie = echangerTuile(joueur, index);
                    manipulerEtPlacerTuile(joueur, tuileChoisie);
                    break;
                } else {
                    std::cout << "Index invalide !\n";
                }
            } else {
                std::cout << "Aucun coupon d'échange disponible !\n";
            }
        } else {
            std::cout << "Choix invalide. Essayez encore.\n";
        }
    } while (true);
}

Tuile LayingGlass::echangerTuile(Joueur& joueur, int index) {
    Tuile tuileChoisie = fileTuiles[index]; // Récupère la tuile choisie
    fileTuiles.erase(fileTuiles.begin() + index); // Retire la tuile choisie de la file
    joueur.utiliserCoupons(); // Déduit un coupon d'échange
    return tuileChoisie;
}

void LayingGlass::manipulerEtPlacerTuile(Joueur& joueur, Tuile& tuile) {
    char choix;
    do {
        std::cout << "Options :\n";
        std::cout << "Flip tile (F)\n";
        std::cout << "Rotate tile (R)\n";
        std::cout << "Place tile (P)\n";
        std::cout << "> ";
        std::cin >> choix;

        if (choix == 'F') {
            tuile.retourner();
            tuile.display(); // Affiche la tuile après retournement
        } else if (choix == 'R') {
            tuile.rotation();
            tuile.display(); // Affiche la tuile après rotation
        } else if (choix == 'P') {
            int x, y;
            std::cout << "Entrez les coordonnées pour placer la tuile (ligne, colonne) : ";
            std::cin >> x >> y;

            if (board->PlacerTuile(tuile, x, y, joueur.getColor()[0])) {
                joueur.addCases(x, y); // Ajoute la tuile au territoire du joueur
                std::cout << "Tuile placée avec succès !\n";
                board->displayGrille(); // Affiche le plateau après placement
                break;
            } else {
                std::cout << "Placement invalide. Essayez encore.\n";
            }
        } else {
            std::cout << "Choix invalide. Essayez encore.\n";
        }
    } while (true);
}

void LayingGlass::determinerGagnant() const {
    int maxCarre = 0;
    std::vector<Joueur> gagnantsPotentiels;

    // Trouver le plus grand carré pour chaque joueur
    for (const Joueur& joueur : joueurs) {
        int tailleCarre = board->calculerTaillePlusGrandCarre(joueur.getColor()[0]);
        if (tailleCarre > maxCarre) {
            maxCarre = tailleCarre;
            gagnantsPotentiels.clear();
            gagnantsPotentiels.push_back(joueur);
        } else if (tailleCarre == maxCarre) {
            gagnantsPotentiels.push_back(joueur);
        }
    }

    // En cas d'égalité, comparer le nombre total de cases occupées
    if (gagnantsPotentiels.size() > 1) {
        int maxCases = 0;
        Joueur gagnant;

        for (const Joueur& joueur : gagnantsPotentiels) {
            int totalCases = board->compterCasesOccupees(joueur.getColor()[0]);
            if (totalCases > maxCases) {
                maxCases = totalCases;
                gagnant = joueur;
            }
        }

        std::cout << "Le gagnant est : " << gagnant.getName()
                  << " avec un carré de taille " << maxCarre
                  << " et " << maxCases << " cases occupées !\n";
    } else if (!gagnantsPotentiels.empty()) {
        std::cout << "Le gagnant est : " << gagnantsPotentiels[0].getName()
                  << " avec un carré de taille " << maxCarre << " !\n";
    } else {
        std::cout << "Aucun gagnant trouvé.\n";
    }
}


LayingGlass::~LayingGlass()
{
    delete board;
}
