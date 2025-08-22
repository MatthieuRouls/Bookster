package Modele.Utilisateurs;

import Controleur.Bibliotheque;
import Modele.Actions.Emprunt;
import Modele.Articles.Livre;
import java.util.List;
import java.util.Scanner;

public class Bibliothecaire {
    Scanner sc = new Scanner(System.in);
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


    public void ajouterExemplaireLivreInteractif(Bibliotheque bibliotheque) {
        System.out.println("ID du livre");
        String id = sc.nextLine();

        Livre livres = bibliotheque.rechercherLivreParID(id);

        if (livres != null) {
            System.out.println("Nombre d'exemplaires a ajouter : ");
            int nombreExemplaires = sc.nextInt();
            sc.nextLine();

            try {
                livres.ajouterExemplaire(nombreExemplaires);
                System.out.println("Exemplaire ajoute avec succes par " + getNom());
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun livre trouve avec cet identifiant.");
        }
    }

    public void supprimerExemplaireLivreInteractif(Bibliotheque bibliotheque) {
        System.out.println("ID du livre");
        String id = sc.nextLine();

        Livre livres = bibliotheque.rechercherLivreParID(id);

        if (livres != null) {
            System.out.println("Nombre d'exemplaires a supprimer : ");
            int nombreExemplaires = sc.nextInt();
            sc.nextLine();

            try {
                livres.supprimerExemplaire(nombreExemplaires);
                System.out.println("Exemplaire(s) supprimer avec succes par " + getNom() );
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }
}
