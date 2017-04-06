package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiMove {

    private Player playerMoves;
    private int move;
    private Board moveBoard;
    private int score;
    private ArrayList<ReversiMove> nextMovesList;

    public ReversiMove(Player playerMoves, int move, Board board) {
        this.playerMoves = playerMoves;
        this.move = move;
        this.moveBoard = makeBoardAfterMove(move, board);
        this.nextMovesList = new ArrayList<>();
        score = calculateScore();
    }

    private int calculateScore() {
        return moveBoard.getScore(playerMoves);
    }

    private Board makeBoardAfterMove(int move, Board board) {
        Board boardAfterMove = new Board(board);
        boardAfterMove.setPlayerAtPos(playerMoves, move);
        return boardAfterMove;
    }




    public int getMove() {
        return move;
    }

    public ArrayList<ReversiMove> getNextMovesList(){
        return nextMovesList;
    }

    public void addMoveToNextMoves(ReversiMove nextMove){
        nextMovesList.add(nextMove);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Board getMoveBoard() {
        return moveBoard;
    }

    public void setMoveBoard(Board moveBoard) {
        this.moveBoard = moveBoard;
    }
}
