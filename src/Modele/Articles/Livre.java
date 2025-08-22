package Modele.Articles;

import Modele.Securite.SecurisationEntrees;

public class Livre {

    private String titre;
    private String auteur;
    private int quantite;
    private int quantiteDisponible;
    private String isbn;

    private Livre(String titre, String auteur, int quantite,int quantiteDisponible, String isbn) {
        this.titre = titre;
        this.auteur = auteur;
        this.quantite = quantite;
        this.quantiteDisponible = quantiteDisponible;
        this.isbn = isbn;
    }

    public static Livre creerLivre(String titre,  String auteur, int quantite,int quantiteDisponible, String isbn) {
        SecurisationEntrees.validerStringFormat(titre,  "titre", "^[a-zA-Z0-9\\s\\-\\.,:;!?'\\(\\)]+$",
                                                        " titre.");
        SecurisationEntrees.validerStringFormat(auteur, "auteur",
                                                        "^[a-zA-ZÀ-ÖØ-öø-ÿ']+(?:[-\\s][a-zA-ZÀ-ÖØ-öø-ÿ']+)*$",
                                                        " auteur." );
        SecurisationEntrees.validerEntierpositif(quantite,  "quantite" );
        SecurisationEntrees.validerEntierpositif(quantiteDisponible,  "quantiteDisponible" );
        SecurisationEntrees.validerISBN(isbn);

        if (quantiteDisponible > quantite) {
            throw new IllegalArgumentException("La quantite disponible ne peut pas etre superieur a la quantite initiale.");
        }
        return new Livre(titre, auteur, quantite, quantiteDisponible, isbn);
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public int getQuantite() {
        return quantite;
    }

    public int getQuantiteDisponible() {
        if(quantiteDisponible > quantite) {
            throw new IllegalStateException("La quantite disponible ne peut pas etre superieure a la quantite initiale.");
        }
        return quantiteDisponible;
    }

    public String getID() {
        return isbn;
    }


    public boolean verifierDisponibilite() {
        return this.quantiteDisponible > 0;
    }

    public void emprunter() {
        if (verifierDisponibilite()) {
            this.quantiteDisponible--;
        } else {
            throw new IllegalArgumentException("Le livre n'est pas disponible");
        }
    }

    public void rendre() {
        if (quantiteDisponible >= quantite) {
            throw new IllegalArgumentException("Erreur, tous les livres sont deja rendus");
        }
        this.quantiteDisponible++;
    }

    public void ajouterExemplaire(int nombreExemplaire) {
        if (nombreExemplaire <= 0) {
            throw new IllegalArgumentException("Le nombre d'exemplaire a ajouter doit etre positif");
        }

        this.quantite += nombreExemplaire;
        this.quantiteDisponible += nombreExemplaire;

        System.out.println(nombreExemplaire + " exemplaire(s) ajoute(s) avec succes !");
        System.out.println("Nouvelle quantite totale : " + this.quantite);
        System.out.println("Nouvelle quantite disponible : " + this.quantiteDisponible);
    }

    public void supprimerExemplaire(int nombreExemplaire) {
        if (nombreExemplaire <= 0 || nombreExemplaire > this.quantite) {
            throw new IllegalArgumentException("Le nombre d'exemplaire doit etre positif et existant");
        }
        this.quantiteDisponible -= nombreExemplaire;
        this.quantite -= nombreExemplaire;

        System.out.println("Exemplaire(s) supprimes avec succes !");
        System.out.println("Nouvelle quantite totale : " + this.quantite);
        System.out.println("Nouvelle quantite disponible : " + this.quantiteDisponible);
    }
}


