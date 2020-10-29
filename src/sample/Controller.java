package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import sample.model.Game;

public class Controller {
    private final Text[] HOLES = new Text[10];
    private final Game game = new Game();
    @FXML
    private ToggleGroup difficultyToggle;
    @FXML
    private Text hole1, hole2, hole3, hole4, hole5, hole6, hole7, hole8, hole9, hole10, myContainer, opponentContainer;

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
    }
}
