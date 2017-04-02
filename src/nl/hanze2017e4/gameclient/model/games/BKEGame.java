package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

public class BKEGame extends AbstractGame {

    public BKEGame(Player p1, Player p2, Player playsFirst) {
        super(3, 3, p1, p2, playsFirst);
    }

    @Override
    protected int executeMyGUIMove(Board board) {
        return 0;
    }

    @Override
    protected int executeMyAIMove(Board board) {
        int[] bestMove = board.calculateBestMove();
        return (bestMove[0]*3 + bestMove[1]);
    }

}