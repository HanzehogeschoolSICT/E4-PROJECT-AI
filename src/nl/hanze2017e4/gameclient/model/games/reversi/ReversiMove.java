package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiMove {

    private Player playerMoves;
    private int move;
    private Board moveBoard;
    private int score;
    private ArrayList<ReversiMove> nextMoves;

    public ReversiMove(Player playerMoves, int move) {
        this.playerMoves = playerMoves;
        this.move = move;
        this.moveBoard = makeBoardAfterMove(move);
        this.nextMoves = new ArrayList<>();
    }

    private Board makeBoardAfterMove(int move) {
        //TODO make board after move --JORIS
        return null;
    }

    public int getMove() {
        return move;
    }
}
