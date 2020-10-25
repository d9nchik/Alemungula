package sample.model;

public class Game {
    private final int[] pits;
    private final String nameOfFirstPlayer;
    private final String nameOfSecondPlayer;
    private int storeHouseOfFirstPlayer;
    private int storeHouseOfSecondPlayer;

    public Game(String nameOfFirstPlayer, String nameOfSecondPlayer) {
        this.pits = new int[10];

        this.nameOfFirstPlayer = nameOfFirstPlayer;
        this.nameOfSecondPlayer = nameOfSecondPlayer;
    }
}
