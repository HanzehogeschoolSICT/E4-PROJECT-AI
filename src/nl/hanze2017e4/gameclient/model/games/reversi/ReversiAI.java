package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.SETTINGS;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static nl.hanze2017e4.gameclient.model.games.reversi.ReversiAiCalculate.determineLegalMoves;
import static nl.hanze2017e4.gameclient.model.games.reversi.ReversiAiCalculate.determinePossibleMoves;

public class ReversiAI {

    private ReversiBoard sourceBoard;
    private Player player1;
    private Player player2;

    /**
     * AI object that contains logic for determining the best move.
     *
     * @param sourceBoard The current board as seen by both players.
     * @param player1     The player that performs the next move.
     * @param player2     The opponent.
     */
    public ReversiAI(ReversiBoard sourceBoard, Player player1, Player player2) {
        this.sourceBoard = sourceBoard;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Method that determines the best move.
     * <p>
     * [1] = Determine all legal moves including score for the first generation, and find the best move along them.
     * [2] = True if we want to accumulate the score of more then one generation of moves.
     * [3] = Create for each legal move an thread that searches deeper for more generations.
     * [4] = If the threads exceed the timeout, we return the best move of the first generation. Otherwise we find the best move for more generations.
     * [5] = Return the best move if only one generation is needed.
     *
     * @return The position for the best possible move determined by this algorithm. -1 if an error occurs.
     */
    @SuppressWarnings("ConstantConditions")
    public int getBestMove() {
        //[1]
        ArrayList<ReversiMove> firstGenLegalMoves = getFirstGenLegalMoves();
        ReversiMove safeMove = ReversiAiCalculate.determineBestMove(firstGenLegalMoves, player1, player2, 1);
        try {
            //[2]
            if (SETTINGS.GENERATION_LIMIT != 1) {
                ArrayList<ReversiMove> processedMoves = new ArrayList<>();
                ExecutorService executorService = Executors.newFixedThreadPool(100);

                //[3]
                for (ReversiMove reversiMove : firstGenLegalMoves) {
                    executorService.execute(() -> processedMoves.add(reversiMove.createNextGen()));
                }
                executorService.shutdown();

                //[4]
                if (!executorService.awaitTermination(SETTINGS.SERVER_TURN_TIME - 1, TimeUnit.SECONDS)) {
                    System.out.println("TIMEOUT");
                    return safeMove.getMove();
                } else {
                    return ReversiAiCalculate.determineBestMove(firstGenLegalMoves, player1, player2, 1).getMove();
                }

            }
            //[5]
            return safeMove.getMove();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Creates the moves of the first generation.
     * @return Arraylist containing of legal moves in the first generation.
     */
    private ArrayList<ReversiMove> getFirstGenLegalMoves() {
        ArrayList<ReversiMove> possibleMoves = determinePossibleMoves(sourceBoard, player1, player2, 1);
        return determineLegalMoves(possibleMoves, sourceBoard, player1, 1);
    }
}
