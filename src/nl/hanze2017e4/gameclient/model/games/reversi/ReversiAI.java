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
        //calculateWestToEastMoves(board);
        //calculateEastToWestMoves(board);
        //calculateNorthToSouthMoves(board);
        //calculateSouthToNorthMoves(board);
        //diagonalLeftToRight(board);
        subDiagonalRightToLeftOne(board);

        //ArrayList<ReversiMove> legalMoves = calculateLegalMoves(board);
        ReversiMove move = determineScore(legalMoves);
        System.out.println("calculateBestMove invoked");
        return move.getMove();
    }

    public void diagonalLeftToRight(Board board){
        subDiagonalLeftToRightOne(board);
        subDiagonalLeftToRightTwo(board);
    }

    private void subDiagonalRightToLeftTwo(Board board){
        this.board = board;
        ArrayList<Integer> array = new ArrayList<>();
        int i = 15;
        int rowCounter = 5;
        while (i < 47) {
            array.add(rowCounter);
            for (int j = i; rowCounter > 0; rowCounter--, j += 7) {
                System.out.println("j= " + j);
                System.out.println("counter= " + rowCounter);
                if (board.getPlayerAtPos(j) != null) {

                    if (board.getPlayerAtPos(j + 7) != null) {
                        String check = player1.getSymbol();
                        if (!board.getPlayerAtPos(j + 7).getSymbol().equals(check)) {
                            System.out.println("symbol did not match at index" + (j + 7));
                            int legalMove = j + 14;
                            System.out.println("legalmove: " + legalMove);
                            legalMoves.add(new ReversiMove(player1, legalMove, board));
                        } else {

                        }
                    } else {
                        System.out.println("j: " + j);
                        j += 7;
                    }
                }

            }
            i+= 8;
            rowCounter = array.get(0) - 1;

        }
    }

    private void subDiagonalRightToLeftOne(Board board) {
        this.board = board;
        ArrayList<Integer> array = new ArrayList<>();
        int i = 7;
        int rowCounter = 6;
        while (i >= 0) {
            array.add(rowCounter);
            for (int j = i; rowCounter > 0; rowCounter--, j += 7) {
                System.out.println("j= " + j);
                System.out.println("counter= " + rowCounter);
                if (board.getPlayerAtPos(j) != null) {

                    if (board.getPlayerAtPos(j + 7) != null) {
                        String check = player1.getSymbol();
                        if (!board.getPlayerAtPos(j + 7).getSymbol().equals(check)) {
                            System.out.println("symbol did not match at index" + (j + 7));
                            int legalMove = j + 14;
                            System.out.println("legalmove: " + legalMove);
                            legalMoves.add(new ReversiMove(player1, legalMove, board));
                        } else {

                        }
                    } else {
                        System.out.println("j: " + j);
                        j += 7;
                    }
                }

            }
            i--;
            rowCounter = array.get(0) - 1;

        }
    }

    private void subDiagonalLeftToRightOne(Board board){
        this.board = board;
        ArrayList<Integer> array = new ArrayList<>();
        int rowCounter = 6;
        int i = 8;
        while(i <= 40){
            array.add(rowCounter);
            for (int j = i; rowCounter > 0; rowCounter--,j+= 9){
                if (board.getPlayerAtPos(j) != null){

                    if (board.getPlayerAtPos(j + 9) != null ){
                        String check = player1.getSymbol();
                        if (!board.getPlayerAtPos(j + 9).getSymbol().equals(check) && board.getPlayerAtPos(j + 18) != null){
                            System.out.println("symbol did not match at index" + (j+9));
                            int legalMove = j + 18;
                            System.out.println(legalMove);
                            legalMoves.add(new ReversiMove(player1,legalMove,board));
                        }
                        else{
                            j += 9;
                        }
                    }
                    else{
                        System.out.println("j: " + j);
                        j += 9;
                    }
                }
            }
            i+= 8;
            rowCounter = array.get(0) -1;

        }
    }

    private void subDiagonalLeftToRightTwo(Board board){
        this.board = board;
        ArrayList<Integer> array = new ArrayList<>();
        int i = 0;
        int rowCounter = 6;
        while(i < 8){
            array.add(rowCounter);
            for (int j = i; rowCounter > 0; rowCounter--,j+=9){
                System.out.println("j= " + j);
                System.out.println("counter= " + rowCounter);
                if (board.getPlayerAtPos(j) != null){

                    if (board.getPlayerAtPos(j + 9) != null ){
                        String check = player1.getSymbol();
                        if (!board.getPlayerAtPos(j + 9).getSymbol().equals(check)){
                            System.out.println("symbol did not match at index" + (j+9));
                            int legalMove = j + 18;
                            //System.out.println(legalMove);
                            legalMoves.add(new ReversiMove(player1,legalMove,board));
                        }
                        else{

                        }
                    }
                    else{
                        System.out.println("j: " + j);
                        j += 9;
                    }
                }

            }
            i++;
            rowCounter = array.get(0) -1;

        }





    }

    public void calculateEastToWestMoves(Board board){
        this.board = board;

        for (int i = 63; i > 0;i--){
            int rowcounter = 8;
            if (board.getPlayerAtPos(i) != null){
                String check = player1.getSymbol();

                for (int j = 1; j < rowcounter -2;j++){
                    if (board.getPlayerAtPos(i-j) != null){
                        if(!board.getPlayerAtPos(i-j).getSymbol().equals(check)){
                            int validIndex = i-j-1;
                            System.out.println(validIndex);
                            legalMoves.add(new ReversiMove(player1,validIndex,board));
                        }
                    }

                    else{
                        break;
                    }
                }
            }
            else{
                rowcounter --;
            }
            if(rowcounter < 1){
                rowcounter = 8;
            }
        }
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

    public void calculateSouthToNorthMoves(Board board){
        this.board = board;
        int i = 63;

        while(i > 55 ){

            for (int j = i; j > 0; j-=8){
                int columnCounter = 7;
                //System.out.println(board.getPlayerAtPos(j) + " " + j);
                if (board.getPlayerAtPos(j) != null){
                    for (int k = j; columnCounter > 1; k-=8,columnCounter-- ){
                        if(board.getPlayerAtPos(k - 8 )!= null){
                            String check = player1.getSymbol();
                            if (!board.getPlayerAtPos(k - 8).getSymbol().equals(check)){
                                int legalMove = k - 16;
                                System.out.println("legal move: " + legalMove);
                                legalMoves.add(new ReversiMove(player1,legalMove,board));
                            }
                        }
                    }
                }
                else{
                    columnCounter --;
                }
                if (columnCounter < 1){
                    columnCounter = 7;
                }
            }


            i--;
        }

    }

    public void calculateNorthToSouthMoves(Board board){
        this.board = board;
        int i = 0;

        while(i < 8){

            for (int j = i; j < 63; j+=8){
                int columnCounter = 7;
                //System.out.println(board.getPlayerAtPos(j) + " " + j);
                if (board.getPlayerAtPos(j) != null){
                    for (int k = j; columnCounter > 1; k+=8,columnCounter-- ){
                        if(board.getPlayerAtPos(k + 8 )!= null){
                            String check = player1.getSymbol();
                            System.out.println(k + 8);
                            if (!board.getPlayerAtPos(k + 8).getSymbol().equals(check)){
                                int legalMove = k + 16;
                                System.out.println("legal move: " + legalMove);
                                legalMoves.add(new ReversiMove(player1,legalMove,board));
                            }
                        }
                    }
                }
                else{
                    columnCounter --;
                }
                if (columnCounter < 1){
                    columnCounter = 7;
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
