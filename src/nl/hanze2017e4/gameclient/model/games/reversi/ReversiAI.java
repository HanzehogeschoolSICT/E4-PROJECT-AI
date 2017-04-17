package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.SETTINGS;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

public class ReversiAI {

    private ReversiBoard sourceBoard;
    private Player playerMoves;
    private Player opponent;
    private int generation;
    private ArrayList<ReversiMove> possibleMoves = new ArrayList<>();
    private ArrayList<ReversiMove> legalMoves = new ArrayList<>();
    private ReversiMove bestMove;

    /**
     * AI object that contains logic for determining the best move.
     *
     * @param sourceBoard The current board as seen by both players.
     * @param playerMoves     The player that performs the next move.
     * @param opponent     The opponent.
     */
    public ReversiAI(ReversiBoard sourceBoard, Player playerMoves, Player opponent) {
        this.sourceBoard = sourceBoard;
        this.playerMoves = playerMoves;
        this.opponent = opponent;
        this.generation = 1;
    }

    /**
     * AI object that contains logic for determining the best move.
     *
     * @param sourceBoard The current board as seen by both players.
     * @param playerMoves The player that performs the next move.
     * @param opponent    The opponent.
     * @param generation
     */
    public ReversiAI(ReversiBoard sourceBoard, Player playerMoves, Player opponent, int generation) {
        this.sourceBoard = sourceBoard;
        this.playerMoves = playerMoves;
        this.opponent = opponent;
        this.generation = generation;
    }

    /**
     * Method that determines the best move.
     * <p>
     * [1] = Determine all legal moves including score for the first generation, and find the best move along them.
     * [2] = True if we are not at the set highest generation level.
     * [3] = Create for each legal move an thread that searches deeper for more generations.
     * [4] = If the threads exceed the timeout, we return the best move of this generation. Otherwise we find the best move for more generations.
     * [5] = Return the best move if only one generation is needed.
     *
     * @return The position for the best possible move determined by this algorithm. -1 if an error occurs.
     */
    @SuppressWarnings("ConstantConditions")
    public ReversiMove getBestMove() {
        //[1]
        determinePossibleMoves();
        determineLegalMoves();
        determineBestMove();

        //[2]
        if ((SETTINGS.GENERATION_LIMIT != 1) && (generation + 1 <= SETTINGS.GENERATION_LIMIT)) {
                //[3]
                for (ReversiMove reversiMove : legalMoves) {
                    try {
                        ReversiThreading.executeInThreadingPool(() -> reversiMove.createNextGen());
                    } catch (RejectedExecutionException ree) {
                        System.out.println(ree.toString());
                        return bestMove;
                    }
                }

                //[4]
                if (generation == 1) {
                    ReversiThreading.shutdownAndWait();
                    determineBestMove();
                    return bestMove;
                }
        }

        //[5]
        return bestMove;
    }
    /**
     * Creates the moves of the first generation.
     * @return Arraylist containing of legal moves in the first generation.
     */

    private void determinePossibleMoves() {
        Set<Integer> possibleMoveSet = new HashSet<>();

        for (int i = 0; i < 64; i++) {
            int[] temp = {i - 9, i - 7, i + 9, i + 7, i + 1, i - 1, i - 8, i + 8};

            if (sourceBoard.getPlayerAtPos(i) == null) {
                for (int aTemp : temp) {
                    if (aTemp >= 0 && aTemp <= 63) {
                        if (sourceBoard.getPlayerAtPos(aTemp) == opponent) {
                            possibleMoveSet.add(i);
                        }
                    }
                }
            }
        }

        for (Integer legalMove : possibleMoveSet) {
            possibleMoves.add(new ReversiMove(playerMoves, opponent, legalMove, sourceBoard, generation));
        }

        debugAIPrint("AI", ":cyan,n:Generation " + generation + " Possible Moves", possibleMoves.toString());
    }

    private void determineLegalMoves() {
        int currentScore = sourceBoard.getScore(playerMoves);

        for (ReversiMove possibleMove : possibleMoves) {
            if (possibleMove.getScore() > currentScore + 1) {
                legalMoves.add(possibleMove);
            }
        }

        debugAIPrint("AI", ":cyan,n:Generation " + generation + " Legal Moves", legalMoves.toString());
    }

    private void determineBestMove() {
        if (legalMoves.size() > 0) {
            ArrayList<ReversiMove> randomness = new ArrayList<>();
            randomness.add(legalMoves.get(0));

            for (ReversiMove legalMove : legalMoves) {

                if (legalMove.getScore() > randomness.get(0).getScore()) {
                    randomness.clear();
                    randomness.add(legalMove);
                } else if (legalMove.getScore() == randomness.get(0).getScore()) {
                    if (legalMove.getPriority() > randomness.get(0).getPriority()) {
                        randomness.clear();
                        randomness.add(legalMove);
                    } else if (legalMove.getPriority() == randomness.get(0).getPriority()) {
                        randomness.add(legalMove);
                    }
                }
                if (generation == 1) {
                    debugAIPrint("AI", ":cyan,n:FINAL DECISION",
                                 " Move: " + legalMove.getMove() +
                                     " with score: " + legalMove.getScore() +
                                     " and priority: " + legalMove.getPriority()
                    );
                }
            }
            if (randomness.size() > 1) {
                bestMove = randomness.get(new Random().nextInt(randomness.size()));
            } else {
                bestMove = randomness.get(0);
            }
        } else {
            bestMove = new ReversiMove(playerMoves, opponent, -1, null, generation);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void debugAIPrint(String source, String subject, String message) {
        if (SETTINGS.DEBUG_AIMODE) {
            TerminalPrinter.println(source, subject, message);
        }
    }

}
