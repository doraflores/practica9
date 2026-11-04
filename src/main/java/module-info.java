module org.example.practica9 {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens org.example.practica9.Vista to javafx.graphics;
    exports org.example.practica9.Vista;
    exports org.example.practica9;
    // exports org.example.practica9.Modelo;
    // opens org.example.practica9.Modelo to javafx.fxml;
}