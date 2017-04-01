package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

public class BKEGame extends AbstractGame {


    public BKEGame(Player p1, Player p2, Player playsFirst) {
        super(3, 3, p1, p2, playsFirst);
    }

    @Override
    protected int executeMyMove(Player player) {
        //TODO implement, return move value
        return 0;
    }
}
