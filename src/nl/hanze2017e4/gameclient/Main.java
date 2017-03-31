package nl.hanze2017e4.gameclient;

import nl.hanze2017e4.gameclient.games.master.Board;
import nl.hanze2017e4.gameclient.games.master.Player;

public class Main {

    public static void main(String[] args) {
        System.out.println("BKE:");
        Player p1 = new Player("p1", "X");
        Player p2 = new Player("p2", "O");
        Board board = new Board(3, 3, p1, p2);

        board.placeMove(p1, 5);
        board.placeMove(p2, 8);
        System.out.println(board);
        System.out.println("\nReversi:");

        Player p11 = new Player("p11", "B");
        Player p22 = new Player("p22", "W");
        Board board2 = new Board(8, 8, p11, p22);

        board2.placeMove(p11, 5);
        board2.placeMove(p22, 8);

        System.out.println(board2);
    }
}
