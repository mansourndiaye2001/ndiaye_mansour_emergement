package Controlleur;

import DAO.Notification_db;
import Entity.Cours;
import Entity.Emergement;
import Entity.Notification;
import Entity.Utulisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {

    public  TableColumn<Notification, Utulisateur>colProfesseur;
    public  TableColumn<Notification, DatePicker>colDate;
    public  TableColumn<Notification, Integer>colID;
    public TableView<Notification>tableNotifacion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date_envoie"));
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        load();


    }
    public  void load(){
        Notification_db notificationDb = new Notification_db();
        List<Notification> notificationList = notificationDb.List();

        if (notificationList == null) {
            notificationList = new ArrayList<>();
        }


        ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();
        notificationObservableList.addAll(notificationList);


        tableNotifacion.setItems(notificationObservableList);
    }
}
