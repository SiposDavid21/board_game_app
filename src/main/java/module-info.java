module com.example.javafxgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires static lombok;
    requires com.fasterxml.jackson.databind;

    opens com.example.board_game_app to javafx.fxml;
    exports com.example.board_game_app;
    exports com.example.board_game_app.model;
    opens com.example.board_game_app.model to javafx.fxml;
}