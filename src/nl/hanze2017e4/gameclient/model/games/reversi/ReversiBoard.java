package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.AbstractBoard;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiBoard extends AbstractBoard {


    public ReversiBoard(Player player1, Player player2) {
        super(8, 8, player1, player2);
        if (player1.isPlaysFirst()) {
            setBoardBeginState(player1, player2);
        } else {
            setBoardBeginState(player2, player1);
        }
    }

    public ReversiBoard(ReversiBoard board) {super(board);}

    @Override
    public void setPlayerAtPos(Player playerWhoPlaced, int pos) {
        super.setPlayerAtPos(playerWhoPlaced, pos);
        swapTilesAfterMove(playerWhoPlaced, pos);
    }

    @Override
    public void setPlayerAtXY(Player playerWhoPlaced, int row, int column) {
        super.setPlayerAtXY(playerWhoPlaced, row, column);
        swapTilesAfterMove(playerWhoPlaced, convertXYToPos(row, column));
    }

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

    public void setBoardBeginState(Player playsFirst, Player otherPlayer) {
        super.setPlayerAtPos(playsFirst, 28);
        super.setPlayerAtPos(otherPlayer, 27);
        super.setPlayerAtPos(playsFirst, 35);
        super.setPlayerAtPos(otherPlayer, 36);
    }

    public void swapTilesAfterMove(Player playerWhoPlaced, int move) {
        ArrayList<Integer> toSwap = new ArrayList<>();
        ArrayList<Integer> toSwapAfterChecks = new ArrayList<>();

        for (Directions direction : Directions.values()) {
            for (int i = (move + direction.valueChange); (i < (getRows() * getColumns())) && (i > 0); i += direction.valueChange) {
                if (!verifyStreak(i, playerWhoPlaced, toSwap, toSwapAfterChecks)) {
                    break;
                }
                if (isPosOnEdge(i, direction.checkBoardEdges)) {
                    break;
                }
            }
            toSwap.clear();
        }
        swapTiles(toSwapAfterChecks, playerWhoPlaced);
    }

    private boolean isPosOnEdge(int pos, BoardEdges[] edgesToCheck) {
        for (BoardEdges edge : edgesToCheck) {
            for (int place : edge.values) {
                if (place == pos) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyStreak(int pos, Player playerWhoPlaced, ArrayList<Integer> currentStreak, ArrayList toSwapAfterChecks) {
        int valueAtPos = 0;

        if (getPlayerAtPos(pos) != null) {
            valueAtPos = getPlayerAtPos(pos).getUserID();
        }

        if ((valueAtPos == playerWhoPlaced.getUserID()) && (currentStreak.size() > 0)) {
            toSwapAfterChecks.addAll(currentStreak);
            currentStreak.clear();
            return false;
        } else if (valueAtPos != 0) {
            currentStreak.add(pos);
            return true;
        } else {
            return false;
        }
    }

    private void swapTiles(ArrayList<Integer> swapTilesAt, Player swapToPlayer) {
        for (int pos : swapTilesAt) {
            int[] boardLocations = convertPosToXY(pos);
            getBoard()[boardLocations[0]][boardLocations[1]] = swapToPlayer.getUserID();
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
        DIAGONALTOP(8, new BoardEdges[]{BoardEdges.TOPEDGE}),
        DIAGONALBOT(-8, new BoardEdges[]{BoardEdges.BOTTOMEDGE});

        int valueChange;
        BoardEdges[] checkBoardEdges;

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

        BoardEdges(int[] values) {
            this.values = values;
        }
    }

}
