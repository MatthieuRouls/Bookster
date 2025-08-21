package Modele.GenerateurIdentifiant.Generateurs;

import Modele.GenerateurIdentifiant.GenerateurIdentifiant;

import java.util.Random;

public class IDGenerateur extends GenerateurIdentifiant {


    @Override
    public String genererIdentifiant() {
        Random random = new Random();
        String id;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= 3; i++) {
                sb.append(random.nextInt(9));
            }

            int somme = 0;
            for (int i = 0; i < 4; i++) {
                int chiffre = Character.getNumericValue(sb.charAt(i));
                somme += (i % 2 == 0) ? chiffre : chiffre * 3;
            }
            int checkDigit = (10 - (somme % 10)) % 10;

            id = sb.toString() + checkDigit;

        } while (!estUnique(id));

        identifiantsutilises.add(id);
        return id;
    }
}


