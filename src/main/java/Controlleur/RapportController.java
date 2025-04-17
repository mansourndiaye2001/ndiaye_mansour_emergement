package Controlleur;

import DAO.Prof_db;
import Entity.Emergement;
import Entity.Utulisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class RapportController {

    private final Prof_db profDb = new Prof_db();

    // TableView des émargements
    @FXML private TableView<Emergement> tableRapports;
    @FXML private TableColumn<Emergement, Long> colId;
    @FXML private TableColumn<Emergement, LocalDateTime> colDate;
    @FXML private TableColumn<Emergement, String> colProfesseur;
    @FXML private TableColumn<Emergement, String> colCours;
    @FXML private TableColumn<Emergement, String> colStatut;

    // Graphiques
    @FXML private Pane barChartPane;
    @FXML private LineChart<Number, Number> lineChart;
    @FXML private Pane doughnutChartPane;

    @FXML
    public void initialize() {
        // Charger les tableaux
        loadTableRapports();
        loadBarChart();
        loadLineChart();
        loadDoughnutChart();
    }

    private void loadTableRapports() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date_emergement"));
        colProfesseur.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getProfesseur().getNom()
                )
        );
        colCours.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getCours().getNom()
                )
        );
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        List<Emergement> list = profDb.List_emergement();
        ObservableList<Emergement> data = FXCollections.observableArrayList(list);
        tableRapports.setItems(data);
    }

    private void loadBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        xAxis.setLabel("Professeur");
        yAxis.setLabel("Nombre d’émargements");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Présences par professeur");

        List<Object[]> data = profDb.getEmargementsParProfesseur();
        for (Object[] row : data) {
            Utulisateur prof = (Utulisateur) row[0];
            Long total = (Long) row[1];
            series.getData().add(new XYChart.Data<>(prof.getNom(), total));
        }

        barChart.getData().add(series);
        barChartPane.getChildren().add(barChart);
    }

    private void loadLineChart() {
        lineChart.getData().clear(); // s'assurer que le graphique est vide avant d'ajouter
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Évolution des émargements");

        List<Object[]> data = profDb.getEmargementsParDate();
        int index = 0;
        for (Object[] row : data) {
            LocalDateTime date = (LocalDateTime) row[0];
            Long count = (Long) row[1];
            series.getData().add(new XYChart.Data<>(index++, count));
        }

        lineChart.getData().add(series);
    }

    private void loadDoughnutChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Taux de présence par cours");

        List<Object[]> data = profDb.getTauxPresenceParCours(); // à créer dans Prof_db

        for (Object[] row : data) {
            String cours = (String) row[0];
            Long presents = (Long) row[1];
            Long total = (Long) row[2];
            double taux = (double) presents / total * 100;

            pieChart.getData().add(new PieChart.Data(cours + " (" + (int) taux + "%)", taux));
        }

        doughnutChartPane.getChildren().add(pieChart);
    }

   private Stage stage;
    @FXML

    public  void LoadEmergementPdf(ActionEvent event){
        PDFController pdfController = new PDFController();
        List<Emergement> emergements = profDb.List_emergement();
        try {
            pdfController.generatePDF(emergements ,  stage  ,"La Liste des Emergements");
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void LoadExportExcel(ActionEvent event){
        try{
            List<Emergement> emergements = profDb.List_emergement();
            ExportExcel exportExcel = new ExportExcel();
            exportExcel.exportEmergementsToExcel(emergements,stage);

        }
        catch (Exception ex){
            ex.printStackTrace();;
        }
    }
}
