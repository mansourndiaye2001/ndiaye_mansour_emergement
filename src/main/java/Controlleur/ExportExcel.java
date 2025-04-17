package Controlleur;

import Entity.Emergement;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportExcel {

    public void exportEmergementsToExcel(List<Emergement> emergements, Stage stage) {
        if (emergements == null || emergements.isEmpty()) {
            System.out.println("La liste est vide.");
            return;
        }

        // Créer un nouveau classeur Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Emergements");

        // Créer une ligne d'en-têtes
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Date",  "Statut", "Professeur"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Remplir les données
        for (int i = 0; i < emergements.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Emergement emergement = emergements.get(i);

            // Remplir chaque cellule avec les données de l'objet Emergement
            row.createCell(0).setCellValue(emergement.getId()); // Par exemple pour ID
            row.createCell(1).setCellValue(emergement.getDate_emergement().toLocalDate().toString()); // Remplacer par votre champ
            row.createCell(2).setCellValue(emergement.getStatut()); // Remplacer par votre champ
            row.createCell(4).setCellValue(emergement.getProfesseur().getNom() +" "+emergement.getProfesseur().getPrenom());
        }

        // Ouvrir un FileChooser pour sélectionner où enregistrer le fichier Excel
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                System.out.println("Fichier Excel créé avec succès.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Fermer le classeur
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
