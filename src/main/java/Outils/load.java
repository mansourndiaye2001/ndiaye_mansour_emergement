package Outils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class load {
    private void loadPage(ActionEvent event ,  String title, String url)throws IOException{
        ((Node)event.getSource()).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();


    }
    private void loadSubPage(ActionEvent event ,  String title, String url)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();


    }
    public static void load(ActionEvent event ,  String title, String url)throws IOException{
        new load().loadPage(event,title,url);

    }
    public static void loadSup(ActionEvent event ,  String title, String url)throws IOException{
        new load().loadSubPage(event,title,url);
    }
}