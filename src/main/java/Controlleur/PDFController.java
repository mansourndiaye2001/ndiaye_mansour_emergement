package Controlleur;

import com.lowagie.text.DocumentException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class PDFController {
    public static <T> void generatePDF(List<T> items, Stage stage, String title) throws IOException, DocumentException {
        // Créer le dialogue de sauvegarde
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );
        fileChooser.setInitialFileName("Emergement.pdf");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            // Générer le HTML pour la table0
            String html = generateTableHTML(items, title);

            // Créer le PDF
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(html);
                renderer.layout();
                renderer.createPDF(outputStream);
            }
        }
    }

    private static <T> String generateTableHTML(List<T> items, String title) {
        if (items == null || items.isEmpty()) {
            return "<html><body><p>Aucune donnée disponible</p></body></html>";
        }

        StringBuilder html = new StringBuilder();
        html.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; }
                    h1 { 
                        text-align: center;
                        color: #333;
                        margin: 20px 0;
                    }
                    table { 
                        width: 100%;
                        border-collapse: collapse;
                        margin: 20px 0;
                    }
                    th, td { 
                        border: 1px solid #ddd;
                        padding: 8px;
                        text-align: left;
                    }
                    th {
                        background-color: #f5f5f5;
                        font-weight: bold;
                    }
                    tr:nth-child(even) {
                        background-color: #f9f9f9;
                    }
                </style>
            </head>
            <body>
            """);

        // Ajouter le titre
        if (title != null && !title.isEmpty()) {
            html.append("<h1>").append(title).append("</h1>");
        }

        html.append("<table><thead><tr>");

        // Obtenir les champs de la classe
        Field[] fields = items.get(0).getClass().getDeclaredFields();

        // Ajouter les en-têtes
        for (Field field : fields) {
            field.setAccessible(true);
            html.append("<th>").append(field.getName()).append("</th>");
        }

        html.append("</tr></thead><tbody>");

        // Ajouter les données
        for (T item : items) {
            html.append("<tr>");
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(item);
                    html.append("<td>")
                            .append(value != null ? value.toString() : "")
                            .append("</td>");
                } catch (IllegalAccessException e) {
                    html.append("<td></td>");
                }
            }
            html.append("</tr>");
        }

        html.append("</tbody></table></body></html>");

        return html.toString();
    }
}
