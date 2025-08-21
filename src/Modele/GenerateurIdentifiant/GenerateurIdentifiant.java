package Modele.GenerateurIdentifiant;

import java.util.HashSet;
import java.util.Set;

public abstract class GenerateurIdentifiant {
    protected Set<String> identifiantsutilises;

    public GenerateurIdentifiant() {
        this.identifiantsutilises = new HashSet<>();
    }

    public abstract String genererIdentifiant();

    public void ajouterIdentifiantExistant(String identifiant) {
        identifiantsutilises.add(identifiant);
    }

     protected boolean estUnique(String identifiant) {
        return !identifiantsutilises.contains(identifiant);
     }
}
