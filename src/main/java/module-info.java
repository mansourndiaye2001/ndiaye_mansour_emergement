module org.example.gestion_presence_professeurs {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires  org.hibernate.orm.core;
    requires  static  lombok;
    //requires  TrayTester;
    requires  static  TrayTester;
    requires  itextpdf;
    requires javax.mail.api;

    requires flying.saucer.pdf;
    requires  com.github.librepdf.openpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    opens Entity to org.hibernate.orm.core;
   exports Entity;


    opens org.example.gestion_presence_professeurs to javafx.fxml;
    exports org.example.gestion_presence_professeurs;
    exports Controlleur to javafx.fxml;
    opens  Controlleur to  javafx.fxml;
}