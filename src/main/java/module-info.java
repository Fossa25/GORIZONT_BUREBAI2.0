module com.example.proburok {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;

    requires java.desktop;


    opens com.example.proburok to javafx.fxml;
    exports com.example.proburok;
}