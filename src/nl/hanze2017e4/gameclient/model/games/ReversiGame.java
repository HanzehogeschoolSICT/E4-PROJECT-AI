package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

public class ReversiGame extends AbstractGame {

    public ReversiGame(Player player1, Player player2, Player playsFirst) {
        super(8, 8, player1, player2, playsFirst);
    }

    @Override
    protected int executeMyMove(Player player) {
        //TODO implement move, return move value
        return 0;
    }
}
