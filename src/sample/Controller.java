package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.Bot.AlfaBettaChoice;
import sample.model.Game;

import java.io.IOException;

public class Controller {
    private final Text[] HOLES = new Text[10];
    private final Game game = new Game();
    @FXML
    private ToggleGroup difficultyToggle;
    @FXML
    private Text hole1, hole2, hole3, hole4, hole5, hole6, hole7, hole8, hole9, hole10, myContainer, opponentContainer;
    @FXML
    private Text status;

    private int hardLevel = 5;

    @FXML
    protected void initialize() {
        HOLES[0] = hole1;
        HOLES[1] = hole2;
        HOLES[2] = hole3;
        HOLES[3] = hole4;
        HOLES[4] = hole5;
        HOLES[5] = hole6;
        HOLES[6] = hole7;
        HOLES[7] = hole8;
        HOLES[8] = hole9;
        HOLES[9] = hole10;
        showField();
    }

    private void showField() {
        myContainer.setText(game.getStoreHouseOfFirstPlayer() + "");
        opponentContainer.setText(game.getStoreHouseOfSecondPlayer() + "");
        int[] pits = game.getPits();
        for (int i = 0; i < HOLES.length; i++) {
            HOLES[i].setText(pits[i] + "");
        }
        if (!game.isNoEnd()) {
            String statusString;
            switch (game.whoWon()) {
                case 1 -> statusString = "You won!";
                case 0 -> statusString = "It's draw";
                case -1 -> statusString = "Bot won(";
                default -> statusString = "";
            }
            status.setText(statusString);
        }
    }

    @FXML
    private void holeHasBeenChosen(MouseEvent event) {
        if (!game.isNoEnd() || game.isTurnOfSecond()) return;
        Object source = event.getSource();
        if (source instanceof Circle) {
            Node parent = ((Circle) source).getParent();
            if (parent instanceof StackPane) {
                source = ((StackPane) parent).getChildren().stream().filter((node -> node instanceof Text)).findFirst()
                        .orElseThrow();
            }
        }
        if (source instanceof Text) {
            Text node = (Text) source;
            for (int i = 0; i < 5; i++) {
                if (HOLES[i] == node) {
                    if (i == 2) {
                        status.setText("Use arrows to set direction for middle");
                        return;
                    }
                    makeTurn(i);
                    break;
                }
            }
        }
    }

    @FXML
    private void newGame() {
        game.newGame();
        showField();
        status.setText("");
    }

    @FXML
    private void closeWindow() {
        ((Stage) (status.getScene().getWindow())).close();
    }

    @FXML
    private void setHard() {
        hardLevel = 7;
        status.setText("Difficulty set to hard");
    }

    @FXML
    private void setMedium() {
        hardLevel = 5;
        status.setText("Difficulty set to medium");
    }

    @FXML
    private void setEasy() {
        hardLevel = 3;
        status.setText("Difficulty set to easy");
    }

    @FXML
    private void middleDirection(KeyEvent event) {
        switch (event.getCode()) {
            case RIGHT -> {
                status.setText("Counter clockwise direction");
                game.setMiddleDirectionRight();
            }
            case LEFT -> {
                status.setText("Clockwise direction");
                game.setMiddleDirectionLeft();
            }
            default -> {
                return;
            }
        }
        makeTurn(2);
    }

    private void makeTurn(int numberOfHole) {
        try {
            game.makeTurn(numberOfHole);
        } catch (UnsupportedOperationException ex) {
            status.setText(ex.getMessage());
            return;
        }
        status.setText("Turn of bot");
        showField();
        if (!game.isNoEnd()) return;
        new Thread(() -> {
            try {
                //2 seconds to watch your decision
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AlfaBettaChoice.makeBestStep(game, hardLevel);
            Platform.runLater(() -> {
                status.setText("Turn of player");
                showField();
            });
        }).start();
    }

    @FXML
    private void showAbout() {
        new Thread(() -> Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                Parent about = FXMLLoader.load(getClass().getResource("about.fxml"));
                stage.setTitle("Alemungula - About");
                stage.setScene(new Scene(about));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }
}
