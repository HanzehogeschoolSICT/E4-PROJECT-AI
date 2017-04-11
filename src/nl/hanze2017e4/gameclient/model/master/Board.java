package nl.hanze2017e4.gameclient.model.master;

import nl.hanze2017e4.gameclient.model.helper.GameMode;

import java.util.ArrayList;

public class Board implements Cloneable {

    protected int rows;
    protected int columns;
    private int[][] board;
    private Player playerOne;
    private Player playerTwo;
    private ArrayList<Integer> toSwapAfterChecks;
    private GameMode gamemode;

    public Board(int rows, int columns, Player playerOne, Player playerTwo, GameMode gameMode) {
        this.rows = rows;
        this.columns = columns;
        board = new int[rows][columns];
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.gamemode = gameMode;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = 0;
            }
        }
    }
    public Board(Board baseBoard) {
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
        //TODO ugly..
        if (rows == 8) {
            flipTilesAfterMove(playerWhoPlaced, pos);
        }
    }
    public void setPlayerAtXY(Player person, int row, int column) {
        board[row][column] = person.getUserID();
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

    public void flipTilesAfterMove(Player playerWhoPlaced, int move) {
        this.toSwapAfterChecks = new ArrayList<>();
        hasTileDiagonal(move, this, playerWhoPlaced);
        hasTileHorizontal(move, this, playerWhoPlaced);
        hasTileVertical(move, this, playerWhoPlaced);
        swapTiles(toSwapAfterChecks, playerWhoPlaced, this);
    }

    private void hasTileDiagonal(int pos, Board board, Player playerWhoPlaced) {
        System.out.println("B4Dia \n" + board.toString());
        ArrayList<Integer> toSwap = new ArrayList<>();

        //diagonal top left  -9
        for (int i = (pos - 9); ((i > 0) && ((i + 1) % 8 != 0)); i = i - 9) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();
        //diagonal top right -7
        for (int i = (pos - 7); ((i > 0) && (i % 8 != 0)); i = i - 7) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();
        //diagonal bot left +7
        for (int i = (pos + 7); (i < 64) && ((i + 1) % 8 != 0); i = i + 7) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        //diagonal bot right +9
        for (int i = (pos + 9); (i < 64) && (i % 8 != 0); i = i + 9) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();
        System.out.println("AFTDIA \n" + board.toString());

    }

    private void hasTileHorizontal(int pos, Board board, Player playerWhoPlaced) {
        System.out.println("B4HOR \n" + board.toString());
        ArrayList<Integer> toSwap = new ArrayList<>();

        // horizontal right +1 tot einde row
        for (int i = (pos + 1); ((i) % 8 != 0); i++) {
            System.out.println("HOR " + i);
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        // -1 tot begin row
        for (int i = (pos - 1); ((i + 1) % 8 != 0); i--) {
            System.out.println("HOR " + i);
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        System.out.println("AFTHOR\n" + board.toString());

    }

    private void hasTileVertical(int pos, Board board, Player playerWhoPlaced) {
        System.out.println("B4VER \n" + board.toString());
        ArrayList<Integer> toSwap = new ArrayList<>();

        // +8 tot begin col
        for (int i = (pos + 8); i < 63; i = i + 8) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        // -8 tot eind col
        for (int i = (pos - 8); i > 0; i = i - 8) {
            if (!verifyStreak(i, playerWhoPlaced, toSwap, board)) {
                break;
            }
        }
        toSwap.clear();

        System.out.println("AFTVER \n" + board.toString());
    }


    private boolean verifyStreak(int pos, Player playerWhoPlaced, ArrayList<Integer> toSwap, Board board) {
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

    private void swapTiles(ArrayList<Integer> swapTilesAt, Player swapToPlayer, Board board) {

        for (int pos : swapTilesAt) {
            System.out.println("SWAPPED: " + pos + " TO " + swapToPlayer.getSymbol());
            int[] boardLocations = convertPosToXY(pos);
            board.getBoard()[boardLocations[0]][boardLocations[1]] = swapToPlayer.getUserID();
        }
        swapTilesAt.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (getPlayerAtXY(i, j) != null) {
                    String symbol = getPlayerAtXY(i, j).getSymbol();
                    sb.append(" :");
                    sb.append((symbol.equals(gamemode.symbolP1) ? "black" : "white"));
                    sb.append(",n:");
                    sb.append(symbol);
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
                sb.append("\n");
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

    public int getScore(Player player){
        int playerScore = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(board[i][j] == player.getUserID()){
                    playerScore ++;
                }
            }
        }

        return playerScore;
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
