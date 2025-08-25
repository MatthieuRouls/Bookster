package Modele.Utilisateurs;

import Controleur.Bibliotheque;

public class CompteBibliotheque {
    private String idBibliotheque;
    private String motDePasse;
    private Bibliotheque bibliotheque;

    public CompteBibliotheque(String idBibliotheque, String motDePasse) {
        this.idBibliotheque = idBibliotheque;
        this.motDePasse = motDePasse;
    }

    public CompteBibliotheque(String idBibliotheque, String motDePasse, Bibliotheque bibliotheque) {
        this.idBibliotheque = idBibliotheque;
        this.motDePasse = motDePasse;
        this.bibliotheque = bibliotheque;
    }

    public Bibliotheque getBibliotheque() {
        return bibliotheque;
    }

    public String getIdBibliotheque() {
        return idBibliotheque;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
}
