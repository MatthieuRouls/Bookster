package Modele.Securite;

import Modele.Articles.Livre;

import static sun.util.locale.LocaleUtils.isEmpty;

public class SecurisationEntrees {

    public static void validerStringFormat(String valeur, String champ, String regex, String messageErreur) {
        if (valeur == null || !valeur.matches(regex)) {
            throw new IllegalArgumentException(champ + " doit contenir une saisie ou " + messageErreur);
        }
    }

    public static void validerEntierpositif(int valeur, String champ) {
        if (valeur <= 0) {
            throw new IllegalArgumentException(champ + " doit etre un entier positif");
        }
    }

    public static void validerEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email invalide.");
        }
    }

    public static void validerISBN(String isbn) {
        if (isbn == null || !isbn.matches("^[0-9]{5}$")) {
            throw new IllegalArgumentException("ISBN invalide. Doit contenir 10 chiffres.");
        }
    }
    public static void validerID(String id) {
        if (id == null || !id.matches("^[0-9]{5}$")) {
            throw new IllegalArgumentException("ID invalide.");
        }
    }
    public static void validerObjetNonNull(Object objet, String champ) {
        if (objet == null) {
            throw new IllegalArgumentException(champ + " ne peut pas etre vide");
        }
    }
    public static void validerLivreDisponible(Livre livre, String champ) {
        validerObjetNonNull(livre, "Livre");
        if (livre.getQuantiteDisponible() <= 0) {
            throw new IllegalArgumentException(champ + " ne peut pas etre negatif.");
        }
    }
}
