package nl.hanze2017e4.gameclient.model.master;

import nl.hanze2017e4.gameclient.model.helper.GameMode;

import java.util.ArrayList;

public class AbstractBoard implements Cloneable {

    protected int rows;
    protected int columns;
    private int[][] board;
    private Player playerOne;
    private Player playerTwo;
    private ArrayList<Integer> toSwapAfterChecks;
    private GameMode gamemode;

    public AbstractBoard(int rows, int columns, Player playerOne, Player playerTwo) {
        this.rows = rows;
        this.columns = columns;
        board = new int[rows][columns];
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = 0;
            }
        }
    }

    public AbstractBoard(AbstractBoard baseBoard) {
        this.rows = baseBoard.getRows();
        this.columns = baseBoard.getColumns();
        this.playerOne = baseBoard.getPlayerOne();
        this.playerTwo = baseBoard.getPlayerTwo();
        this.board = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = baseBoard.getBoard()[i][j];
            }
        }
    }

    public void setPlayerAtPos(Player playerWhoPlaced, int pos) {
        int[] boardLocations = convertPosToXY(pos);
        board[boardLocations[0]][boardLocations[1]] = playerWhoPlaced.getUserID();
    }

    public void setPlayerAtXY(Player playerWhoPlaced, int row, int column) {
        board[row][column] = playerWhoPlaced.getUserID();
    }

    public void clearPlayerAtXY(int row, int column){
        board[row][column] = 0;
    }
    public Player getPlayerAtXY(int row, int column) {
        int value = board[row][column];
        if (value == playerOne.getUserID()) {
            return playerOne;
        } else if (value == playerTwo.getUserID()) {
            return playerTwo;
        }
        return null;
    }
    public Player getPlayerAtPos(int pos) {
        int[] boardLocations = convertPosToXY(pos);
        int value = board[boardLocations[0]][boardLocations[1]];

        if (value == playerOne.getUserID()) {
            return playerOne;
        } else if (value == playerTwo.getUserID()) {
            return playerTwo;
        }
        return null;
    }

    public int[] convertPosToXY(int pos) {
        return new int[]{(pos / rows), (pos % columns)};
    }

    public int convertXYToPos(int x, int y) { return ((x * columns) + y);}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String tabbing = "\t\t\t\t\t";
        for (int i = 0; i < rows; i++) {
            sb.append(tabbing);
            for (int j = 0; j < columns; j++) {
                Player playerAtXY = getPlayerAtXY(i, j);
                if (playerAtXY != null) {
                    sb.append(" :");
                    sb.append(playerAtXY.getColor());
                    sb.append(",n:");
                    sb.append(playerAtXY.getSymbol());
                    sb.append("[RC]");
                    sb.append(" ");
                } else {
                    sb.append(" . ");
                }
                if (j != columns - 1) {
                    sb.append("|");
                }
            }
            if (i != rows - 1) {
                sb.append("\n" + tabbing);
                for (int j = 0; j < columns; j++) {
                    if (j != columns - 1) {
                        sb.append("---|");
                    } else {
                        sb.append("---");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public int[][] getBoard(){
        return this.board;
    }
    public int getRows(){
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public Player getPlayerOne() {
        return playerOne;
    }
    public Player getPlayerTwo() {
        return playerTwo;
    }
}
