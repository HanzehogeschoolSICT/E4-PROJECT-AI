package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiAI {

    Board board;
    Player player1;
    Player player2;
    int lookForwardMoves;

    /**
     * @param board
     * @param player1
     * @param player2
     * @param lookForwardMoves Number of moves to look ahead. Including opponent moves.
     */
    public ReversiAI(Board board, Player player1, Player player2, int lookForwardMoves) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.lookForwardMoves = lookForwardMoves;
    }


    public int calculateBestMove() {
        //ArrayList<ReversiMove> legalMoves = calculateLegalMoves(this.board);
        ReversiMove move = determineScore(this.board, true);
        return move.getMove();
    }

    private ArrayList<ReversiMove> calculateLegalMoves(Board board) {
        ArrayList<ReversiMove> legalMoves = new ArrayList<>();

        //TODO make legal moves arraylist --VINCENT

        return legalMoves;
    }

    private ReversiMove determineScore(Board board, boolean ourTurn) {
        ArrayList<ReversiMove> allMoves = calculateLegalMoves(board);

//        ArrayList<ReversiMove> allMoves = new ArrayList<>();
//        allMoves.add(new ReversiMove(player1,19,board));
//        allMoves.add(new ReversiMove(player1,26,board));
//        allMoves.add(new ReversiMove(player1,37,board));
//        allMoves.add(new ReversiMove(player1,44,board));

        ReversiMove bestMove = allMoves.get(0);
        int bestValue = 0;

        for(int i = 0; i < allMoves.size(); i++){
            int thisScore = allMoves.get(i).getScore();
            if(thisScore > bestValue){
                bestValue = thisScore;
                bestMove = allMoves.get(i);
            }
        }

        //return the move we want to play
        return bestMove;
    }
}
