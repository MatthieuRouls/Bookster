package Controleur;


import Modele.Utilisateurs.Bibliothecaire;
import Vue.InterfaceBibliotheque;

public class Main {
    public static void main(String[] args) {

        Bibliotheque bibliotheque = new Bibliotheque("Bibliothèque Centrale", "123 Rue des Livres");

        Bibliothecaire martin = new Bibliothecaire("Martin Durand", "BIB001");
        bibliotheque.ajouterBibliothecaire(martin);

        InterfaceBibliotheque interfaceBibliotheque = new InterfaceBibliotheque(bibliotheque);
        interfaceBibliotheque.demarrerApplication();
    }
}
