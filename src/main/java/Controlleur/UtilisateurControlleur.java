package Controlleur;

import DAO.RoleImplents;
import DAO.UserImplent;
import Entity.Role;
import Entity.Utulisateur;
import Services.Notification;
import Services.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UtilisateurControlleur implements Initializable {
    RoleImplents roleImplents = new RoleImplents();
    public ComboBox<Role> comboRole;
    public TextField txtNom;
    public TextField txtPrenom;
    public TextField txtEmail;
    public PasswordField txtPassword;
    public TableColumn<Utulisateur, String> colNom;
    public TableColumn<Utulisateur, String> colPrenom;
    public TableColumn<Utulisateur, String> colEmail;
    public TableColumn<Utulisateur, String> colPassword;
    public TableColumn<Utulisateur, Role> colRole;
    public TableView<Utulisateur> tableView;
    public TableColumn<Utulisateur, Integer> id;
    int idUser;
    UserImplent userImplent = new UserImplent();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
       // colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        loadRole();
        loadUser();
    }

    public void loadRole() {
        List<Role> roleList = roleImplents.list();
        ObservableList<Role> roleObservableList = FXCollections.observableArrayList();
        roleObservableList.addAll(roleList);
        comboRole.setItems(roleObservableList);

    }

    public void AddUtilisateur(ActionEvent event) {
        if (
                (!Validation.ValidateText(txtNom.getText()))
                        && (!Validation.ValidateText(txtPrenom.getText()))
                        && (!Validation.ValidateText(txtPassword.getText()))
                        && (!Validation.isValidEmail(txtEmail.getText()))
        ) {
            Notification.NotifiError("Erreur ","Veuillez remplir tous les champs");

        } else if (
                (!Validation.ValidateText(txtNom.getText()))
                        || (!Validation.ValidateText(txtPrenom.getText()))
                        || (!Validation.ValidateText(txtPassword.getText()))
                        || (!Validation.isValidEmail(txtEmail.getText()))
        ) {
            Notification.NotifiError("Erreur ","Nom , Prenom , Password ou Email incorrect");
        } else {
            Utulisateur utulisateur = new Utulisateur();
            utulisateur.setEmail(txtEmail.getText().trim());
            utulisateur.setNom(txtNom.getText().toUpperCase());
            utulisateur.setPassword(txtPassword.getText());
            utulisateur.setRole(comboRole.getValue());
            utulisateur.setPrenom(txtPrenom.getText().toUpperCase());
            userImplent.add(utulisateur);
           Notification.NotifiSuccess("","Ajout Avec Succes !!");
            loadUser();
            Annuler(event);
        }
    }

    public void loadUser() {
        List<Utulisateur> utulisateurList = userImplent.list();
        ObservableList<Utulisateur> utulisateurObservableList = FXCollections.observableArrayList();
        utulisateurObservableList.addAll(utulisateurList);
        tableView.setItems(utulisateurObservableList);
    }

    public void taclikP(MouseEvent mouseEvent) {
        Utulisateur utulisateur = tableView.getSelectionModel().getSelectedItem();
        txtNom.setText(utulisateur.getNom());
        txtPrenom.setText(utulisateur.getPrenom());
        txtPassword.setText(utulisateur.getPassword());
        txtEmail.setText(utulisateur.getEmail());
        comboRole.setValue(utulisateur.getRole());


    }

    public void deletUser(ActionEvent event) {
        Utulisateur utulisateur = tableView.getSelectionModel().getSelectedItem();
        userImplent.delete(utulisateur);
        loadUser();
       Notification.NotifiSuccess("","Suppression Avec Succes !!");
    }

    public void editUser(ActionEvent event) {
        Utulisateur utulisateur = tableView.getSelectionModel().getSelectedItem();
        if (
                (!Validation.ValidateText(txtNom.getText()))
                        && (!Validation.ValidateText(txtPrenom.getText()))
                        && (!Validation.ValidateText(txtPassword.getText()))
                        && (!Validation.isValidEmail(txtEmail.getText()))
        ) {
            Notification.NotifiError("Erreur","Veuillez remplir tous les champs");

        } else if (
                (!Validation.ValidateText(txtNom.getText()))
                        || (!Validation.ValidateText(txtPrenom.getText()))
                        || (!Validation.ValidateText(txtPassword.getText()))
                        || (!Validation.isValidEmail(txtEmail.getText()))
        ) {
            Notification.NotifiError("Erreur ", "Nom , Prenom , Password ou Email incorrect");
        } else {
            utulisateur.setEmail(txtEmail.getText().trim());
            utulisateur.setNom(txtNom.getText().toUpperCase());
            utulisateur.setPassword(txtPassword.getText());
            utulisateur.setRole(comboRole.getValue());
            utulisateur.setPrenom(txtPrenom.getText().toUpperCase());
            userImplent.update(utulisateur);
            tableView.refresh();
            loadUser();
            Notification.NotifiSuccess(" ", "Modification de l'Utilsateur Avec Succes !!");


        }
    }
    public  void Annuler(ActionEvent event){
        txtEmail.setText("");
        txtPassword.setText("");
        txtPrenom.setText("");
        txtNom.setText("");


    }

}
