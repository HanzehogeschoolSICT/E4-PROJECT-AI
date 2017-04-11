package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static nl.hanze2017e4.gameclient.model.master.Player.PlayerType.OPPONENT;

public class ReversiAI {

    Board board;
    Player player1;
    Player player2;
    int lookForwardMoves;
    private Set<Integer> legalMovesSet = new HashSet<>();
    private ArrayList<ReversiMove> legalMoves;
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
        legalMoves = new ArrayList<>();

    }

    public int calculateBestMove() {

        calculateLegalMoves(board);
        addElementToLegalMoveArray();
        //ArrayList<ReversiMove> legalMoves = calculateLegalMoves();
        ReversiMove move = determineScore(legalMoves);
        System.out.println("calculateBestMove invoked");
        return move.getMove();
    }

    private void addElementToLegalMoveArray(){
        for (Integer legalMove: legalMovesSet){
            legalMoves.add(new ReversiMove(player1,legalMove,board));
        }
    }

    private void calculateLegalMoves(Board board) {
        for (int i = 0; i < 63;i++){
            int diagonallTopLeft = i-9;
            int diagonallTopRight = i-7;
            int diagonalBotRight = i+9;
            int diagonalBotLeft = i+7;
            int horizontalRight = i+1;
            int horizontalLeft = i-1;
            int verticalUp = i-8;
            int verticalDown = i+8;

            if (board.getPlayerAtPos(i) == null){
                try{
                    // diagonal checker
                    if (board.getPlayerAtPos(diagonallTopLeft) != null || board.getPlayerAtPos(diagonalBotRight) !=null || board.getPlayerAtPos(diagonallTopRight) != null || board.getPlayerAtPos(diagonalBotLeft)!= null ) {
                         legalMovesSet.add(i);
                         }
                        //horizontal checker
                    if (board.getPlayerAtPos(horizontalRight) != null || board.getPlayerAtPos(horizontalLeft) != null) {
                        legalMovesSet.add(i);
                        }
                        //vertical checker
                    if (board.getPlayerAtPos(verticalUp) != null || board.getPlayerAtPos(verticalDown) != null) {
                        legalMovesSet.add(i);
                        }
                    } catch(IndexOutOfBoundsException ex){

                }
            }

        }
    }

    private ReversiMove determineScore(ArrayList<ReversiMove> legalMoves) {
        //todo For each move inside legal moves look (lookForwardMoves) ahead. --LEON start
        //return the move we want to play
        return null;
    }

}
