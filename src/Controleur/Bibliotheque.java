package Controleur;

import Modele.Utilisateurs.Abonne;
import Modele.Utilisateurs.Bibliothecaire;
import Modele.Actions.Emprunt;
import Modele.Articles.Livre;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bibliotheque {

    private String nom;
    private String adresse;
    private List<Abonne> abonnes;
    private List<Livre> livres;
    private List<Emprunt> emprunts;
    private List<Bibliothecaire> bibliothecaires;

    public Bibliotheque(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
        this.bibliothecaires = new ArrayList<>();
        this.livres = new ArrayList<>();
        this.emprunts = new ArrayList<>();
        this.abonnes = new ArrayList<>();
    }

    public List<Livre> getLivres() {
        return livres;
    }

    public List<Abonne> getAbonnes() {
        return abonnes;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public List<Bibliothecaire> getBibliothecaires() {
        return bibliothecaires;
    }

    public Emprunt emprunterLivre(String idLivre, String emailAbonne) {
        Livre livre = rechercherLivreParID(idLivre);
        Abonne abonne = rechercherAbonneParEmail(emailAbonne);

        if (livre == null) {
            throw new IllegalArgumentException("Livre introuvable");
        }
        if (abonne == null) {
            throw new IllegalArgumentException("Abonne introuvable");
        }
        if (!bibliothecaires.isEmpty()) {
            return bibliothecaires.get(0).creerEmprunt(livre,abonne, this);
        } else  {
            throw new IllegalArgumentException("Aucun bibliothecaire disponible");
        }
    }

    public void retournerLivre(String isbn, String email) {
        Livre livre = rechercherLivreParID(isbn);
        Abonne abonne = rechercherAbonneParEmail(email);

        if (livre == null) {
            throw new IllegalArgumentException("Livre introuvable");
        }
        if (abonne == null) {
            throw new IllegalArgumentException("Abonne introuvable");
        }

        Emprunt empruntEnCours = null;
        for (Emprunt e : emprunts) {
            if (e.getLivre().getID().equals(isbn) &&
                    e.getAbonne().getEmail().equals(email) &&
                    !e.isRendu()) {
                empruntEnCours = e;
                break;
            }
        }

        if (empruntEnCours == null) {
            throw new IllegalArgumentException("Aucun emprunt en cours trouvé pour ce livre et cet abonné");
        }
        if (!bibliothecaires.isEmpty()) {
            bibliothecaires.getFirst().gererRetour(empruntEnCours, this);
        } else {
            throw new IllegalStateException("Aucun bibliothécaire disponible pour gérer le retour");
        }
    }

    public Livre rechercherLivreParID(String id) {
        return livres.stream().filter(livre -> id.equals(livre.getID())).findFirst().orElse(null);
    }

    public Abonne rechercherAbonneParEmail(String email) {
        return abonnes.stream().filter(abonne -> email.equals(abonne.getEmail())).findFirst().orElse(null);
    }


    public void ajouterAbonne(Abonne abonne) {
        if (this.abonnes != null) {
            this.abonnes.add(abonne);
        }
    }
    public void ajouterLivre(Livre livre) {
        if (this.livres != null) {
            this.livres.add(livre);
        }
    }
    public void ajouterEmprunt(Emprunt emprunt) {
        if (this.emprunts != null) {
            this.emprunts.add(emprunt);
        }
    }
    public void ajouterBibliothecaire(Bibliothecaire bibliothecaire) {
        if (this.bibliothecaires != null) {
            this.bibliothecaires = new ArrayList<>();
        }
        this.bibliothecaires.add(bibliothecaire);
    }

    public void supprimerAbonneBibli(Scanner scanner) {
        System.out.print("Veuillez entrer l'ID de l'abonne a supprimer: ");
        String idAbonne = scanner.next();

        Abonne abonneASupprimer = null;
        for (Abonne abonne : this.abonnes) {
            if (abonne.getID().equals(idAbonne)){
                abonneASupprimer=abonne;
                break;
            }
        }
        if (abonneASupprimer!=null) {
            this.abonnes.remove(abonneASupprimer);
            System.out.println("L'abonne " + abonneASupprimer.getNom() + (" ") + abonneASupprimer.getPrenom() + " a ete supprime avec succes !");
        } else {
            System.out.println("Aucun abonne trouve avec cet ID.");
        }
    }

    public void supprimerReferenceBibli (Scanner scanner) {
        System.out.print("Veuillez entrer l'ID de la reference a supprimer: ");
        String idReference = scanner.next();

        Livre livreASupprimer = null;
        for (Livre livre : this.livres) {
            if (livre.getID().equals(idReference)){
                livreASupprimer=livre;
                break;
            }
        }
        if (livreASupprimer!=null) {
            this.livres.remove(livreASupprimer);
            System.out.println("La reference " + livreASupprimer.getTitre() + " de " + livreASupprimer.getAuteur() + " a bien ete supprimee.");
        } else {
            System.out.println("Aucun reference trouve avec cet ID.");
        }
    }
}
