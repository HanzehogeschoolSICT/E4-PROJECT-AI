package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.Random;

public class BKEGame extends AbstractGame {

    public BKEGame(Player p1, Player p2, Player playsFirst, int turnTimeInSec) {
        super(3, 3, p1, p2, playsFirst, turnTimeInSec);
    }

    @Override
    protected void updateGUIAfterMove() {

    }

    @Override
    protected void updateGUIAfterMatchEnd() {

    }

    @Override
    protected int executeMyGUIMove(Board board) {
        return 0;
    }

    @Override
    protected void launchGUIMode(AbstractGame game) {
        //TODO implement lanuching gui
    }

    /**
     *The implementation of our miniMax AI algorithm is based on several implementations found on the internet.
     * One of the sources we used are:
     * http://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
     *
     * @param board
     * @return
     */
    @Override
    protected int executeMyAIMove(Board board) {
        if(this.isEmpty(this.getBoard())){
            Random random = new Random();
            return random.nextInt(9);
        }

        int[] bestMove = calculateBestMove(board);
        return (bestMove[0]*3 + bestMove[1]);
    }

    /**
     * Returns the score (win or loss or draw) for certain board
     * @param player
     * @param opponent
     * @param possibleBoard
     * @return
     */
    @Override
    public int getBoardScore(Player player, Player opponent, Board possibleBoard){

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

    /**
     * Loops through every free spot in board and calls miniMax for calculating best move
     * @param board
     * @return
     */
    public int[] calculateBestMove(Board board){
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];
        Board possibleBoard = new Board(board);

        for(int i = 0; i < board.getRows(); i++){
            for(int j = 0; j < board.getColumns(); j++){
                if(possibleBoard.getPlayerAtXY(i,j) == null){
                    possibleBoard.setPlayerAtXY(board.getPlayerOne(),i,j);
                    int thisScore = miniMax(false, possibleBoard);
                    if(thisScore > bestScore){
                        bestScore = thisScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                    possibleBoard.clearPlayerAtXY(i,j);
                }
            }
        }
        return bestMove;
    }

    /**
     * MiniMax algorithm for determining best move
     * @param ourTurn
     * @param possibleBoard
     * @return
     */
    public int miniMax(boolean ourTurn, Board possibleBoard){

        int currentScore = this.getBoardScore(possibleBoard.getPlayerOne(), possibleBoard.getPlayerTwo(), possibleBoard);

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
                        possibleBoard.setPlayerAtXY(possibleBoard.getPlayerOne(),i,j);
                        int thisScore = miniMax(!ourTurn, possibleBoard);
                        if (thisScore > bestScore){
                            bestScore = thisScore;
                        }
                        possibleBoard.clearPlayerAtXY(i,j);
                    }
                }
            }
            return bestScore;
        }else{
            int bestScore = Integer.MAX_VALUE;
            for(int i = 0; i < possibleBoard.getRows(); i++){
                for(int j = 0; j < possibleBoard.getColumns(); j++){
                    if(possibleBoard.getBoard()[i][j] == 0){
                        possibleBoard.setPlayerAtXY(possibleBoard.getPlayerTwo(),i,j);
                        int thisScore = miniMax(!ourTurn, possibleBoard);
                        if (thisScore < bestScore){
                            bestScore = thisScore;
                        }
                        possibleBoard.clearPlayerAtXY(i,j);
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

    public boolean isEmpty(Board possibleBoard){
        for (int i = 0; i < possibleBoard.getRows(); i++){
            for (int j = 0; j < possibleBoard.getColumns(); j++){
                if(possibleBoard.getBoard()[i][j] != 0){
                    return false;
                }
            }
        }
        return true;
    }
}