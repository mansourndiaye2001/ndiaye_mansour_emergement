package Controlleur;

import DAO.Prof_db;
import Entity.Cours;
import Entity.Emergement;
import Entity.Salle;
import Entity.Utulisateur;
import Outils.load;
import Services.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.gestion_presence_professeurs.HelloController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfController implements Initializable {
    public Label message;
    public TableColumn<Cours, String> colNom;
    public TableColumn<Cours, String> colDescription;
    public TableColumn<Cours, Integer> colID;
    public TableColumn<Emergement, Integer> colID_r;
    public TableColumn<Emergement, DatePicker> colDate_r;
    public TableColumn<Emergement, String> colStatut;
    public TableColumn<Emergement, Cours> colCours;
    public TableColumn<Cours, Integer> colHeureFin;
    public TableColumn<Cours, Salle> colSalle;
    public TableColumn<Cours, Utulisateur> colProfesseur;
    public TableColumn<Cours, Integer> colHeureDebut;

    @FXML
    public TableView<Cours> tableCours;
    public Prof_db profDb = new Prof_db();
    public TableView<Emergement> tableEmergement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colSalle.setCellValueFactory(new PropertyValueFactory<>("salle"));
        colProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
        colID_r.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colCours.setCellValueFactory(new PropertyValueFactory<>("cours"));
        colDate_r.setCellValueFactory(new PropertyValueFactory<>("date_emergement"));

        message.setText(HelloController.userparams);
        loadCours();
        loadEmergement();
    }

    // Déconnexion
    public void deconnexion(ActionEvent event) {
        try {
            load.load(event, "Page de connexion ", "/org/example/gestion_presence_professeurs/Hello-view.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Charger les cours du jour
    private void loadCours() {
        List<Cours> coursList = profDb.getCoursDuJour(HelloController.user);

        if (coursList == null) { // Vérifie si la liste est null
            coursList = new ArrayList<>(); // Initialise une liste vide pour éviter NullPointerException
        }

        if (coursList.isEmpty()) {
            Notification.NotifiError("Information", "Monsieur, vous n'avez aucun cours à ce jour, merci !");
        } else {
            ObservableList<Cours> coursObservableList = FXCollections.observableArrayList(coursList);
            tableCours.setItems(coursObservableList);
        }
    }

    private void loadEmergement() {
        List<Emergement> emergements = profDb.list(HelloController.user);
        if (emergements == null) {
            emergements = new ArrayList<>();
        }
        ObservableList<Emergement> emergementObservableList = FXCollections.observableArrayList(emergements);
        tableEmergement.setItems(emergementObservableList);
    }

    public void taclikP(MouseEvent mouseEvent) {
        Cours cours = tableCours.getSelectionModel().getSelectedItem();
        if (cours != null) {
            System.out.println("Cours sélectionné : " + cours.getNom());
        }
    }

    public void Emerger(ActionEvent event) {
        Cours cours = tableCours.getSelectionModel().getSelectedItem();
        if (cours != null) {
            boolean ok = profDb.emerger(HelloController.user, cours);
            if (ok) {
                //Notification.NotifiSuccess("", "Vous êtes marqué présent");
                loadEmergement();
            } else {
               // Notification.NotifiError("", "Vous êtes marqué absent");
                loadEmergement();
            }
        } else {
            Notification.NotifiError("Erreur", "Veuillez sélectionner un cours avant d'émerger.");
        }
    }
}
