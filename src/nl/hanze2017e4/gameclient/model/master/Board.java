package nl.hanze2017e4.gameclient.model.master;

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

    public void clearPos(int row, int column){
        board[row][column] = 0;
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

    /*  The implementation of our minimax AI algorithm is based on several implementations found on the internet.
        One of the sources we used are:
        http://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
    */

    //returns the score (win or loss or draw) for certain board
    public int getScore(Player player, Player opponent){

        //check for win in rows and columns
        for(int i = 0; i < this.getRows(); i++){
            if(this.board[i][0] == this.board[i][1] && this.board[i][1] == this.board[i][2]){
                if(this.board[i][0] == player.getUserID()){
                    return 1;
                }

                if(this.board[i][0] == opponent.getUserID()){
                    return -1;
                }

            }
            if(this.board[0][i] == this.board[1][i] && this.board[1][i] == this.board[2][i]){
                if(this.board[0][i] == player.getUserID()){
                    return 1;
                }

                if(this.board[0][i] == opponent.getUserID()){
                    return -1;
                }
            }
        }

        //check for win in diagonal
        if(this.board[0][0] == this.board [1][1] && this.board[1][1] == this.board[2][2]){
            if(this.board [0][0] == player.getUserID()){
                return 1;
            }

            if(this.board [0][0] == opponent.getUserID()){
                return -1;
            }

        }

        //check for win in other diagonal
        if(this.board[0][2] == this.board [1][1] && this.board[1][1] == this.board[2][0]){
            if(this.board [0][2] == player.getUserID()){
                return 1;
            }

            if(this.board [0][2] == opponent.getUserID()){
                return -1;
            }
        }

        //else return draw
        return 0;
    }

    //loops through every free spot in board and calls minimax for calculating best move
    public int[] calculateBestMove(){
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < rows; j++){
                if(board[i][j] == 0){
                    setPersonAtXY(playerOne,i,j);
                    int thisScore = minimax(false);
                    clearPos(i,j);

                    if(thisScore > bestScore){
                        bestScore = thisScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    //minimax algorithm for determining best move
    public int minimax(boolean ourTurn){

        int currentScore = this.getScore(playerOne,playerTwo);

        //if won or lost, return corresponding score
        if(currentScore == 1 || currentScore == -1){
            return currentScore;
        }

        //if no win or los, but board full, return draw (0)
        if(this.isFull()){
            return 0;
        }

        if(ourTurn){
            int bestScore = Integer.MIN_VALUE;
            for(int i = 0; i < rows; i++){
                for(int j = 0; j < rows; j++){
                    if(board[i][j] == 0){
                        setPersonAtXY(playerOne,i,j);
                        int thisScore = minimax(!ourTurn);
                        if (thisScore > bestScore){
                            bestScore = thisScore;
                        }
                        this.clearPos(i,j);
                    }
                }
            }
            return bestScore;
        }else{
            int bestScore = Integer.MAX_VALUE;
            for(int i = 0; i < rows; i++){
                for(int j = 0; j < rows; j++){
                    if(board[i][j] == 0){
                        setPersonAtXY(playerTwo,i,j);
                        int thisScore = minimax(!ourTurn);
                        if (thisScore < bestScore){
                            bestScore = thisScore;
                        }
                        this.clearPos(i,j);
                    }
                }
            }
            return bestScore;
        }
    }

    public boolean isFull(){
        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.rows; j++){
                if(this.board[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] getBoard(){
        return this.board;
    }

    public int getRows(){
        return rows;
    }


}
