#ifndef TUILE_H
#define TUILE_H
#include <vector>
#include <utility>
#include <string>


class Tuile
{
    public:
        Tuile(int id, std::vector<std::vector<int>> shape, int owner = -1);
        static Tuile creerTuileDepart(int id, int owner = -1);
        void display(bool inlineDisplay = false) const;
        void rotation();
        const std::vector<std::vector<int>>& getShape() const;
        static std::vector<Tuile> genererTuiles();
        void retourner(); // Retourne la tuile horizontalement
        virtual ~Tuile();

    protected:

    private:
        int id;
        std::vector<std::vector<int>> shape;
        std::pair<int, int> taille;
        std::string orientation;
        int owner;
        std::pair<int, int> position;



};

#endif // TUILE_H
