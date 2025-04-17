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

public class PresencesController implements Initializable {
    public Label message;
    public TableColumn<Emergement,Integer>colID_r;
    public TableColumn<Emergement,DatePicker>colDate_r;
    public TableColumn<Emergement,String>colStatut;
    public TableColumn<Emergement,Cours>colCours;
    public  TableColumn<Cours, Utulisateur>colProfesseur;

    @FXML


    public TableView<Cours>tableCours;
    public Prof_db profDb = new Prof_db();
    public  TableView<Emergement>tableEmergement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
        colID_r.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colCours.setCellValueFactory(new PropertyValueFactory<>("cours"));
        colDate_r.setCellValueFactory(new PropertyValueFactory<>("date_emergement"));

        message.setText(HelloController.userparams);

        loadEmergement();

    }
    public  void deconnexion(ActionEvent event){
        try {
            load.load(event,"Page de connexion ","/org/example/gestion_presence_professeurs/Hello-view.fxml");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadEmergement() {
        List<Emergement> emergements = profDb.List_emergement();

        if (emergements == null) {
            emergements = new ArrayList<>();
        }


        ObservableList<Emergement> emergementObservableList = FXCollections.observableArrayList();
        emergementObservableList.addAll(emergements);


        tableEmergement.setItems(emergementObservableList);
    }

    public void taclikP(MouseEvent mouseEvent) {
        Cours cours = tableCours.getSelectionModel().getSelectedItem();
    }


}
