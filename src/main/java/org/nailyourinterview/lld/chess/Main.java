package org.nailyourinterview.lld.chess;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.GameStatus;
import org.nailyourinterview.lld.chess.model.Player;
import org.nailyourinterview.lld.chess.service.Game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of white player : ");
        Player white = new Player(scanner.next(), Color.WHITE);

        System.out.print("Enter the name of black player : ");
        Player black = new Player(scanner.next(), Color.BLACK);

        Game game = new Game(white, black);
        System.out.println("Enter moves like: e2 e4   (type 'resign' to give up)");

        while (game.getStatus() == GameStatus.ACTIVE) {
            game.getBoard().printBoard();
            Player player = game.getCurrentPlayer();
            System.out.print(player.getName() + " (" + player.getColor() + ") move : ");

            if (!scanner.hasNext()) {
                break;
            }
            String from = scanner.next();
            if (from.equals("resign")) {
                game.resign();
                break;
            }
            String to = scanner.next();
            game.makeMove(from, to);
        }

        game.getBoard().printBoard();
        System.out.println("Result: " + game.getStatus());
    }
}
