package sample.model;

import java.util.Arrays;

public class Game {
    private final int[] pits;
    private final String nameOfFirstPlayer;
    private final String nameOfSecondPlayer;
    private int storeHouseOfFirstPlayer;
    private int storeHouseOfSecondPlayer;
    private boolean isTurnOfSecond;
    private boolean isDirectionLeft;

    public Game(String nameOfFirstPlayer, String nameOfSecondPlayer) {
        this.pits = new int[10];
        Arrays.fill(pits, 5);
        this.nameOfFirstPlayer = nameOfFirstPlayer;
        this.nameOfSecondPlayer = nameOfSecondPlayer;
    }

    public void setMiddleDirectionLeft() {
        this.isDirectionLeft = true;
    }

    public void setMiddleDirectionRight() {
        this.isDirectionLeft = false;
    }

    public void makeTurn(int pitNumber) {
        if (pitNumber >= 5 || pitNumber < 0)
            throw new UnsupportedOperationException("Pit number should be between 5(exclusive) and 0(inclusive)");

        switch (pitNumber) {
            case 0, 1:
                isDirectionLeft = true;
            case 3, 4:
                isDirectionLeft = false;
        }
        int[] opponentPreviousStatusOfPits = new int[5];
        if (isTurnOfSecond) {
            pitNumber += 5;
            System.arraycopy(pits, 0, opponentPreviousStatusOfPits, 0, 5);
        } else System.arraycopy(pits, 5, opponentPreviousStatusOfPits, 0, 5);

        int numberOfSeeds = pits[pitNumber];
        pits[pitNumber] = 0;

        for (int i = 0; i < numberOfSeeds; i++) {
            if (isDirectionLeft) pitNumber--;
            else pitNumber++;
            pitNumber %= 10;
            pits[pitNumber]++;
        }
        checkOfValidWinMoments(opponentPreviousStatusOfPits);
        isTurnOfSecond = !isTurnOfSecond;
    }

    private void checkOfValidWinMoments(int[] opponentPreviousStatusOfPits) {
        boolean addedToStoreHousePrevious = false;
        if (isTurnOfSecond) {
            if (isDirectionLeft) {
                for (int i = 4; i >= 0; i--) {
                    boolean isAddedToStoreHouseOnCurrentStep = isAddedToStoreHouseOnCurrentStepForSecondPlayer(opponentPreviousStatusOfPits, i);
                    if (addedToStoreHousePrevious && !isAddedToStoreHouseOnCurrentStep)
                        return;
                    addedToStoreHousePrevious = isAddedToStoreHouseOnCurrentStep;
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    boolean isAddedToStoreHouseOnCurrentStep = isAddedToStoreHouseOnCurrentStepForSecondPlayer(opponentPreviousStatusOfPits, i);
                    if (addedToStoreHousePrevious && !isAddedToStoreHouseOnCurrentStep)
                        return;
                    addedToStoreHousePrevious = isAddedToStoreHouseOnCurrentStep;
                }
            }
        } else {
            if (isDirectionLeft) {
                for (int i = 9; i >= 5; i--) {
                    boolean isAddedToStoreHouseOnCurrentStep = isAddedToStoreHouseOnCurrentStepForFirstPlayer(opponentPreviousStatusOfPits, i);
                    if (addedToStoreHousePrevious && !isAddedToStoreHouseOnCurrentStep)
                        return;
                    addedToStoreHousePrevious = isAddedToStoreHouseOnCurrentStep;
                }
            } else {
                for (int i = 5; i < 10; i++) {
                    boolean isAddedToStoreHouseOnCurrentStep = isAddedToStoreHouseOnCurrentStepForFirstPlayer(opponentPreviousStatusOfPits, i);
                    if (addedToStoreHousePrevious && !isAddedToStoreHouseOnCurrentStep)
                        return;
                    addedToStoreHousePrevious = isAddedToStoreHouseOnCurrentStep;
                }
            }
        }
    }

    private boolean isAddedToStoreHouseOnCurrentStepForSecondPlayer(int[] opponentPreviousStatusOfPits, int i) {
        boolean isAddedToStoreHouseOnCurrentStep = false;
        if (opponentPreviousStatusOfPits[i] != pits[i] && (pits[i] == 2 || pits[i] == 4)) {
            isAddedToStoreHouseOnCurrentStep = true;
            storeHouseOfSecondPlayer += pits[i];
            pits[i] = 0;
        }
        return isAddedToStoreHouseOnCurrentStep;
    }

    private boolean isAddedToStoreHouseOnCurrentStepForFirstPlayer(int[] opponentPreviousStatusOfPits, int i) {
        boolean isAddedToStoreHouseOnCurrentStep = false;
        if (opponentPreviousStatusOfPits[i - 5] != pits[i] && (pits[i] == 2 || pits[i] == 4)) {
            isAddedToStoreHouseOnCurrentStep = true;
            storeHouseOfFirstPlayer += pits[i];
            pits[i] = 0;
        }
        return isAddedToStoreHouseOnCurrentStep;
    }

    public int[] getPits() {
        return pits;
    }

    public int getStoreHouseOfFirstPlayer() {
        return storeHouseOfFirstPlayer;
    }

    public int getStoreHouseOfSecondPlayer() {
        return storeHouseOfSecondPlayer;
    }

    public boolean isTurnOfSecond() {
        return isTurnOfSecond;
    }
}
