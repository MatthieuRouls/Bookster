package Vue;

import Controleur.Bibliotheque;
import Modele.Actions.Emprunt;
import Modele.Articles.Livre;
import Modele.GenerateurIdentifiant.Generateurs.IDGenerateur;
import Modele.GenerateurIdentifiant.Generateurs.ISBNGenerator;
import Modele.Securite.SecurisationEntrees;
import Modele.Utilisateurs.Abonne;
import Modele.Utilisateurs.Bibliothecaire;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InterfaceBibliotheque {
    private Scanner sc;
    protected Bibliotheque bibliotheque;
    private Bibliothecaire bibliothecaire;

    public InterfaceBibliotheque(Bibliotheque bibliotheque) {
        this.sc = new Scanner(System.in);
        this.bibliotheque = bibliotheque;

        List<Bibliothecaire> bibliothecaires = bibliotheque.getBibliothecaires();
        if (bibliothecaires != null && !bibliothecaires.isEmpty()) {
            this.bibliothecaire = bibliothecaires.get(0);
        } else {
            System.out.println("Erreur: Aucun bibliothecaire disponible dans cette bibliotheque !");
        }

    }

    public void demarrerApplication() {
        try {
            System.out.println("BOOKSTER");

            boolean continuer = true;
            while (continuer) {
                afficherMenuPrincipal();
                continuer = false;
            }
        } finally {
            sc.close();
        }
        System.out.println("Au revoir !");
    }

    public void afficherMenuPrincipal() {
        int choixPrincipal;
        int choixSousMenu;

        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gestion des livres");
            System.out.println("2. Gestion des abonnes");
            System.out.println("3. Gestion des prets");
            System.out.println("4. Quitter");
            System.out.println("Choisissez une option: ");
            choixPrincipal = lireChoixUtilisateur();
            sc.nextLine();

            switch (choixPrincipal) {
                case 1:
                    do {
                        System.out.println("\n== GESTION DES LIVRES ===");
                        System.out.println("1. Enregistrer une nouvelle reference");
                        System.out.println("2. Ajouter de nouveaux exemplaires a une reference");
                        System.out.println("3. Supprimer une reference");
                        System.out.println("4. Supprimer un ou des exemplaires d'une reference");
                        System.out.println("5. Afficher la liste des references");
                        System.out.println("6. Retour au menu principal");
                        System.out.println("Choisissez une option: ");
                        choixSousMenu = lireChoixUtilisateur();
                        sc.nextLine();

                        switch (choixSousMenu) {
                            case 1:
                                enregistrerNouveauLivre();
                                break;
                            case 2:
                                bibliothecaire.ajouterExemplaireLivreInteractif(bibliotheque);
                                break;
                            case 3:
                                supprimerReferences(this.sc);
                                break;
                            case 4:
                                bibliothecaire.supprimerExemplaireLivreInteractif(bibliotheque);
                                break;
                            case 5:
                                afficherListeLivres();
                                break;
                            case 6:
                                break;
                            default:
                                System.out.println("Cette option n'existe pas");
                        }
                    } while (choixSousMenu != 6);
                    break;

                case 2:
                    do {
                        System.out.println("\n== GESTION DES ABONNES ===");
                        System.out.println("1. Enregistrer un nouvel abonne");
                        System.out.println("2. Supprimer un abonne");
                        System.out.println("3. Afficher liste des abonnes");
                        System.out.println("4. Retour au menu principal");
                        choixSousMenu = lireChoixUtilisateur();
                        sc.nextLine();

                        switch (choixSousMenu) {
                            case 1:
                                enregistrerNouvelAbonne();
                                break;
                            case 2:
                                supprimerAbonne(this.sc);
                                break;
                            case 3:
                                afficherListeAbonnes();
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Cette option n'existe pas");
                        }
                    } while (choixSousMenu != 4);
                    break;

                case 3:
                    do {
                        System.out.println("\n== GESTION DES PRETS ===");
                        System.out.println("1. Enregistrer un nouveau pret");
                        System.out.println("2. Gerer un retour");
                        System.out.println("3. Afficher liste des prets");
                        System.out.println("4. Retour au menu principal");
                        System.out.println("Choisissez une option: ");
                        choixSousMenu = lireChoixUtilisateur();
                        sc.nextLine();

                        switch (choixSousMenu) {
                            case 1:
                                enregistrerNouveauPret();
                                break;
                            case 2:
                                gererRetour();
                                break;
                            case 3:
                                afficherListePrets();
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Cette option n'existe pas");
                        }
                    } while  (choixSousMenu != 4);
                    break;

                case 4:
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Cette option n'existe pas");
            }
        } while (choixPrincipal != 3);
    }


    protected int lireChoixUtilisateur() {
        try {
            int choix = sc.nextInt();
            SecurisationEntrees.validerEntierpositif(choix, "Choix");
            return choix;
        } catch (Exception e) {
            System.out.println("Choix invalide, veuillez entrer un nombre");
            return -1;
        }
    }


    private void enregistrerNouvelAbonne() {
        System.out.println("\n=== NOUVEL ABONNE ===");

        String nom;
        while (true) {
            System.out.print("Nom : ");
            nom = sc.nextLine();
            try {
                SecurisationEntrees.validerStringFormat(nom, "Nom",
                                                        "^[a-zA-ZÀ-ÖØ-öø-ÿ']+(?:[-\\s][a-zA-ZÀ-ÖØ-öø-ÿ']+)*$",
                                                        "ou des caracteres valides");
                break;
            }  catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        String prenom;
        while (true) {
            System.out.print("Prenom : ");
            prenom = sc.nextLine();
            try {
                SecurisationEntrees.validerStringFormat(prenom, "Prenom",
                                                        "^[a-zA-ZÀ-ÖØ-öø-ÿ']+(?:[-\\s][a-zA-ZÀ-ÖØ-öø-ÿ']+)*$",
                                                        "ou des caracteres valides");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        String email;
        while (true) {
            System.out.print("Email : ");
            email = sc.nextLine();
            try {
                SecurisationEntrees.validerEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        String id;
        IDGenerateur generateurID = new IDGenerateur();
        id = generateurID.genererIdentifiant();

        try {
            Abonne abonne = Abonne.creerAbonne(nom, prenom, email, id);
            bibliotheque.ajouterAbonne(abonne);
            System.out.println("Nouvel abonné enregistré avec succès ! - ID: " + id);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void enregistrerNouveauLivre() {
        System.out.println("\n=== NOUVEAU LIVRE ===");

        String titre;
        while (true) {
        System.out.print("Titre : ");
        titre = sc.nextLine();
            try {
                SecurisationEntrees.validerStringFormat(titre, "Titre", "^[a-zA-Z0-9\\s\\-\\.,:;!?'\\(\\)]+$",
                                                                "contient des caractères interdits.");
                    break;
                } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        String auteur;
        while (true) {
        System.out.print("Auteur : ");
        auteur = sc.nextLine();
            try {
                SecurisationEntrees.validerStringFormat(auteur, "Auteur",
                                                                "^[a-zA-ZÀ-ÖØ-öø-ÿ']+(?:[-\\s][a-zA-ZÀ-ÖØ-öø-ÿ']+)*$",
                                                                "contient des caracteres interdits");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        int quantite;
        while (true) {
            System.out.print("Quantité : ");
            quantite = sc.nextInt();
            sc.nextLine();
                try {
                    SecurisationEntrees.validerEntierpositif(quantite, "quantite");
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
        }
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

        String isbn;
        while (true) {
        System.out.print("ISBN du livre : ");
        isbn = sc.nextLine();
            try {
                SecurisationEntrees.validerISBN(isbn);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
        String email;
        while (true) {
            System.out.print("Email de l'abonné : ");
            email = sc.nextLine();
            try {
                SecurisationEntrees.validerEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

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

        String id;
        while (true) {
            System.out.print("ID de l'abonné qui rend le livre : ");
            id = sc.nextLine();
            try {
                SecurisationEntrees.validerID(id);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        String isbn;
        while (true) {
            System.out.print("ISBN du livre à rendre : ");
            isbn = sc.nextLine();
            try {
                SecurisationEntrees.validerISBN(isbn);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
        try {
            bibliotheque.retournerLivre(isbn, id);
            System.out.println("Livre retourné avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void supprimerAbonne(Scanner scanner) {
        bibliotheque.supprimerAbonneBibli(scanner);
    }

    private void supprimerReferences(Scanner scanner) {
        bibliotheque.supprimerReferenceBibli(scanner);
    }
}
