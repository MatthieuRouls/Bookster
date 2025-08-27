package Vue.JavaFX.Controllers;

import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import Controleur.Bibliotheque;
import Modele.Actions.Emprunt;
import Modele.Articles.Livre;
import Modele.GenerateurIdentifiant.Generateurs.IDGenerateur;
import Modele.GenerateurIdentifiant.Generateurs.ISBNGenerator;
import Modele.Securite.GestionConnexion;
import Modele.Utilisateurs.Abonne;
import Modele.Utilisateurs.Bibliothecaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    // Navigation
    @FXML private Button btnDashboard, btnBooks, btnMembers, btnCheckouts;
    @FXML private Label lblLibraryName, lblLibrarianName;

    // Content Panes
    @FXML private AnchorPane dashboardPane, booksPane, membersPane, checkoutsPane;

    // Dashboard Elements
    @FXML private Text txtTotalBooks, txtActiveMembers, txtBooksCheckedOut, txtOverdue;
    @FXML private Text txtUsageRate, txtAvailableBooks;
    @FXML private VBox recentActivityBox;

    // Books Management
    @FXML private TableView<Livre> booksTable;
    @FXML private TableColumn<Livre, String> colBookTitle, colBookAuthor, colBookISBN, colBookGenre, colBookStatus;
    @FXML private TableColumn<Livre, Integer> colBookTotal, colBookAvailable;
    @FXML private TextField txtSearchBooks;
    @FXML private Button btnAddBook;

    // Members Management
    @FXML private TableView<Abonne> membersTable;
    @FXML private TableColumn<Abonne, String> colMemberName, colMemberEmail, colMemberPhone, colMemberType, colMemberStatus;
    @FXML private TableColumn<Abonne, String> colMemberJoinDate;
    @FXML private TableColumn<Abonne, Integer> colMemberBooksOut;
    @FXML private TextField txtSearchMembers;
    @FXML private Button btnAddMember;

    // Checkouts Management
    @FXML private TableView<Emprunt> checkoutsTable;
    @FXML private TableColumn<Emprunt, String> colCheckoutBook, colCheckoutMember, colCheckoutDate, colCheckoutDue, colCheckoutStatus;
    @FXML private TextField txtSearchCheckouts;
    @FXML private Button btnCheckoutBook;

    // Data
    private Bibliotheque bibliotheque;
    private Bibliothecaire bibliothecaireConnecte;
    private GestionConnexion gestionConnexion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeData();
        setupNavigation();
        setupTables();
        showDashboard();
        updateDashboardStats();
    }

    private void initializeData() {
        // Initialize with test data like in your original code
        this.gestionConnexion = new GestionConnexion();
        Bibliotheque bibliothequeTest = new Bibliotheque("Bibliothèque des Coquilles", "123 rue des livres");
        gestionConnexion.ajouterBibliotheque("1234", "Roucoulette.57", bibliothequeTest);

        Bibliothecaire bibliothecaireTest = new Bibliothecaire("Jeanne Boule", "1234", "Roucoulette.57");
        bibliothequeTest.ajouterBibliothecaire(bibliothecaireTest);
        this.bibliotheque = bibliothequeTest;
        this.bibliothecaireConnecte = bibliothecaireTest;

        // Add some sample data
        addSampleData();

        // Update UI
        lblLibraryName.setText(bibliotheque.getNom());
        lblLibrarianName.setText("Admin"); // You can change this based on your needs
    }

    private void addSampleData() {
        try {
            // Add sample books
            ISBNGenerator isbnGen = new ISBNGenerator();
            Livre livre1 = Livre.creerLivre("Le Grand Gatsby", "F. Scott Fitzgerald", 3, 2, isbnGen.genererIdentifiant());
            Livre livre2 = Livre.creerLivre("Ne tirez pas sur l'oiseau moqueur", "Harper Lee", 2, 1, isbnGen.genererIdentifiant());
            Livre livre3 = Livre.creerLivre("1984", "George Orwell", 4, 4, isbnGen.genererIdentifiant());
            Livre livre4 = Livre.creerLivre("Orgueil et Préjugés", "Jane Austen", 2, 2, isbnGen.genererIdentifiant());

            bibliotheque.ajouterLivre(livre1);
            bibliotheque.ajouterLivre(livre2);
            bibliotheque.ajouterLivre(livre3);
            bibliotheque.ajouterLivre(livre4);

            // Add sample members
            IDGenerateur idGen = new IDGenerateur();
            Abonne abonne1 = Abonne.creerAbonne("Dupont", "Jean", "jean.dupont@email.com", idGen.genererIdentifiant());
            Abonne abonne2 = Abonne.creerAbonne("Martin", "Marie", "marie.martin@email.com", idGen.genererIdentifiant());
            Abonne abonne3 = Abonne.creerAbonne("Moreau", "Pierre", "pierre.moreau@email.com", idGen.genererIdentifiant());
            Abonne abonne4 = Abonne.creerAbonne("Leroy", "Sophie", "sophie.leroy@email.com", idGen.genererIdentifiant());

            bibliotheque.ajouterAbonne(abonne1);
            bibliotheque.ajouterAbonne(abonne2);
            bibliotheque.ajouterAbonne(abonne3);
            bibliotheque.ajouterAbonne(abonne4);

            // Add sample checkouts
            Emprunt emprunt1 = bibliotheque.emprunterLivre(livre2.getID(), abonne1.getEmail());
            Emprunt emprunt2 = bibliotheque.emprunterLivre(livre1.getID(), abonne2.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupNavigation() {
        btnDashboard.setOnAction(e -> showDashboard());
        btnBooks.setOnAction(e -> showBooks());
        btnMembers.setOnAction(e -> showMembers());
        btnCheckouts.setOnAction(e -> showCheckouts());
    }

    private void setupTables() {
        // Books table
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colBookAuthor.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colBookISBN.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookTotal.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colBookAvailable.setCellValueFactory(new PropertyValueFactory<>("quantiteDisponible"));
        colBookStatus.setCellValueFactory(cellData -> {
            Livre livre = cellData.getValue();
            String status = livre.verifierDisponibilite() ? "available" : "checked out";
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        // Members table
        colMemberName.setCellValueFactory(cellData -> {
            Abonne abonne = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(abonne.getPrenom() + " " + abonne.getNom());
        });
        colMemberEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMemberJoinDate.setCellValueFactory(cellData -> {
            Abonne abonne = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(abonne.getDateInscription().toString());
        });
        colMemberBooksOut.setCellValueFactory(cellData -> {
            Abonne abonne = cellData.getValue();
            long booksOut = abonne.getEmprunts().stream().filter(e -> !e.isRendu()).count();
            return new javafx.beans.property.SimpleObjectProperty<>((int) booksOut);
        });
        colMemberStatus.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty("active");
        });
        colMemberType.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty("standard");
        });

        // Checkouts table
        colCheckoutBook.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLivre().getTitre());
        });
        colCheckoutMember.setCellValueFactory(cellData -> {
            Abonne abonne = cellData.getValue().getAbonne();
            return new javafx.beans.property.SimpleStringProperty(abonne.getPrenom() + " " + abonne.getNom());
        });
        colCheckoutDate.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateDebut());
        });
        colCheckoutDue.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateFin());
        });
        colCheckoutStatus.setCellValueFactory(cellData -> {
            Emprunt emprunt = cellData.getValue();
            String status;
            if (emprunt.isRendu()) {
                status = "Returned";
            } else if (emprunt.estEnRetard()) {
                status = "Overdue";
            } else {
                status = "checked out";
            }
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        // Add button handlers
        btnAddBook.setOnAction(e -> showAddBookDialog());
        btnAddMember.setOnAction(e -> showAddMemberDialog());
        btnCheckoutBook.setOnAction(e -> showCheckoutDialog());
    }

    private void showDashboard() {
        setActiveButton(btnDashboard);
        hideAllPanes();
        dashboardPane.setVisible(true);
        updateDashboardStats();
    }

    private void showBooks() {
        setActiveButton(btnBooks);
        hideAllPanes();
        booksPane.setVisible(true);
        refreshBooksTable();
    }

    private void showMembers() {
        setActiveButton(btnMembers);
        hideAllPanes();
        membersPane.setVisible(true);
        refreshMembersTable();
    }

    private void showCheckouts() {
        setActiveButton(btnCheckouts);
        hideAllPanes();
        checkoutsPane.setVisible(true);
        refreshCheckoutsTable();
    }

    private void hideAllPanes() {
        dashboardPane.setVisible(false);
        booksPane.setVisible(false);
        membersPane.setVisible(false);
        checkoutsPane.setVisible(false);
    }

    private void setActiveButton(Button activeBtn) {
        // Remove active class from all buttons
        btnDashboard.getStyleClass().remove("active");
        btnBooks.getStyleClass().remove("active");
        btnMembers.getStyleClass().remove("active");
        btnCheckouts.getStyleClass().remove("active");

        // Add active class to current button
        activeBtn.getStyleClass().add("active");
    }

    private void updateDashboardStats() {
        List<Livre> livres = bibliotheque.getLivres();
        List<Abonne> abonnes = bibliotheque.getAbonnes();
        List<Emprunt> emprunts = bibliotheque.getEmprunts();

        int totalBooks = livres.stream().mapToInt(Livre::getQuantite).sum();
        int availableBooks = livres.stream().mapToInt(Livre::getQuantiteDisponible).sum();
        int checkedOutBooks = totalBooks - availableBooks;
        int activeMembers = abonnes.size();
        long overdueBooks = emprunts.stream().filter(e -> !e.isRendu() && e.estEnRetard()).count();

        double usageRate = totalBooks > 0 ? (double) checkedOutBooks / totalBooks * 100 : 0;

        txtTotalBooks.setText(String.valueOf(totalBooks));
        txtActiveMembers.setText(String.valueOf(activeMembers));
        txtBooksCheckedOut.setText(String.valueOf(checkedOutBooks));
        txtOverdue.setText(String.valueOf(overdueBooks));
        txtUsageRate.setText(String.format("%.1f%%", usageRate));
        txtAvailableBooks.setText(String.valueOf(availableBooks));
    }

    private void refreshBooksTable() {
        ObservableList<Livre> booksList = FXCollections.observableArrayList(bibliotheque.getLivres());
        booksTable.setItems(booksList);
    }

    private void refreshMembersTable() {
        ObservableList<Abonne> membersList = FXCollections.observableArrayList(bibliotheque.getAbonnes());
        membersTable.setItems(membersList);
    }

    private void refreshCheckoutsTable() {
        List<Emprunt> emprunts = bibliotheque.getEmprunts().stream()
                .filter(e -> !e.isRendu())
                .collect(Collectors.toList());
        ObservableList<Emprunt> checkoutsList = FXCollections.observableArrayList(emprunts);
        checkoutsTable.setItems(checkoutsList);
    }

    private void showAddBookDialog() {
        Dialog<Livre> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un nouveau livre");
        dialog.setHeaderText("Saisissez les informations du livre");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titre = new TextField();
        TextField auteur = new TextField();
        TextField quantite = new TextField();

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titre, 1, 0);
        grid.add(new Label("Auteur:"), 0, 1);
        grid.add(auteur, 1, 1);
        grid.add(new Label("Quantité:"), 0, 2);
        grid.add(quantite, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    ISBNGenerator isbnGen = new ISBNGenerator();
                    String isbn = isbnGen.genererIdentifiant();
                    int qty = Integer.parseInt(quantite.getText());
                    return Livre.creerLivre(titre.getText(), auteur.getText(), qty, qty, isbn);
                } catch (Exception e) {
                    showAlert("Erreur", "Données invalides: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Livre> result = dialog.showAndWait();
        result.ifPresent(livre -> {
            bibliotheque.ajouterLivre(livre);
            refreshBooksTable();
            updateDashboardStats();
            showAlert("Succès", "Livre ajouté avec succès!");
        });
    }

    private void showAddMemberDialog() {
        Dialog<Abonne> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un nouveau membre");
        dialog.setHeaderText("Saisissez les informations du membre");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nom = new TextField();
        TextField prenom = new TextField();
        TextField email = new TextField();

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nom, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenom, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(email, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    IDGenerateur idGen = new IDGenerateur();
                    String id = idGen.genererIdentifiant();
                    return Abonne.creerAbonne(nom.getText(), prenom.getText(), email.getText(), id);
                } catch (Exception e) {
                    showAlert("Erreur", "Données invalides: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Abonne> result = dialog.showAndWait();
        result.ifPresent(abonne -> {
            bibliotheque.ajouterAbonne(abonne);
            refreshMembersTable();
            updateDashboardStats();
            showAlert("Succès", "Membre ajouté avec succès!");
        });
    }

    private void showCheckoutDialog() {
        Dialog<Emprunt> dialog = new Dialog<>();
        dialog.setTitle("Nouveau prêt");
        dialog.setHeaderText("Sélectionnez le livre et le membre");

        ButtonType checkoutButtonType = new ButtonType("Emprunter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(checkoutButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<Livre> livreCombo = new ComboBox<>();
        ComboBox<Abonne> abonneCombo = new ComboBox<>();

        livreCombo.getItems().addAll(bibliotheque.getLivres().stream()
                .filter(Livre::verifierDisponibilite)
                .collect(Collectors.toList()));
        livreCombo.setConverter(new StringConverter<Livre>() {
            @Override
            public String toString(Livre livre) {
                return livre != null ? livre.getTitre() + " - " + livre.getAuteur() : "";
            }

            @Override
            public Livre fromString(String string) {
                return null;
            }
        });

        abonneCombo.getItems().addAll(bibliotheque.getAbonnes());
        abonneCombo.setConverter(new StringConverter<Abonne>() {
            @Override
            public String toString(Abonne abonne) {
                return abonne != null ? abonne.getPrenom() + " " + abonne.getNom() : "";
            }

            @Override
            public Abonne fromString(String string) {
                return null;
            }
        });

        grid.add(new Label("Livre:"), 0, 0);
        grid.add(livreCombo, 1, 0);
        grid.add(new Label("Membre:"), 0, 1);
        grid.add(abonneCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == checkoutButtonType) {
                try {
                    Livre livre = livreCombo.getValue();
                    Abonne abonne = abonneCombo.getValue();
                    if (livre != null && abonne != null) {
                        return bibliotheque.emprunterLivre(livre.getID(), abonne.getEmail());
                    }
                } catch (Exception e) {
                    showAlert("Erreur", "Erreur lors de l'emprunt: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Emprunt> result = dialog.showAndWait();
        result.ifPresent(emprunt -> {
            refreshCheckoutsTable();
            refreshBooksTable();
            updateDashboardStats();
            showAlert("Succès", "Prêt enregistré avec succès!");
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
