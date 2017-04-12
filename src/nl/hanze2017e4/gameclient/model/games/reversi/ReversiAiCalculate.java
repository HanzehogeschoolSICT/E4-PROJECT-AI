package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ReversiAiCalculate {

    public static ArrayList<ReversiMove> determinePossibleMoves(ReversiBoard board, Player movePlacer, Player opponent, int generation) {
        Set<Integer> possibleMoveSet = new HashSet<>();

        for (int i = 0; i < 63; i++) {
            int[] temp = {i - 9, i - 7, i + 9, i + 7, i + 1, i - 1, i - 8, i + 8};

            if (board.getPlayerAtPos(i) == null) {
                for (int aTemp : temp) {
                    if (aTemp >= 0 && aTemp <= 63) {
                        if (board.getPlayerAtPos(aTemp) == opponent) {
                            possibleMoveSet.add(i);
                        }
                    }
                }
            }
        }

        ArrayList<ReversiMove> possibleMoves = new ArrayList<>();
        TerminalPrinter.println("AI", ":cyan,n:Generation " + generation + " Possible Moves", possibleMoveSet.toString());

        for (Integer legalMove : possibleMoveSet) {
            possibleMoves.add(new ReversiMove(movePlacer, opponent, legalMove, board, generation));
        }

        return possibleMoves;
    }

    public static ArrayList<ReversiMove> determineLegalMoves(ArrayList<ReversiMove> possibleMoves, ReversiBoard board, Player movePlacer, int generation) {
        int prevScore = board.getScore(movePlacer);
        ArrayList<ReversiMove> legalMoves = new ArrayList<>();

        for (ReversiMove possibleMove : possibleMoves) {
            if (possibleMove.getScore() > prevScore + 1) {
                legalMoves.add(possibleMove);
            }
        }

        TerminalPrinter.println("AI", ":cyan,n:Generation " + generation + " Legal Moves", legalMoves.toString());
        return legalMoves;
    }

    public static ReversiMove determineBestMove(ArrayList<ReversiMove> legalMoves, ReversiBoard sourceBoard, Player playerMoves, Player otherPlayer, int generation) {

        if (legalMoves.size() > 0) {
            ReversiMove bestMove = legalMoves.get(0);
            int bestValue = 0;

            for (ReversiMove legalMove : legalMoves) {
                int thisScore = legalMove.getScore();
                if (thisScore > bestValue) {
                    bestValue = thisScore;
                    bestMove = legalMove;
                }
            }
            return bestMove;
        } else {
            return new ReversiMove(playerMoves, otherPlayer, -1, sourceBoard, generation + 1);
        }
    }

}
