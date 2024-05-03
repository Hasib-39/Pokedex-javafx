module org.example.pokedex {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens org.example.pokedex to javafx.fxml;
    exports org.example.pokedex;
}