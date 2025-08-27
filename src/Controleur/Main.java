package Controleur;

import Vue.JavaFX.MainApplication;
import Vue.InterfaceBibliotheque;

public class Main {
    public static void main(String[] args) {
        // Choix entre interface console et JavaFX
        if (args.length > 0 && args[0].equals("--console")) {
            // Interface console
            InterfaceBibliotheque interfaceBibliotheque = new InterfaceBibliotheque();
            interfaceBibliotheque.demarrerApplication();
        } else {
            // Interface JavaFX (par défaut)
            MainApplication.main(args);
        }
    }
}