package org.example.gestion_presence_professeurs;

import DAO.UserImplent;
import Entity.Utulisateur;
import Outils.load;
import Services.Notification;
import Services.Validation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class HelloController {
   @FXML public TextField emailField;
   @FXML public TextField passwordField;
   @FXML public ProgressIndicator progressIndicator;
   @FXML public Button loginButton;

   UserImplent userImplent = new UserImplent();
   public static String userparams;
   public static Utulisateur user = null;
   int role_id;

   public void VerifEmailAndPassword(ActionEvent event) {
      // Vérifier que les champs ne sont pas vides
      if((!Validation.ValidateText(emailField.getText()))
              && !(Validation.ValidateText(passwordField.getText()))) {
         Notification.NotifiError("", "Veuillez remplir les champs ");
         return;
      }

      if((!Validation.ValidateText(emailField.getText()))
              || (!Validation.ValidateText(passwordField.getText()))) {
         Notification.NotifiError("", "Tous les champs sont obligatoires ");
         return;
      }

      // Afficher l'indicateur de progression et désactiver le bouton
      progressIndicator.setVisible(true);
      loginButton.setDisable(true);

      // Création d'une tâche en arrière-plan pour la vérification des identifiants
      Task<Utulisateur> authTask = new Task<Utulisateur>() {
         @Override
         protected Utulisateur call() throws Exception {
            // Simuler un court délai pour voir l'indicateur de progression (à supprimer en production)
            Thread.sleep(1000);
            // Vérification des identifiants
            return userImplent.verifierConnexion(emailField.getText(), passwordField.getText());
         }
      };

      // Gestion de la fin de la tâche
      authTask.setOnSucceeded(e -> {
         Utulisateur utulisateur = authTask.getValue();

         // Masquer l'indicateur de progression
         progressIndicator.setVisible(false);
         loginButton.setDisable(false);

         // Traitement du résultat
         if(utulisateur != null) {
            processSuccessfulLogin(utulisateur, event);
         } else {
            Notification.NotifiError("", "Veuillez vérifier les paramètres de connexion");
         }
      });

      // Gestion des erreurs
      authTask.setOnFailed(e -> {
         progressIndicator.setVisible(false);
         loginButton.setDisable(false);
         Notification.NotifiError("Erreur", "Une erreur est survenue pendant la connexion");
         authTask.getException().printStackTrace();
      });

      // Démarrer la tâche en arrière-plan
      new Thread(authTask).start();
   }

   private void processSuccessfulLogin(Utulisateur utulisateur, ActionEvent event) {
      user = utulisateur;
      role_id = utulisateur.getRole().getId();

      try {
         switch (role_id) {
            case 1:
               userparams = "Admin " + utulisateur.getNom() + " " + utulisateur.getPrenom();
               load.load(event, "Accueil", "/org/example/gestion_presence_professeurs/accueil.fxml");
               break;
            case 2:
               userparams = utulisateur.getNom() ;
               load.load(event, "Accueil", "/org/example/gestion_presence_professeurs/accueil.fxml");
               break;
            case 3:
               userparams = "Professeur " + utulisateur.getNom() + " " + utulisateur.getPrenom();
               load.load(event, "Professeurs ", "/org/example/gestion_presence_professeurs/prof.fxml");
               break;
         }
      } catch (Exception ex) {
         ex.printStackTrace();
         Notification.NotifiError("Erreur", "Erreur lors du chargement de l'interface");
      }
   }
}