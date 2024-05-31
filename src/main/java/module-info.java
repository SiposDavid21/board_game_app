module com.example.javafxgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires static lombok;
    requires com.fasterxml.jackson.databind;

    opens com.example.roleplay_app to javafx.fxml;
    exports com.example.roleplay_app;
    exports com.example.roleplay_app.model;
    opens com.example.roleplay_app.model to javafx.fxml;
}