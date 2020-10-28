package sample.model.Bot;

import sample.model.Game;

public class AlfaBettaChoice {
    private final int recursionDeep;
    private final Game gameStatus;
    private final Integer best;
    private Integer currentBest;

    public AlfaBettaChoice(int recursionDeep, Game gameStatus) {
        this(recursionDeep, gameStatus, null);
    }

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

    public static int bestStep(Game gameStatus, int recursionDeep) {
        Integer best = null;
        int choice = -1;
        for (int i = 0; i < 5; i++) {
            Game gameStatusOnStep = (Game) gameStatus.clone();
            try {
                gameStatusOnStep.makeTurn(i);
                AlfaBettaChoice alfaBettaChoice = new AlfaBettaChoice(recursionDeep - 1, gameStatusOnStep, best);

                if (best == null || alfaBettaChoice.getPrice() > best) {
                    choice = i;
                    best = alfaBettaChoice.getPrice();
                }
            } catch (UnsupportedOperationException ignored) {
            }
        }
        return choice;
    }

    private void search() {
        if (recursionDeep == 0) return;
        for (int i = 0; i < 5; i++) {
            Game gameStatusOnStep = (Game) gameStatus.clone();
            try {
                gameStatusOnStep.makeTurn(i);
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
