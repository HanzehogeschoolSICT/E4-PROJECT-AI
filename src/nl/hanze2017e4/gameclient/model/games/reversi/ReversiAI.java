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
        ArrayList<ReversiMove> legalMoves = calculateLegalMoves();
        ReversiMove move = determineScore(legalMoves);
        return move.getMove();
    }

    private ArrayList<ReversiMove> calculateLegalMoves(Board board) {
        ArrayList<ReversiMove> legalMoves = new ArrayList<>();

        //TODO make legal moves arraylist --VINCENT

        return legalMoves;
    }

    private ReversiMove determineScore(ArrayList<ReversiMove> legalMoves, Board board, boolean ourTurn) {
        ArrayList<ReversiMove> allMoves = this.calculateLegalMoves(board);
        int bestMove;
        int bestValue;

        if(ourTurn){
            bestValue = Integer.MIN_VALUE;

            for(int i = 0; i < allMoves.size(); i++){
                Board moveBoard = new Board(this.board);
                moveBoard.setPlayerAtPos(player1, allMoves.get(i).getMove());
                int thisScore = moveBoard.getScore(player1);
            }

        } else {
            bestValue = Integer.MAX_VALUE;
        }


        //todo For each move inside legal moves look (lookForwardMoves) ahead. --LEON start
        //return the move we want to play
        return null;
    }
}
