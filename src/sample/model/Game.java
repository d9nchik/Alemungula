package sample.model;

import java.util.Arrays;

public class Game {
    private final int[] pits;
    private int storeHouseOfFirstPlayer;
    private int storeHouseOfSecondPlayer;
    private boolean isTurnOfSecond;
    private boolean isDirectionLeft;
    private int blockedPit = -1;

    //    TODO: add rule for one stone
//    TODO: make wins more real
    public Game() {
        this.pits = new int[10];
        Arrays.fill(pits, 5);
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

//        choosing direction
        switch (pitNumber) {
            case 0, 1 -> isDirectionLeft = true;
            case 3, 4 -> isDirectionLeft = false;
        }

        int[] opponentPreviousStatusOfPits = new int[5];
        if (isTurnOfSecond) {
            pitNumber += 5;
            System.arraycopy(pits, 0, opponentPreviousStatusOfPits, 0, 5);
        } else System.arraycopy(pits, 5, opponentPreviousStatusOfPits, 0, 5);
        if (pitNumber == blockedPit)
            throw new UnsupportedOperationException("This Pit is blocked");
        if (pits[pitNumber] == 0)
            throw new UnsupportedOperationException("Your turn can not be applied to empty pit");

        blockedPit = blockingVariant(pitNumber);
        spreadSeeds(pitNumber);
        if (blockedPit == -1) {
            checkOfValidWinMoments(opponentPreviousStatusOfPits);
        }
        isTurnOfSecond = !isTurnOfSecond;
    }

    private void spreadSeeds(int pitNumber) {
        int numberOfSeeds = pits[pitNumber];
        pits[pitNumber] = 0;
        for (int i = 0; i < numberOfSeeds; i++) {
            if (isDirectionLeft) pitNumber = pitNumber == 0 ? 9 : pitNumber - 1;
            else pitNumber = (pitNumber + 1) % 10;
            pits[pitNumber]++;
        }
    }

    public int blockingVariant(int pitNumber) {
        if (pits[pitNumber] == 1) {
            switch (pitNumber) {
                case 0, 4, 5, 9 -> {
                    if (isDirectionLeft) pitNumber = pitNumber == 0 ? 9 : pitNumber - 1;
                    else pitNumber = (pitNumber + 1) % 10;
                    if (pits[pitNumber] == 0)
                        return pitNumber;
                }
            }
        }
        return -1;
    }

    private void checkOfValidWinMoments(int[] opponentPreviousStatusOfPits) {
        boolean addedToStoreHousePrevious = false;
        if (isTurnOfSecond) {
            if (isDirectionLeft) {
                for (int i = 0; i < 5; i++) {
                    boolean isAddedToStoreHouseOnCurrentStep = isAddedToStoreHouseOnCurrentStepForSecondPlayer(opponentPreviousStatusOfPits, i);
                    if (addedToStoreHousePrevious && !isAddedToStoreHouseOnCurrentStep)
                        return;
                    addedToStoreHousePrevious = isAddedToStoreHouseOnCurrentStep;
                }
            } else {
                for (int i = 4; i >= 0; i--) {
                    boolean isAddedToStoreHouseOnCurrentStep = isAddedToStoreHouseOnCurrentStepForSecondPlayer(opponentPreviousStatusOfPits, i);
                    if (addedToStoreHousePrevious && !isAddedToStoreHouseOnCurrentStep)
                        return;
                    addedToStoreHousePrevious = isAddedToStoreHouseOnCurrentStep;
                }
            }
        } else {
            if (isDirectionLeft) {
                for (int i = 5; i < 10; i++) {
                    boolean isAddedToStoreHouseOnCurrentStep = isAddedToStoreHouseOnCurrentStepForFirstPlayer(opponentPreviousStatusOfPits, i);
                    if (addedToStoreHousePrevious && !isAddedToStoreHouseOnCurrentStep)
                        return;
                    addedToStoreHousePrevious = isAddedToStoreHouseOnCurrentStep;
                }
            } else {
                for (int i = 9; i >= 5; i--) {
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

    public boolean isEnd() {
        int seedsLeft = 50 - storeHouseOfFirstPlayer - storeHouseOfSecondPlayer;
        return seedsLeft == 0 || seedsLeft == 1;
    }

    public int whoWon() {
        return Integer.compare(storeHouseOfFirstPlayer, storeHouseOfSecondPlayer);
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
