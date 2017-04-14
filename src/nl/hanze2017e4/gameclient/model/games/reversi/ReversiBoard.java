package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.AbstractBoard;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiBoard extends AbstractBoard {

    private ArrayList<Integer> toSwapAfterChecks;

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

        setPlayerAtPos(playsFirst, 28);
        setPlayerAtPos(otherPlayer, 27);
        setPlayerAtPos(playsFirst, 35);
        setPlayerAtPos(otherPlayer, 36);
    }

    public void swapTilesAfterMove(Player playerWhoPlaced, int move) {
        this.toSwapAfterChecks = new ArrayList<>();
        hasTileDiagonal(move, this, playerWhoPlaced);
        hasTileHorizontal(move, this, playerWhoPlaced);
        hasTileVertical(move, this, playerWhoPlaced);
        swapTiles(toSwapAfterChecks, playerWhoPlaced, this);
    }

    private void hasTileDiagonal(int pos, AbstractBoard board, Player playerWhoPlaced) {
        ArrayList<Integer> toSwap = new ArrayList<>();

        //diagonal top left  -9
        for (int i = (pos - 9); ((i > 0) && (i % 8 != 0)); i = i - 9) {
            System.out.println("DTL" + i);
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();
        //diagonal top right -7
        for (int i = (pos - 7); ((i > 0) && (i % 8 != 0)); i = i - 7) {
            System.out.println("DTR" + i);
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();
        //diagonal bot left +7
        for (int i = (pos + 7); (i < 64) && (i % 8 != 0); i = i + 7) {
            System.out.println("DBL" + i);
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        //diagonal bot right +9
        for (int i = (pos + 9); (i < 64) && (i % 8 != 0); i = i + 9) {
            System.out.println("DBR" + i);
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

    }

    private void hasTileHorizontal(int pos, AbstractBoard board, Player playerWhoPlaced) {
        ArrayList<Integer> toSwap = new ArrayList<>();

        // horizontal right +1 tot einde row
        for (int i = (pos + 1); (i < 64) && ((i) % 8 != 0); i++) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        // -1 tot begin row
        for (int i = (pos - 1); (i > 0) && ((i) % 8 != 0); i--) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();
    }

    private void hasTileVertical(int pos, AbstractBoard board, Player playerWhoPlaced) {
        ArrayList<Integer> toSwap = new ArrayList<>();

        // +8 tot begin col
        for (int i = (pos + 8); i < 64; i = i + 8) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        // -8 tot eind col
        for (int i = (pos - 8); i >= 0; i = i - 8) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

    }

    private boolean verifyStreak(int pos, Player playerWhoPlaced, ArrayList<Integer> toSwap, AbstractBoard board) {
        int valueAtPos = 0;

        if (board.getPlayerAtPos(pos) != null) {
            valueAtPos = board.getPlayerAtPos(pos).getUserID();
        }

        if (valueAtPos == playerWhoPlaced.getUserID()) {
            if (toSwap.size() > 0) {
                toSwapAfterChecks.addAll(toSwap);
                toSwap.clear();
                return false;
            } else {
                return false;
            }
        } else if (valueAtPos != 0) {
            toSwap.add(pos);
            return true;
        } else {
            return false;
        }
    }

    private void swapTiles(ArrayList<Integer> swapTilesAt, Player swapToPlayer, AbstractBoard board) {
        for (int pos : swapTilesAt) {
            int[] boardLocations = convertPosToXY(pos);
            board.getBoard()[boardLocations[0]][boardLocations[1]] = swapToPlayer.getUserID();
        }
        swapTilesAt.clear();
    }
}
