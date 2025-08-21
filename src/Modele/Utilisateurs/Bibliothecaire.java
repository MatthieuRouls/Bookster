package Modele.Utilisateurs;

import Controleur.Bibliotheque;
import Modele.Actions.Emprunt;
import Modele.Articles.Livre;

public class Bibliothecaire {

    private String nom;
    private String identifiant;

    public Bibliothecaire(String nom, String identifiant) {
        this.nom = nom;
        this.identifiant = identifiant;
    }
    public String getNom() {
        return nom;
    }
    public String getIdentifiant() {
        return identifiant;
    }

    public Emprunt creerEmprunt(Livre livre, Abonne abonne, Bibliotheque bibliotheque) {
        if (!livre.verifierDisponibilite()) {
            throw new IllegalStateException("Livre non disponible");
        }
        try {
            Emprunt nouvelEmprunt = Emprunt.creerEmprunt(livre, abonne);

            abonne.ajouterEmprunt(nouvelEmprunt);
            bibliotheque.ajouterEmprunt(nouvelEmprunt);
            return nouvelEmprunt;
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }
    }

    public void gererRetour(Emprunt emprunt, Bibliotheque bibliotheque) {
        if (emprunt != null && !emprunt.isRendu()) {
            emprunt.marquerCommeRendu();

            System.out.println("Livre retourne avec succes !");
        }
    }

    public void enregistrerNouveauLivre(Livre livre, Bibliotheque bibliotheque) {
        bibliotheque.ajouterLivre(livre);
    }
    public void enregistrerNouvelAbonne(Abonne abonne, Bibliotheque bibliotheque) {
        bibliotheque.ajouterAbonne(abonne);
    }
}
