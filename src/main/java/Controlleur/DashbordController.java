package Controlleur;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.gestion_presence_professeurs.HelloController;

import java.io.IOException;

public class DashbordController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button btnAccueil, btnUtilisateurs, btnCours, btnPresences, btnRapports, btnNotifications, btnSalle, btnDeconnexion;

    @FXML
    private Label message;

    @FXML
    private ProgressIndicator progressCircle;

    @FXML
    public void initialize() {
        // Affiche le message personnalisé (ex : nom d'utilisateur)
        message.setText(HelloController.userparams);

        // Contrôle d'accès selon le rôle utilisateur
        if (HelloController.user.getRole().getId() == 1) {
            btnUtilisateurs.setDisable(false);
            btnSalle.setDisable(true);
            btnCours.setDisable(true);
            btnNotifications.setDisable(true);
            btnRapports.setDisable(true);
            btnPresences.setDisable(true);
        } else if (HelloController.user.getRole().getId() == 2) {
            btnUtilisateurs.setDisable(true);
        }

        // Actions des boutons
        btnAccueil.setOnAction(e -> loadPage("/org/example/gestion_presence_professeurs/accueil.fxml"));
        btnUtilisateurs.setOnAction(e -> loadPage("/org/example/gestion_presence_professeurs/utilisateur.fxml"));
        btnSalle.setOnAction(e -> loadPage("/org/example/gestion_presence_professeurs/salle.fxml"));
        btnCours.setOnAction(e -> loadPage("/org/example/gestion_presence_professeurs/cours.fxml"));
        btnPresences.setOnAction(e -> loadPage("/org/example/gestion_presence_professeurs/presences.fxml"));
        btnRapports.setOnAction(e -> loadPage("/org/example/gestion_presence_professeurs/rapport.fxml"));
        btnNotifications.setOnAction(e -> loadPage("/org/example/gestion_presence_professeurs/notification.fxml"));
        btnDeconnexion.setOnAction(e -> deconnexion());
    }

    private void loadPage(String fxmlFile) {
        // Affiche le cercle de chargement
        progressCircle.setVisible(true);
        progressCircle.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        // Simule un délai de chargement (1 seconde ici)
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            try {
                Parent newPage = FXMLLoader.load(getClass().getResource(fxmlFile));
                mainPane.setCenter(newPage);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cache le cercle après le chargement
                progressCircle.setVisible(false);
            }
        });
        pause.play();
    }

    private void deconnexion() {
        try {
            Stage currentStage = (Stage) btnDeconnexion.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestion_presence_professeurs/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
