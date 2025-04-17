package Controlleur;

import DAO.CoursImplemts;
import DAO.Prof_db;
import DAO.SalleImplemt;
import DAO.UserImplent;
import Entity.Cours;
import Entity.Salle;
import Entity.Utulisateur;
import Services.Notification;
import Services.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CoursControlleur implements Initializable {

    public  ComboBox<Salle>comboSalle;
    public  ComboBox<Utulisateur>comboProfesseur;
    public  ComboBox<String>comboJour;
    public TextField txtNom;
    public TextArea  txtDescription;
    SalleImplemt salleImplemt = new SalleImplemt();
    UserImplent userImplent = new UserImplent();
    CoursImplemts coursImplemts = new CoursImplemts();
   public TableColumn<Cours,String>colNom;
    public TableColumn<Cours,String>colDescription;
    public TableColumn<Cours,Integer>colID;
    public TableColumn<Cours,Integer>colHeureDebut;
    public TableColumn<Cours,Integer>colHeureFin;
    public  TableColumn<Cours,Salle>colSalle;
    public  TableColumn<Cours, String>colJour;
    public  TableColumn<Cours,Utulisateur>colProfesseur;
    public  TableColumn<Cours , DatePicker> colDate;
    @FXML
    private DatePicker datePickerCours;

    @FXML
    private Spinner<Integer> spinnerHeureDebut;

    @FXML
    private Spinner<Integer> spinnerMinuteDebut;

    @FXML
    private Spinner<Integer> spinnerHeureFin;

    @FXML
    private Spinner<Integer> spinnerMinuteFin;

    public TableView<Cours>tableCours;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colSalle.setCellValueFactory(new PropertyValueFactory<>("salle"));
        colProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
        colJour.setCellValueFactory(new PropertyValueFactory<>("jour"));


        LoadSalle();
        LoadProf();
        loadCours();

    }


    public  void LoadSalle(){
        List<Salle> salleList = salleImplemt.list();
        ObservableList<Salle> salleObservableList = FXCollections.observableArrayList();
        salleObservableList.addAll(salleList);
        comboSalle.setItems(salleObservableList);
    }
    public  void LoadProf(){
        List<Utulisateur> utulisateurList = userImplent.list_Prof();
        ObservableList<Utulisateur> utulisateurObservableList = FXCollections.observableArrayList();
        utulisateurObservableList.addAll(utulisateurList);
        comboProfesseur.setItems(utulisateurObservableList);
    }
    public  void AddCours(ActionEvent event){
        if(
                (!Validation.ValidateText(txtNom.getText()))
                        && (!Validation.ValidateText(txtDescription.getText()))
                        && (!Validation.ValidateText(txtNom.getText()))
                        && (!Validation.ValidateText(String.valueOf(comboSalle.getValue())))
                        && (!Validation.ValidateText(String.valueOf(comboProfesseur.getValue())))
        )

        {
            Notification.NotifiError(" Erreur","Veuillez remplir tous les champs");
        }else{
            if(
                    (!Validation.ValidateText(txtNom.getText()))
                            || (!Validation.ValidateText(txtDescription.getText()))
                            || (!Validation.ValidateText(txtNom.getText()))
                            || (!Validation.ValidateText(String.valueOf(comboSalle.getValue())))
                            || (!Validation.ValidateText(String.valueOf(comboProfesseur.getValue())))
            )
            {
                Notification.NotifiError(" Erreur","Tous les champs sont obligatoire");
            }else{
                Cours cours = new Cours();
                cours.setNom(txtNom.getText().toUpperCase().toUpperCase());
                cours.setDescription(txtDescription.getText().toUpperCase().toUpperCase());
                cours.setSalle(comboSalle.getValue());
                cours.setProfesseur(comboProfesseur.getValue());
                cours.setHeureDebut(LocalTime.of(spinnerHeureDebut.getValue(), spinnerMinuteDebut.getValue()));
                cours.setHeureFin(LocalTime.of(spinnerHeureFin.getValue(), spinnerMinuteFin.getValue()));
                cours.setJour(comboJour.getValue());
                coursImplemts.addCours(cours, comboJour.getValue());

                loadCours();
            }

        }




    }
    public void loadCours() {
        List<Cours> coursList = coursImplemts.list();
        ObservableList<Cours> coursObservableList = FXCollections.observableArrayList();
        coursObservableList.addAll(coursList);
        tableCours.setItems(coursObservableList);
    }
    public void taclikP(MouseEvent mouseEvent) {
        Cours cours = tableCours.getSelectionModel().getSelectedItem();
        txtNom.setText(cours.getNom());
        txtDescription.setText(cours.getDescription());
        cours.setHeureFin(LocalTime.of(spinnerHeureFin.getValue(), spinnerMinuteFin.getValue()));
       cours.setHeureDebut(LocalTime.of(spinnerHeureDebut.getValue(), spinnerMinuteDebut.getValue()));
        comboProfesseur.setValue(cours.getProfesseur());
        comboSalle.setValue(cours.getSalle());




    }
    public void DeleteCours(ActionEvent event){
        Cours cours = tableCours.getSelectionModel().getSelectedItem();
        coursImplemts.delete(cours);
        loadCours();
        Notification.NotifiSuccess("Information","Suppression Avec Succes !!");

    }
}
