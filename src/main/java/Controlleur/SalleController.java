package Controlleur;

import DAO.SalleImplemt;
import Entity.Role;
import Entity.Salle;
import Entity.Utulisateur;
import Services.Notification;
import Services.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SalleController  implements Initializable {
    public TextField txtNomSalle;
    public TableColumn<Salle, Integer> colID;
    public TableColumn<Salle, String> colNom;
    public TableView<Salle>tableSalle;
    SalleImplemt salleImplemt = new SalleImplemt();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        LoadSalle();
    }
    public  void AddSalle(ActionEvent event){
        String nomSalle = txtNomSalle.getText();
        if((!Validation.ValidateText(nomSalle))){
            Notification.NotifiError(" ","Veuillez saisir le nom de la  classe ");

        }else{
            if( ! salleImplemt.existeSalleParNom(nomSalle)){
                Salle salle = new Salle();
                salle.setNom(nomSalle.toUpperCase());
                salleImplemt.add(salle);
                Notification.NotifiSuccess(" ","Ajout Avec Succes !!");
                LoadSalle();
                txtNomSalle.setText("");

            }else{
                Notification.NotifiError("","Cette Salle existe deja !! ");
            }
        }
    }
    public  void LoadSalle(){
        List<Salle> salleList = salleImplemt.list();
        ObservableList<Salle> salleObservableList = FXCollections.observableArrayList();
        salleObservableList.addAll(salleList);
        tableSalle.setItems(salleObservableList);
    }
    public void taclikP(MouseEvent mouseEvent) {
        Salle salle = tableSalle.getSelectionModel().getSelectedItem();
        txtNomSalle.setText(salle.getNom());



    }
    public void deleteSalle(ActionEvent event) {
        Salle salle = tableSalle.getSelectionModel().getSelectedItem();
        salleImplemt.delete(salle);
        LoadSalle();
       // Notification.NotifiSuccess("","Salle Supprime Avec Succes !!");
    }
    public  void EditSalle(ActionEvent event){
        Salle salle = tableSalle.getSelectionModel().getSelectedItem();
        String nomSalle = txtNomSalle.getText();
        if((!Validation.ValidateText(nomSalle))){
            Notification.NotifiError("","Veuillez saisir le nom de la  classe ");

        }else{
            if( ! salleImplemt.existeSalleParNom(nomSalle)){

                salle.setNom(nomSalle.toUpperCase());
                salleImplemt.update(salle);
                Notification.NotifiSuccess("","Modification Avec Succes !!");
                tableSalle.refresh();
                LoadSalle();


            }else{
                Notification.NotifiError("","Cette Salle existe deja !! ");
            }
        }

    }
}
