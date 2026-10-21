module org.example.practica9 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.practica9 to javafx.fxml;
    exports org.example.practica9;
}