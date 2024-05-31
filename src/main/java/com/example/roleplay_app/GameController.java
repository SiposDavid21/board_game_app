package com.example.roleplay_app;

import com.example.roleplay_app.model.Hero;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameController {

    @FXML
    private Label turnCounterLabel;

    //player 1
    @FXML
    private Label player1NameLabel;

    @FXML
    private Button player1AttackButton;

    @FXML
    private Button player1DefenseButton;

    @FXML
    private Button player1PotionButton;

    @FXML
    private TextField player1HealthField;

    @FXML
    private TextField player1DamageField;

    //player 2
    @FXML
    private Label player2NameLabel;

    @FXML
    private Button player2AttackButton;

    @FXML
    private Button player2DefenseButton;

    @FXML
    private Button player2PotionButton;

    @FXML
    private TextField player2HealthField;

    @FXML
    private TextField player2DamageField;

    @FXML
    private ListView<String> combatLogListView;

    private int turnCounter = 0;
    private boolean firstPlayerTurn;

    private Hero player1, player2;

    private List<String> combatLog;

    @FXML
    private void initialize() {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Hero> heroes;
        try {
            heroes = objectMapper.readValue(getClass().getResource("/json/game1.json"), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initGameData(heroes);

        updateHealthFields();
        updateButtonStates();

        updateTurnCounter();
    }

    @FXML
    private void onPlayer1Attack() {
        if (firstPlayerTurn) {
            attack(player1, player2);

            if (player2.getHealth() <= 0) {
                showEndOfGameDialog(player1.getName() + " is the winner!");
                generateInput(player1);
            } else {
                changeActivePlayer();
                updateButtonStates();
            }
        }
    }

    @FXML
    private void onPlayer2Attack() {
        if (!firstPlayerTurn) {
            attack(player2, player1);

            if (player1.getHealth() <= 0) {
                showEndOfGameDialog(player2.getName() + " is the winner!");
                generateInput(player2);
            } else {
                changeActivePlayer();
                updateButtonStates();

                updateTurnCounter();
            }
        }
    }

    @FXML
    private void onPlayer1Defense() {
        if (firstPlayerTurn) {
            defense(player1);

            changeActivePlayer();
            updateButtonStates();
        }
    }

    @FXML
    private void onPlayer2Defense() {
        if (!firstPlayerTurn) {
            defense(player2);

            changeActivePlayer();
            updateTurnCounter();
            updateButtonStates();
        }
    }

    @FXML
    private void onPlayer1Potion() {
        if (firstPlayerTurn && !player1.getUsedPotion()) {
            combatLog.add("Turn " + turnCounter + ": " + "Player 1 drink a potion");
            updateCombatLog();

            player1.usePotion();

            updateHealthFields();

            changeActivePlayer();
            updateButtonStates();
        }
    }

    @FXML
    private void onPlayer2Potion() {
        if (!firstPlayerTurn && !player2.getUsedPotion()) {
            combatLog.add("Turn " + turnCounter + ": " + "Player 2 drink a potion");
            updateCombatLog();
            updateTurnCounter();

            player2.usePotion();

            updateHealthFields();

            changeActivePlayer();
            updateButtonStates();
        }
    }

    private void initGameData(List<Hero> heroes) {
        combatLog = new ArrayList<>();
        firstPlayerTurn = true;

        Collections.shuffle(heroes);

        Hero randomHero1 = heroes.getFirst();
        Hero randomHero2 = heroes.getLast();

        player1 = randomHero1;
        player1NameLabel.setText(player1.getName());
        player1DamageField.setText(player1.getFullDamage().getMinDamage() + " - " + player1.getFullDamage().getMaxDamage());

        player2 = randomHero2;
        player2NameLabel.setText(player2.getName());
        player2DamageField.setText(player2.getFullDamage().getMinDamage() + " - " + player2.getFullDamage().getMaxDamage());
    }

    private Integer generateRandomDamage(int minDamage, int maxDamage) {
        Random random = new Random();
        return random.nextInt(maxDamage - minDamage + 1) + minDamage;
    }

    private void attack(Hero attacker, Hero target) {
        Double damage = Double.valueOf(generateRandomDamage(attacker.getFullDamage().getMinDamage(), attacker.getFullDamage().getMaxDamage()));

        if (target.getIsDefending()) {
            if (target.getFullArmor() > 800) {
                damage *= 0.30;
            } else if (target.getFullArmor() > 500) {
                damage *= 0.50;
            } else if (target.getFullArmor() > 300) {
                damage *= 0.70;
            } else {
                damage *= 0.90;
            }



            damage = (Math.floor(damage * 100) / 100);
            target.setIsDefending(false);
        }

        target.setHealth(Math.floor((target.getHealth() - damage) * 100) / 100);

        updateHealthFields();

        combatLog.add("Turn " + turnCounter + ": " + attacker.getName() + " attacks, dealing " + damage + " damage. " + " Opponent remaining health: " + target.getHealth());
        updateCombatLog();
    }

    private void defense(Hero hero) {
        hero.setIsDefending(true);

        combatLog.add("Turn " + turnCounter + ": " + hero.getName() + " defends");
        updateCombatLog();
    }

    private void changeActivePlayer() {
        firstPlayerTurn = !firstPlayerTurn;
    }

    private void updateTurnCounter() {
        turnCounter++;
        turnCounterLabel.setText("Turn: " + turnCounter);
    }

    private void updateButtonStates() {
        player1AttackButton.setDisable(!firstPlayerTurn);
        player1DefenseButton.setDisable(!firstPlayerTurn);

        if (player1.getUsedPotion()) {
            player1PotionButton.setDisable(true);
        } else {
            player1PotionButton.setDisable(!firstPlayerTurn);
        }

        player2AttackButton.setDisable(firstPlayerTurn);
        player2DefenseButton.setDisable(firstPlayerTurn);

        if (player2.getUsedPotion()) {
            player2PotionButton.setDisable(true);
        } else {
            player2PotionButton.setDisable(firstPlayerTurn);
        }
    }

    private void updateHealthFields() {
        player1HealthField.setText(String.valueOf(player1.getHealth()));
        player2HealthField.setText(String.valueOf(player2.getHealth()));
    }

    private void updateCombatLog() {
        combatLogListView.getItems().clear();
        combatLogListView.getItems().addAll(combatLog);

        combatLogListView.scrollTo(combatLog.size() - 1);
    }

    private void showEndOfGameDialog(String winner) {

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);  //felugró ablak
        dialogStage.setTitle("End of Game");


        Label winnerLabel = new Label("Winner: " + winner);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(actionEvent -> {


            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

            MainApplication.getPrimaryStage().close();
        });


        VBox vbox = new VBox(winnerLabel, exitButton);
        vbox.setSpacing(20);
        vbox.setMinSize(200, 100);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");


        Scene dialogScene = new Scene(vbox);
        dialogStage.setScene(dialogScene);


        dialogStage.showAndWait();
    }
    private void generateInput(Hero winner) {
        ObjectMapper objectMapper = new ObjectMapper();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        objectMapper.setDefaultPrettyPrinter(prettyPrinter);

        try {
            combatLog.add(winner.getName() + " level up!");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("./combatLog.json"), combatLog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}