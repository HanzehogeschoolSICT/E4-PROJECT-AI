package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.AbstractBoard;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiBoard extends AbstractBoard {


    protected ReversiBoard(Player player1, Player player2) {
        super(8, 8, player1, player2);
        if (player1.isPlaysFirst()) {
            setBoardBeginState(player1, player2);
        } else {
            setBoardBeginState(player2, player1);
        }
    }

    protected ReversiBoard(ReversiBoard board) {super(board);}

    /**
     * Places a player at a certain position 0-63.
     * Updates the board afterwards.
     *
     * @param playerWhoPlaced The player that placed the move.
     * @param pos             The position where the move has been placed.
     */
    @Override
    public void setPlayerAtPos(Player playerWhoPlaced, int pos) {
        super.setPlayerAtPos(playerWhoPlaced, pos);
        swapTilesAfterMove(playerWhoPlaced, pos);
    }

    /**
     * Places a player at a certain position 0-63.
     * Updates the board afterwards.
     * @param playerWhoPlaced The player that placed the move.
     * @param row The row the move was placed on. (0-7)
     * @param column The column the move was placed on. (0-7)
     */
    @Override
    public void setPlayerAtXY(Player playerWhoPlaced, int row, int column) {
        super.setPlayerAtXY(playerWhoPlaced, row, column);
        swapTilesAfterMove(playerWhoPlaced, convertXYToPos(row, column));
    }

    /**
     * Calculates the score of the specified player.
     * @param player Player for who the score is calculated.
     * @return The score.
     */
    public int getScore(Player player) {
        int playerScore = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (super.getBoard()[i][j] == player.getUserID()) {
                    playerScore++;
                }
            }
        }

        return playerScore;
    }

    /**
     * This method sets the state at the beginning of a Reversi game.
     * @param playsFirst The player that goes first.
     * @param otherPlayer The opponent.
     */
    public void setBoardBeginState(Player playsFirst, Player otherPlayer) {
        super.setPlayerAtPos(playsFirst, 28);
        super.setPlayerAtPos(otherPlayer, 27);
        super.setPlayerAtPos(playsFirst, 35);
        super.setPlayerAtPos(otherPlayer, 36);
    }

    /**
     * This method makes sure the board is updated after a move has been placed.
     * By looking in all 8 directions (See ReversiBoard.Directions).
     * [1] = Check all directions around the tile for streaks of tiles to swap.
     * [2] = If the current pos is already on the edge we want to check streaks at, skip this direction.
     * [3] = For each position in that streak, while inside the values of the board.
     * [4] = Check if the streak continues or the last position is on the edge of the board. If true, break the streak.
     * [5] = Swap any swappable tiles.
     *
     * @param playerWhoPlaced The player that has placed the move.
     * @param pos             At which position the player has placed the tile.
     */
    public void swapTilesAfterMove(Player playerWhoPlaced, int pos) {
        boolean resultsInCompleteStreak = false;
        ArrayList<Integer> toSwap = new ArrayList<>();
        ArrayList<Integer> toSwapAfterChecks = new ArrayList<>();

        //[1]
        for (Directions direction : Directions.values()) {
            //[2]
            if (isPosOnEdge(pos, direction.checkBoardEdges)) {
                continue;
            }
            //[3]
            for (int i = (pos + direction.valueChange); (i < (getRows() * getColumns())) && (i > 0); i += direction.valueChange) {
                //[4]
                if (doesStreakStop(i, playerWhoPlaced, toSwap, toSwapAfterChecks) || isPosOnEdge(i, direction.checkBoardEdges)) {
                    break;
                }
            }
            toSwap.clear();
        }
        //[5]
        swapTiles(toSwapAfterChecks, playerWhoPlaced);
    }

    /**
     * Checks if a position is on the edge of a board.
     * [1] = For each edge of a direction that needs to be checked.
     * [2] = For each value on the edge, check if the current position is in it.
     * @param pos The position that needs to be checked.
     * @param edgesToCheck An array with edges that needs to be checked.
     * @return True, if the value is on an edge.
     */
    private boolean isPosOnEdge(int pos, BoardEdges[] edgesToCheck) {
        //[1]
        for (BoardEdges edge : edgesToCheck) {
            //[2]
            for (int place : edge.values) {
                //[3]
                if (place == pos) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Checks if the current streak continues, or is finished.
     * [1] = Checks if there is a person at a certain position. If there is no player there return false.
     * [2a] = Check if the stone at the current position is the same as that from the person who is going to make the move.
     * [2b] = If false, the stone is from the opponent and is added to the current streak.
     * [3] = Check if there is an existing streak. If there is, it has now ended and the positions in the streak will be swapped.
     *
     * @param pos               The postition that needs to be checked.
     * @param playerWhoPlaced   The player who placed the tile.
     * @param currentStreak     An arraylist which contains the whole streak.
     * @param toSwapAfterChecks An arraylist containing everything to swap after all checks are done
     * @return True when the streak stops.
     */
    private boolean doesStreakStop(int pos, Player playerWhoPlaced, ArrayList<Integer> currentStreak, ArrayList<Integer> toSwapAfterChecks) {
        //[1]
        if (getPlayerAtPos(pos) != null) {
            //[2a]
            if (getPlayerAtPos(pos).equals(playerWhoPlaced)) {
                //[3]
                if (currentStreak.size() > 0) {
                    toSwapAfterChecks.addAll(currentStreak);
                    currentStreak.clear();
                    return true;
                }
                return true;
            } else {
                //[2b]
                currentStreak.add(pos);
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Set a other player at the positions in the arrayList.
     * @param swapTilesAt ArrayList containing all positions that need to be swapped.
     * @param swapToPlayer The new "owner" of the tile.
     */
    private void swapTiles(ArrayList<Integer> swapTilesAt, Player swapToPlayer) {
        for (int pos : swapTilesAt) {
            super.setPlayerAtPos(swapToPlayer, pos);
        }
        swapTilesAt.clear();
    }


    @SuppressWarnings("unused")
    public enum Directions {
        DIAGONAlTOPLEFTBOTRIGHT(9, new BoardEdges[]{BoardEdges.BOTTOMEDGE, BoardEdges.RIGHTEDGE}),
        DIAGONALTOPRIGHTBOTLEFT(7, new BoardEdges[]{BoardEdges.BOTTOMEDGE, BoardEdges.LEFTEDGE}),
        DIAGONALBOTLEFTTOPRIGHT(-7, new BoardEdges[]{BoardEdges.TOPEDGE, BoardEdges.RIGHTEDGE}),
        DIAGONALBOTRIGHTTOPLEFT(-9, new BoardEdges[]{BoardEdges.TOPEDGE, BoardEdges.LEFTEDGE}),
        HORIZONTALRIGHT(1, new BoardEdges[]{BoardEdges.RIGHTEDGE}),
        HORIZONTALLEFT(-1, new BoardEdges[]{BoardEdges.LEFTEDGE}),
        VERTICALTOP(-8, new BoardEdges[]{BoardEdges.TOPEDGE}),
        VERTICALBOT(8, new BoardEdges[]{BoardEdges.BOTTOMEDGE});

        int valueChange;
        BoardEdges[] checkBoardEdges;

        /**
         * Specifies all directions that can be checked from a position.
         * @param valueChange The change in position for the next location in a streak.
         * @param checkBoardEdges The edges that need to be verified.
         */
        Directions(int valueChange, BoardEdges[] checkBoardEdges) {
            this.valueChange = valueChange;
            this.checkBoardEdges = checkBoardEdges;
        }
    }

    public enum BoardEdges {
        TOPEDGE(new int[]{0, 1, 2, 3, 4, 5, 6, 7}),
        BOTTOMEDGE(new int[]{56, 57, 58, 59, 60, 61, 62, 63}),
        LEFTEDGE(new int[]{0, 8, 16, 24, 32, 40, 48, 56}),
        RIGHTEDGE(new int[]{7, 15, 23, 31, 39, 47, 55, 63});

        public int[] values;

        /**
         * Specifies all values for all edges.
         * @param values Array with positions on the edge.
         */
        BoardEdges(int[] values) {
            this.values = values;
        }
    }

}
