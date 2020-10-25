package sample.model;

import java.util.Scanner;

public class ConsoleVariant {
    public static void main(String[] args) {
        Game game = new Game();
        while (game.isNoEnd()) {
            System.out.println("First player is playing");
            showField(game);
            makeTurn(game);

            if (game.isNoEnd()) {
                System.out.println("Second player is playing");
                showField(game);
                makeTurn(game);
            }
        }
        System.out.println("Won: " + game.whoWon());
    }

    private static void showField(Game game) {
        for (int i = game.getPits().length / 2 - 1; i >= 0; i--) {
            System.out.print(game.getPits()[i] + "\t");
        }
        System.out.println();
        for (int i = game.getPits().length / 2; i < game.getPits().length; i++) {
            System.out.print(game.getPits()[i] + "\t");
        }
        System.out.println();
        System.out.println("First player storage: " + game.getStoreHouseOfFirstPlayer());
        System.out.println("Second player storage: " + game.getStoreHouseOfSecondPlayer());
    }

    private static void makeTurn(Game game) {
        while (true) {
            System.out.print("Enter number of hole: ");
            final Scanner scanner = new Scanner(System.in);
            int number = scanner.nextInt();

            if (number == 2) {
                System.out.print("Enter move vector(0 - clockwise, other otherwise): ");
                if (scanner.nextInt() == 0) game.setMiddleDirectionLeft();
                else game.setMiddleDirectionRight();
            }
            try {
                game.makeTurn(number);
                break;
            } catch (UnsupportedOperationException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Try again!");
            }
        }
    }
}
