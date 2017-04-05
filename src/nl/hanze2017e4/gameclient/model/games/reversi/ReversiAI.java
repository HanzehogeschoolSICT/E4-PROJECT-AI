package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static nl.hanze2017e4.gameclient.model.master.Player.PlayerType.OPPONENT;

public class ReversiAI {

    Board board;
    Player player1;
    Player player2;
    int lookForwardMoves;
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
        calculateWestToEastMoves(board);
        //calculateNorthToSouthMoves(board);

        //ArrayList<ReversiMove> legalMoves = calculateLegalMoves();
        ReversiMove move = determineScore(legalMoves);
        System.out.println("calculateBestMove invoked");
        return move.getMove();
    }


    public void calculateEastToWestMoves(Board board){

    }

    public void calculateWestToEastMoves(Board board){
        this.board = board;
        for (int i = 0; i < 63;i++){
            System.out.println(board.getPlayerAtPos(i));
            int rowCounter = 8;
            //if the current position has no player, skip to next tile
            if(board.getPlayerAtPos(i) != null) {
                String check = player1.getSymbol();
                // found player on tile compare each tile with the Symbol from first tile
                for (int j = 1; j < rowCounter-2; j++) {
                    //check if the next tile not is null if it is empty then there is no need to look for a move
                    if (board.getPlayerAtPos(i+j)!= null){
                        // another symbol has been found, which means you can flip it
                        if (!board.getPlayerAtPos(i+j).getSymbol().equals(check)){

                            int validIndex = i+j+1;
                            System.out.println(validIndex);
                            legalMoves.add(new ReversiMove(player1,validIndex,board));
                        }
                    }
                    // no other valid option so skip loop
                    else{
                        break;
                    }
                }
            }
            else{
                rowCounter --;
            }
            if(rowCounter < 1){
                rowCounter = 8;
            }

        }



    }

    public void calculateDiagonalMoves() {
    }

    /**
    public void calculateWestToEastMoves(){
        for(int i = 0; i < 63; i++){
        calculateHorizontalMoves(i);
        }
    }
    **/


    public void calculateNorthToSouthMoves(Board board){
        this.board = board;
        int i = 0;

        while (i < 8){
            for (int j = 0; j < 47;j+=8){
                //only search if the tile is filled
                if (board.getPlayerAtPos(j) != null){
                    String check = "w";
                    if(!board.getPlayerAtPos(j +8).getSymbol().equals(check)){
                        if(board.getPlayerAtPos(j+16) == null){
                            int legalMove = j + 16;
                            System.out.println("NorthToSouth: " + legalMove);
                            legalMoves.add(new ReversiMove(player1,legalMove,board));
                        }
                    }
                }
            }
            i++;
        }

    }


    private ArrayList<ReversiMove> calculateLegalMoves(Board board) {
        this.board = board;

        ArrayList<ReversiMove> legalMoves = new ArrayList<>();



        //TODO make legal moves arraylist --VINCENT
        return legalMoves;
    }

    private ReversiMove determineScore(ArrayList<ReversiMove> legalMoves) {
        //todo For each move inside legal moves look (lookForwardMoves) ahead. --LEON start
        //return the move we want to play
        return null;
    }

}
