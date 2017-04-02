package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

public class ReversiGame extends AbstractGame {

    public ReversiGame(Player player1, Player player2, Player playsFirst, int turnTimeInSec) {
        super(8, 8, player1, player2, playsFirst, turnTimeInSec);
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
    protected void launchGUIMode(Board board) {

    }

    @Override
    public int getBoardScore(Player player1, Player player2, Board board) {
        return 0;
    }

    @Override
    protected void updateGUIAfterMove(Board board) {

    }

    @Override
    protected void updateGUIAfterMatchEnd() {

    }
}
