package Modele.Securite;

import Controleur.Bibliotheque;
import Modele.Utilisateurs.Bibliothecaire;
import Modele.Utilisateurs.CompteBibliotheque;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GestionConnexion {
    private Map<String, CompteBibliotheque> bibliothèques;
    private Map<String, Bibliothecaire> bibliothecaires;
    private Bibliothecaire bibliothecaireConnecte;
    private CompteBibliotheque bibliothèqueConnectée;

    public GestionConnexion() {
        this.bibliothèques = new HashMap<>();
        this.bibliothecaires = new HashMap<>();
    }

    public void ajouterBibliotheque(String idBibliotheque, String motDePasse, Bibliotheque bibliotheque) {
        bibliothèques.put(idBibliotheque, new CompteBibliotheque(idBibliotheque, motDePasse, bibliotheque));
    }

    public void ajouterBibliothecaire(String idBibliothecaire, Bibliothecaire bibliothecaire) {
        bibliothecaires.put(idBibliothecaire, bibliothecaire);
    }

    public boolean connexionBibliotheque(Scanner sc) {
        System.out.println("ID de la bibliothèque : ");
        String idBibliotheque = sc.nextLine();
        System.out.println("Mot de passe de la bibliothèque : ");
        String motDePasse = sc.nextLine();

        CompteBibliotheque compte = bibliothèques.get(idBibliotheque);
        if (compte != null && compte.getMotDePasse().equals(motDePasse)) {
            bibliothèqueConnectée = compte;
            System.out.println("Connexion à la bibliothèque réussie !");
            return true;
        } else {
            System.out.println("ID ou mot de passe incorrect.");
            return false;
        }
    }

    public boolean connexionBibliothecaire(Scanner sc) {
        if (bibliothèqueConnectée == null) {
            System.out.println("Veuillez d'abord vous connecter à une bibliothèque.");
            return false;
        }

        System.out.println("ID du bibliothécaire : ");
        String idBibliothecaire = sc.nextLine();
        System.out.println("Mot de passe du bibliothécaire : ");
        String motDePasse = sc.nextLine();

        Bibliothecaire bibliothecaire = bibliothecaires.get(idBibliothecaire);
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

    public CompteBibliotheque getBibliothèqueConnectée() {
        return bibliothèqueConnectée;
    }
}
