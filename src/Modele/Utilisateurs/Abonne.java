package Modele.Utilisateurs;

import Modele.Actions.Emprunt;
import Modele.Securite.SecurisationEntrees;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Abonne {

    private String nom;
    private String prenom;
    private String email;
    private String id;
    private Date dateInscription;
    private List<Emprunt> emprunts;

    private Abonne(String nom, String prenom, String email, Date dateInscription, String id) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.id = id;
        this.dateInscription = dateInscription;
        this.emprunts = new ArrayList<>();
    }

    public static Abonne creerAbonne(String nom, String prenom, String email, String id) {
        SecurisationEntrees.validerStringFormat(nom,"Nom",
                                                    "^[a-zA-ZÀ-ÖØ-öø-ÿ']+(?:[-\\s][a-zA-ZÀ-ÖØ-öø-ÿ']+)*$",
                                                    " nom");
        SecurisationEntrees.validerStringFormat(prenom, "Prenom",
                                                        "^[a-zA-Z]{2,}(-[a-zA-Z]+)*$",
                                                        " prenom");
        SecurisationEntrees.validerEmail(email);
        SecurisationEntrees.validerID(id);
        return new Abonne(nom, prenom, email, new Date(), id);
    }

    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getEmail() {
        return email;
    }
    public String getID() {
        return id;
    }
    public Date getDateInscription() {
        return dateInscription;
    }
    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public void ajouterEmprunt(Emprunt emprunt) {
        if (emprunts != null) {}
            this.emprunts.add(emprunt);
    }

    public void rendreLivre(Emprunt emprunt) {
        if (emprunt != null && this.emprunts.contains(emprunt)) {
            emprunt.marquerCommeRendu();
        }
    }

    public List<Emprunt> afficherHistorique() {
        return new ArrayList<>(this.emprunts);
    }
}
