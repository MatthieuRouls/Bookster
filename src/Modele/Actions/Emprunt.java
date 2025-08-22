package Modele.Actions;

import Modele.Articles.Livre;
import Modele.Securite.SecurisationEntrees;
import Modele.Utilisateurs.Abonne;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Emprunt {

    private Livre livre;
    private Date dateDebut;
    private String dateFin;
    private Abonne abonne;
    private boolean rendu = false;
    private static final int DUREE_PRET = 7;


    private Emprunt(Livre livre, Abonne abonne) {
        this.dateDebut = new Date();
        this.dateFin = calculerDateFin();
        this.abonne = abonne;
        this.livre = livre;
        livre.emprunter();
    }

    public static Emprunt creerEmprunt(Livre livre, Abonne abonne) {
        SecurisationEntrees.validerLivreDisponible(livre, "Livre");
        SecurisationEntrees.validerObjetNonNull(abonne,  "Abonne");
        return new Emprunt(livre, abonne);
    }

    public String getDateDebut() {
        return dateDebut.toString();
    }
    public String getDateFin() {
        return dateFin.toString();
    }
    public Livre getLivre() {
        return livre;
    }
    public Abonne getAbonne() {
        return abonne;
    }
    public boolean isRendu() {
        return rendu;
    }

    public String calculerDateFin() {
        LocalDate pret = LocalDate.ofInstant(this.dateDebut.toInstant(), java.time.ZoneId.systemDefault());
        LocalDate dateFin = pret.plusDays(DUREE_PRET);

        DateTimeFormatter formatterFR = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);

        return dateFin.format(formatterFR);
    }

    public void marquerCommeRendu() {
        if (!rendu) {
            this.rendu = true;
            livre.rendre();
        }
    }

    public boolean estEnRetard() {
        Date aujourdhui = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            Date dateFin = sdf.parse(this.dateFin);
            return !rendu && aujourdhui.after(dateFin);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
