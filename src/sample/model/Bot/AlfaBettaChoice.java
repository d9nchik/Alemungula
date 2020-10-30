package sample.model.Bot;

import sample.model.Game;

public class AlfaBettaChoice {
    private final int recursionDeep;
    private final Game gameStatus;
    private final Integer best;
    private Integer currentBest;

    public AlfaBettaChoice(int recursionDeep, Game gameStatus, Integer best) {
        this.recursionDeep = recursionDeep;
        this.gameStatus = gameStatus;
        this.best = best;
        this.currentBest = best;
        search();
    }

    private static int heuristic(Game gameStatus) {
        return gameStatus.getStoreHouseOfSecondPlayer() - gameStatus.getStoreHouseOfFirstPlayer();
    }

    public static void makeBestStep(Game gameStatus, int recursionDeep) {
        Integer best = null;
        int choice = -1;
        for (int i = 0; i < 6; i++) {
            Game gameStatusOnStep = (Game) gameStatus.clone();
            try {
                makeTurn(i, gameStatusOnStep);
                AlfaBettaChoice alfaBettaChoice = new AlfaBettaChoice(recursionDeep - 1, gameStatusOnStep, best);

                if (best == null || alfaBettaChoice.getPrice() > best) {
                    choice = i;
                    best = alfaBettaChoice.getPrice();
                }
            } catch (UnsupportedOperationException ignored) {
            }
        }
        makeTurn(choice, gameStatus);
    }

    private static void makeTurn(int i, Game gameStatusOnStep) {
        if (i == 2) {
            gameStatusOnStep.setMiddleDirectionRight();
        }
        if (i == 5) {
            gameStatusOnStep.setMiddleDirectionLeft();
            gameStatusOnStep.makeTurn(2);
        } else
            gameStatusOnStep.makeTurn(i);
    }

    private void search() {
        if (recursionDeep == 0) return;
        for (int i = 0; i < 6; i++) {
            Game gameStatusOnStep = (Game) gameStatus.clone();
            try {
                makeTurn(i, gameStatusOnStep);
                int price;
                if (!gameStatusOnStep.isNoEnd()) {
                    price = heuristic(gameStatus);
                } else {
                    AlfaBettaChoice alfaBettaChoice = new AlfaBettaChoice(recursionDeep - 1, gameStatusOnStep, currentBest);
                    price = alfaBettaChoice.getPrice();
                }
                //TODO: optimize
                if (currentBest == null) {
                    currentBest = price;
                }
                if (!gameStatus.isTurnOfSecond()) {
                    if (best != null && price > best) {
                        currentBest = price;
                        return;
                    }
                    if (price < currentBest)
                        currentBest = price;
                } else if (price > currentBest) {
                    currentBest = price;
                }
            } catch (UnsupportedOperationException ignored) {
            }
        }
    }

    public int getPrice() {
        if (recursionDeep == 0 || !gameStatus.isNoEnd()) {
            return heuristic(gameStatus);
        }
        return currentBest;
    }
}
