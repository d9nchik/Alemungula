package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.Bot.AlfaBettaChoice;
import sample.model.Game;

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
                case 1 -> statusString = "Won first!";
                case 0 -> statusString = "It's draw";
                case -1 -> statusString = "Won second";
                default -> statusString = "";
            }
            status.setText(statusString);
        }
    }

    @FXML
    private void holeHasBeenChosen(MouseEvent event) {
        if (!game.isNoEnd()) {
            return;
        }
        final Object source = event.getSource();
        if (source instanceof Text) {
            Text node = (Text) source;
            for (int i = 0; i < 5; i++) {
                if (HOLES[i] == node) {
                    try {
                        game.makeTurn(i);
                    } catch (UnsupportedOperationException ex) {
                        status.setText(ex.getMessage());
                        return;
                    }
                    status.setText("Turn of bot");
                    showField();
                    if (!game.isNoEnd()) return;
                    new Thread(() -> {
                        AlfaBettaChoice.makeBestStep(game, hardLevel);
                        Platform.runLater(() -> {
                            status.setText("Turn of player");
                            showField();
                        });
                    }).start();
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
}
