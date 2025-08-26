package Modele.Securite;

import Controleur.Bibliotheque;
import Modele.Utilisateurs.Bibliothecaire;
import Modele.Utilisateurs.CompteBibliotheque;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GestionConnexion {
    private Map<String, CompteBibliotheque> bibliotheques;
    private Bibliothecaire bibliothecaireConnecte;
    private CompteBibliotheque bibliothequeConnectee;

    public GestionConnexion() {
        this.bibliotheques = new HashMap<>();
    }

    public void ajouterBibliotheque(String idBibliotheque, String motDePasse, Bibliotheque bibliotheque) {
        bibliotheques.put(idBibliotheque, new CompteBibliotheque(idBibliotheque, motDePasse, bibliotheque));
    }

    public void ajouterBibliothecaire(String nom, String identifiant, String motDePasse) {
        if (bibliothequeConnectee != null && bibliothequeConnectee.getBibliotheque() != null) {
            Bibliothecaire nouvelleBibliothecaire = new Bibliothecaire(nom, identifiant, motDePasse);
            bibliothequeConnectee.getBibliotheque().ajouterBibliothecaire(nouvelleBibliothecaire);
            System.out.println("BIbliothecaire ajoute avec succes !");
        } else {
            System.out.println("Aucune bibliotheque connectee !");
        }
    }

    public void afficherBibliothecaires() {
        if (bibliothequeConnectee != null && bibliothequeConnectee.getBibliotheque() != null) {
            System.out.println("=== Bibliothecaire de " + bibliothequeConnectee.getBibliotheque().getNom() + "===");
            for (Bibliothecaire bib : bibliothequeConnectee.getBibliotheque().getBibliothecaires()) {
                System.out.println("- " + bib.getNom() + "(ID: " + bib.getIdentifiant() + ")");
            }
        } else {
            System.out.println("Aucune bibliotheque connectee !");
        }
    }

    public boolean connexionBibliotheque(Scanner sc) {
        System.out.println("ID de la bibliothèque : ");
        String idBibliotheque = sc.nextLine();
        System.out.println("Mot de passe de la bibliothèque : ");
        String motDePasse = sc.nextLine();

        CompteBibliotheque compte = bibliotheques.get(idBibliotheque);
        if (compte != null && compte.getMotDePasse().equals(motDePasse)) {
            bibliothequeConnectee = compte;
            System.out.println("Connexion à la bibliothèque réussie !");
            return true;
        } else {
            System.out.println("ID ou mot de passe incorrect.");
            return false;
        }
    }

    public boolean connexionBibliothecaire(Scanner sc) {
        if (bibliothequeConnectee == null) {
            System.out.println("Veuillez d'abord vous connecter à une bibliothèque.");
            return false;
        }

        System.out.println("ID du bibliothécaire : ");
        String idBibliothecaire = sc.nextLine();
        System.out.println("Mot de passe du bibliothécaire : ");
        String motDePasse = sc.nextLine();

        Bibliothecaire bibliothecaire = null;
        for (Bibliothecaire bib : bibliothequeConnectee.getBibliotheque().getBibliothecaires()) {
            if (bib.getIdentifiant().equals(idBibliothecaire)) {
                bibliothecaire = bib;
                break;
            }
        }

        if (bibliothecaire != null && bibliothecaire.getMotDePasse().equals(motDePasse)) {
            bibliothecaireConnecte = bibliothecaire;
            System.out.println("Connexion réussie ! Bienvenue, " + bibliothecaire.getNom());
            return true;
        } else {
            System.out.println("ID ou mot de passe incorrect.");
            return false;
        }
    }

    public Bibliothecaire getBibliothecaireConnecte() {
        return bibliothecaireConnecte;
    }

    public Bibliotheque getBibliothequeConnectee() {
        return bibliothequeConnectee.getBibliotheque();
    }
}
