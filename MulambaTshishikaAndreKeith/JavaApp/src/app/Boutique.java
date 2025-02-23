package app;

public class Boutique {
    private int id;
    private String nom;

    public Boutique(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return  nom;
    }
}