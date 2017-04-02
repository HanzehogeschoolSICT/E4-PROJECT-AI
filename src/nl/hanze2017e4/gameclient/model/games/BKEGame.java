package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

public class BKEGame extends AbstractGame {

    public BKEGame(Player p1, Player p2, Player playsFirst) {
        super(3, 3, p1, p2, playsFirst);
    }

    /*  The implementation of our minimax AI algorithm is based on several implementations found on the internet.
        One of the sources we used are:
        http://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
    */

    @Override
    protected int executeMyGUIMove(Board board) {
        return 0;
    }

    /**
     *The implementation of our minimax AI algorithm is based on several implementations found on the internet.
     * One of the sources we used are:
     * http://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
     *
     * @param board
     * @return
     */
    @Override
    protected int executeMyAIMove(Board board) {
        int[] bestMove = calculateBestMove(board);
        return (bestMove[0]*3 + bestMove[1]);
    }

    //loops through every free spot in board and calls minimax for calculating best move
    public int[] calculateBestMove(Board board){
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];
        Board possibleBoard = new Board(board);

        for(int i = 0; i < board.getRows(); i++){
            for(int j = 0; j < board.getColumns(); j++){
                if(possibleBoard.getPersonAtXY(i,j) == null){
                    possibleBoard.setPersonAtXY(board.getPlayerOne(),i,j);
                    int thisScore = minimax(false, possibleBoard);
                    if(thisScore > bestScore){
                        bestScore = thisScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                    possibleBoard.clearPos(i,j);
                }
            }
        }
        return bestMove;
    }


    //returns the score (win or loss or draw) for certain board
    @Override
    public int getScore(Player player, Player opponent, Board possibleBoard){

        //check for win in rows and columns
        for(int i = 0; i < possibleBoard.getRows(); i++){
            if(possibleBoard.getBoard()[i][0] == possibleBoard.getBoard()[i][1] && possibleBoard.getBoard()[i][1] == possibleBoard.getBoard()[i][2]){
                if(possibleBoard.getBoard()[i][0] == player.getUserID()){
                    return 1;
                }

                if(possibleBoard.getBoard()[i][0] == opponent.getUserID()){
                    return -1;
                }

            }
            if(possibleBoard.getBoard()[0][i] == possibleBoard.getBoard()[1][i] && possibleBoard.getBoard()[1][i] == possibleBoard.getBoard()[2][i]){
                if(possibleBoard.getBoard()[0][i] == player.getUserID()){
                    return 1;
                }

                if(possibleBoard.getBoard()[0][i] == opponent.getUserID()){
                    return -1;
                }
            }
        }

        //check for win in diagonal
        if(possibleBoard.getBoard()[0][0] == possibleBoard.getBoard()[1][1] && possibleBoard.getBoard()[1][1] == possibleBoard.getBoard()[2][2]){
            if(possibleBoard.getBoard() [0][0] == player.getUserID()){
                return 1;
            }

            if(possibleBoard.getBoard()[0][0] == opponent.getUserID()){
                return -1;
            }

        }

        //check for win in other diagonal
        if(possibleBoard.getBoard()[0][2] == possibleBoard.getBoard()[1][1] && possibleBoard.getBoard()[1][1] == possibleBoard.getBoard()[2][0]){
            if(possibleBoard.getBoard()[0][2] == player.getUserID()){
                return 1;
            }

            if(possibleBoard.getBoard()[0][2] == opponent.getUserID()){
                return -1;
            }
        }

        //else return draw
        return 0;
    }



    //minimax algorithm for determining best move
    public int minimax(boolean ourTurn, Board possibleBoard){

        int currentScore = this.getScore(possibleBoard.getPlayerOne(), possibleBoard.getPlayerTwo(), possibleBoard);

        //if won or lost, return corresponding score
        if(currentScore == 1 || currentScore == -1){
            return currentScore;
        }

        //if no win or los, but board full, return draw (0)
        if(this.isFull(possibleBoard)){
            return 0;
        }

        if(ourTurn){
            int bestScore = Integer.MIN_VALUE;
            for(int i = 0; i < possibleBoard.getRows(); i++){
                for(int j = 0; j < possibleBoard.getColumns(); j++){
                    if(possibleBoard.getBoard()[i][j] == 0){
                        possibleBoard.setPersonAtXY(possibleBoard.getPlayerOne(),i,j);
                        int thisScore = minimax(!ourTurn, possibleBoard);
                        if (thisScore > bestScore){
                            bestScore = thisScore;
                        }
                        possibleBoard.clearPos(i,j);
                    }
                }
            }
            return bestScore;
        }else{
            int bestScore = Integer.MAX_VALUE;
            for(int i = 0; i < possibleBoard.getRows(); i++){
                for(int j = 0; j < possibleBoard.getColumns(); j++){
                    if(possibleBoard.getBoard()[i][j] == 0){
                        possibleBoard.setPersonAtXY(possibleBoard.getPlayerTwo(),i,j);
                        int thisScore = minimax(!ourTurn, possibleBoard);
                        if (thisScore < bestScore){
                            bestScore = thisScore;
                        }
                        possibleBoard.clearPos(i,j);
                    }
                }
            }
            return bestScore;
        }
    }

    public boolean isFull(Board possibleBoard){
        for (int i = 0; i < possibleBoard.getRows(); i++){
            for (int j = 0; j < possibleBoard.getColumns(); j++){
                if(possibleBoard.getBoard()[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }

}