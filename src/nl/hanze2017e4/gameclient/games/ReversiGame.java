package nl.hanze2017e4.gameclient.games;

import nl.hanze2017e4.gameclient.games.master.AbstractGame;
import nl.hanze2017e4.gameclient.games.master.Player;

public class ReversiGame extends AbstractGame {

    public ReversiGame(Player player1, Player player2, Player playsFirst) {
        super(8, 8, player1, player2, playsFirst);
    }

    @Override
    protected int executeMyMove(Player player) {
        //TODO implement
        return 0;
    }
}
