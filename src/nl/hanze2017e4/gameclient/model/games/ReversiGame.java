package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

public class ReversiGame extends AbstractGame {

    public ReversiGame(Player player1, Player player2, Player playsFirst) {
        super(8, 8, player1, player2, playsFirst);
    }

    @Override
    protected int executeMyGUIMove(Board board) {
        return 0;
    }

    @Override
    protected int executeMyAIMove(Board board) {
        return 0;
    }

    @Override
    public int getScore(Player player1, Player player2, Board board) {
        return 0;
    }
}
