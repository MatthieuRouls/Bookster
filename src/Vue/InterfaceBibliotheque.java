package Vue;

import Controleur.Bibliotheque;
import Modele.Actions.Emprunt;
import Modele.Articles.Livre;
import Modele.GenerateurIdentifiant.Generateurs.IDGenerateur;
import Modele.GenerateurIdentifiant.Generateurs.ISBNGenerator;
import Modele.Utilisateurs.Abonne;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InterfaceBibliotheque {
    private Scanner sc;
    protected Bibliotheque bibliotheque;
    public InterfaceBibliotheque(Bibliotheque bibliotheque) {
        this.sc = new Scanner(System.in);
        this.bibliotheque = bibliotheque;
    }

    public void demarrerApplication() {
        try {
            System.out.println("BIBLI'GESTION");

            boolean continuer = true;
            while (continuer) {
                afficherMenuPrincipal();
                int choix = lireChoixUtilisateur();
                continuer = traiterChoix(choix);
            }
        } finally {
            sc.close();
        }

        System.out.println("Au revoir !");
    }

    public void afficherMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Enregistrer un nouvel abonné");
        System.out.println("2. Enregistrer un nouveau livre");
        System.out.println("3. Enregistrer un nouveau prêt");
        System.out.println("4. Afficher la liste des abonnés");
        System.out.println("5. Afficher la liste des livres");
        System.out.println("6. Afficher la liste des prêts");
        System.out.println("7. Gérer un retour");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    protected int lireChoixUtilisateur() {
        try {
            return sc.nextInt();
        } catch (Exception e) {
            sc.nextLine();
            System.out.println("Choix invalide, veuillez entrer un nombre");
            return -1;
        }
    }

    protected boolean traiterChoix(int choix) {
        sc.nextLine();

        switch (choix) {
            case 1: enregistrerNouvelAbonne(); break;
            case 2: enregistrerNouveauLivre(); break;
            case 3: enregistrerNouveauPret(); break;
            case 4: afficherListeAbonnes(); break;
            case 5: afficherListeLivres(); break;
            case 6: afficherListePrets(); break;
            case 7: gererRetour(); break;
            case 0: return false;
            default:
                System.out.println(" Choix invalide !");
                break;
        }
        return true;
    }

    private void enregistrerNouvelAbonne() {
        System.out.println("\n=== NOUVEL ABONNE ===");

        System.out.println("Nom : ");
        String nom = sc.nextLine();

        System.out.println("Prenom : ");
        String prenom = sc.nextLine();

        System.out.println("Email : ");
        String email = sc.nextLine();

        String id;
        IDGenerateur generateurID = new IDGenerateur();
        id = generateurID.genererIdentifiant();

        try {
            Abonne abonne = Abonne.creerAbonne(nom, prenom, email, id);
            bibliotheque.ajouterAbonne(abonne);
            System.out.println("Nouvel abonné enregistré avec succès !");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void enregistrerNouveauLivre() {
        System.out.println("\n=== NOUVEAU LIVRE ===");

        System.out.print("Titre : ");
        String titre = sc.nextLine();

        System.out.print("Auteur : ");
        String auteur = sc.nextLine();

        System.out.print("Quantité : ");
        int quantite = sc.nextInt();
        sc.nextLine();

        System.out.print("Quantite disponible : ");
        int quantiteDisponible = quantite;
        System.out.println(quantiteDisponible);

        String isbn;
        ISBNGenerator generateurISBN = new ISBNGenerator();
        isbn = generateurISBN.genererIdentifiant();

        try {
            Livre livre = Livre.creerLivre(titre, auteur, quantite, quantiteDisponible, isbn);
            bibliotheque.ajouterLivre(livre);
            System.out.println("Livre enregistré avec succès: " + isbn);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void enregistrerNouveauPret() {
        System.out.println("\n=== NOUVEAU PRÊT ===");

        System.out.print("ISBN du livre : ");
        String isbn = sc.nextLine();

        System.out.print("Email de l'abonné : ");
        String email = sc.nextLine();

        try {
            Emprunt emprunt = bibliotheque.emprunterLivre(isbn, email);
            System.out.println("Prêt enregistré avec succès !");
            System.out.println("Date de retour prévue : " + emprunt.getDateFin());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void afficherListeAbonnes() {
        System.out.println("\n=== LISTE DES ABONNÉS ===");
        List<Abonne> abonnes = bibliotheque.getAbonnes();

        if (abonnes.isEmpty()) {
            System.out.println("Aucun abonné enregistré.");
        } else {
            for (int i = 0; i < abonnes.size(); i++) {
                Abonne abonne = abonnes.get(i);
                System.out.println((i+1) + ". " + abonne.getPrenom() + " " +
                        abonne.getNom() + " (" + abonne.getEmail() + " - ID: " + abonne.getID() + ")");
            }
        }
    }

    private void afficherListeLivres() {
        System.out.println("\n=== LISTE DES LIVRES ===");
        List<Livre> livres = bibliotheque.getLivres();

        if (livres.isEmpty()) {
            System.out.println("Aucun livre enregistré.");
        } else {
            for (int i = 0; i < livres.size(); i++) {
                Livre livre = livres.get(i);
                System.out.println((i+1) + ". \"" + livre.getTitre() + "\" par " +
                        livre.getAuteur() + " (Stock: " + livre.getQuantiteDisponible() + ")" + " - ISBN: " + livre.getIsbn());
            }
        }
    }

    private void afficherListePrets() {
        System.out.println("\n=== LISTE DES PRÊTS EN COURS ===");
        List<Emprunt> emprunts = bibliotheque.getEmprunts().stream().filter(e -> !e.isRendu()).collect(Collectors.toList());

        if (emprunts.isEmpty()) {
            System.out.println("Aucun prêt en cours.");
        } else {
            for (int i = 0; i < emprunts.size(); i++) {
                Emprunt emprunt = emprunts.get(i);
                System.out.println((i+1) + ". " + emprunt.getAbonne().getPrenom() +
                        " a emprunté \"" + emprunt.getLivre().getTitre() +
                        "\" (Retour prévu : " + emprunt.getDateFin() + " - Retard ? " +
                                            (emprunt.estEnRetard()? "Oui, des penalites peuvent s'appliquer.)" : "Non)"));
            }
        }
    }

    private void gererRetour() {
        System.out.println("\n=== GESTION RETOUR ===");

        afficherListePrets();

        System.out.print("ID de l'abonné qui rend le livre : ");
        String id = sc.nextLine();

        System.out.print("ISBN du livre à rendre : ");
        String isbn = sc.nextLine();

        try {
            bibliotheque.retournerLivre(isbn, id);
            System.out.println("Livre retourné avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
