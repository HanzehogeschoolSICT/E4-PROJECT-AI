package nl.hanze2017e4.gameclient.games.master;

public class Board {

    protected int rows;
    protected int columns;
    private int[][] board;
    private Player playerOne;
    private Player playerTwo;

    public Board(int rows, int columns, Player playerOne, Player playerTwo) {
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

    public void setPersonAtPos(Player person, int pos) {
        int[] boardLocations = convertPosToXY(pos);
        board[boardLocations[0]][boardLocations[1]] = person.getUserID();
    }

    public void setPersonAtXY(Player person, int row, int column) {
        board[row][column] = person.getUserID();
    }

    public Player getPersonAtXY(int row, int column) {
        int value = board[row][column];
        if (value == playerOne.getUserID()) {
            return playerOne;
        } else if (value == playerTwo.getUserID()) {
            return playerTwo;
        }
        return null;
    }

    public Player getPersonInPos(int pos) {
        int[] boardLocations = convertPosToXY(pos);
        int value = board[boardLocations[0]][boardLocations[1]];

        if (value == playerOne.getUserID()) {
            return playerOne;
        } else if (value == playerTwo.getUserID()) {
            return playerTwo;
        }
        return null;
    }

    private int[] convertPosToXY(int pos) {
        return new int[]{(pos / rows), (pos % columns)};
    }

    public void placeMove(Player p1, int pos) {
        setPersonAtPos(p1, pos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (getPersonAtXY(i, j) != null) {
                    sb.append(" " + getPersonAtXY(i, j).getSymbol() + " ");
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
}
