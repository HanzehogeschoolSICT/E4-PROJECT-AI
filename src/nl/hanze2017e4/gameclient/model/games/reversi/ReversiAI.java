package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReversiAI {

    private final int lookForwardMoves;
    private ReversiBoard sourceBoard;
    private Player player1;
    private Player player2;
    private int timeForThinking;
    /**
     * @param sourceBoard
     * @param player1
     * @param player2
     * @param lookForwardMoves Number of moves to look ahead. Including opponent moves.
     */
    public ReversiAI(ReversiBoard sourceBoard, Player player1, Player player2, int lookForwardMoves, int timeForThinking) {
        this.sourceBoard = sourceBoard;
        this.player1 = player1;
        this.player2 = player2;
        this.lookForwardMoves = lookForwardMoves;
        this.timeForThinking = timeForThinking;

    }

    public static ArrayList<ReversiMove> determinePossibleMoves(ReversiBoard board, Player movePlacer, Player opponent, int lookForwardMoves) {
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
        TerminalPrinter.println("AI", ":cyan,n:Generation " + lookForwardMoves + " Possible Moves", possibleMoveSet.toString());

        for (Integer legalMove : possibleMoveSet) {
            possibleMoves.add(new ReversiMove(movePlacer, opponent, legalMove, board, lookForwardMoves - 1));
        }

        return possibleMoves;
    }

    public static ArrayList<ReversiMove> determineLegalMoves(ArrayList<ReversiMove> possibleMoves, ReversiBoard board, Player movePlacer, int lookForwardMoves) {
        int prevScore = board.getScore(movePlacer);
        ArrayList<ReversiMove> legalMoves = new ArrayList<>();

        for (ReversiMove possibleMove : possibleMoves) {
            if (possibleMove.getScore() > prevScore + 1) {
                legalMoves.add(possibleMove);
            }
        }

        TerminalPrinter.println("AI", ":cyan,n:Generation " + lookForwardMoves + " Legal Moves", legalMoves.toString());
        return legalMoves;
    }

    public static ReversiMove determineBestMove(ArrayList<ReversiMove> legalMoves, ReversiBoard sourceBoard, Player playerMoves, Player otherPlayer, int lookForwardMoves) {
        if (lookForwardMoves < 0) {
            TerminalPrinter.println("AI", ":cyan,n:FINAL DECIDING DETERMING BEST MOVE", legalMoves.toString());
        }

        if (legalMoves.size() > 0) {
            ReversiMove bestMove = legalMoves.get(0);
			int bestValue = 0;

            for (ReversiMove legalMove : legalMoves) {
                int thisScore = legalMove.getAllGenMoveScore();
                if (thisScore > bestValue) {
                    bestValue = thisScore;
                    bestMove = legalMove;
                }
            }
            return bestMove;
		} else {
            return new ReversiMove(playerMoves, otherPlayer, -1, sourceBoard, lookForwardMoves - 1);
        }
	}

    public int getBestMove() {
        ArrayList<ReversiMove> gen1LegalMoves = determineLegalMoves(determinePossibleMoves(sourceBoard, player1, player2, lookForwardMoves), sourceBoard, player1, lookForwardMoves);
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        try {
            for (ReversiMove reversiMove : gen1LegalMoves) {
                if (reversiMove.getLookForwardSteps() > 0) {
                    executorService.execute(reversiMove);
                }
            }
            executorService.shutdown();
            executorService.awaitTermination(timeForThinking, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return determineBestMove(gen1LegalMoves, sourceBoard, player1, player2, -1).getMove();
    }
}
