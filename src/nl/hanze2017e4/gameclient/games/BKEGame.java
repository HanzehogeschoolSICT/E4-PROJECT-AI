package nl.hanze2017e4.gameclient.games;

import nl.hanze2017e4.gameclient.games.master.AbstractGame;
import nl.hanze2017e4.gameclient.games.master.Player;

public class BKEGame extends AbstractGame {


    public BKEGame(Player p1, Player p2, Player playsFirst) {
        super(3, 3, p1, p2, playsFirst);
    }

    @Override
    protected int executeMyMove(Player player) {
        println("MYMOVE > My turn!");
        //TODO implement, return move value
        return 0;
    }
}
