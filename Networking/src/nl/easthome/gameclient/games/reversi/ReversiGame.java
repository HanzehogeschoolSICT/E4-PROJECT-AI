package nl.easthome.gameclient.games.reversi;

import nl.easthome.gameclient.games.master.AbstractGame;
import nl.easthome.gameclient.games.master.AbstractPlayer;

public class ReversiGame extends AbstractGame {

    public ReversiGame(ReversiPlayer player1, ReversiPlayer player2, ReversiPlayer playsFirst) {
        super(player1, player2, playsFirst);
    }

    @Override
    protected void executeOnNewGame() {
        //TODO implement
    }

    @Override
    protected void executeGameEnd() {
        //TODO implement
    }

    @Override
    protected int executeMyMove(AbstractPlayer player) {
        //TODO implement
        return 0;
    }

    @Override
    protected void executeProcessMove(AbstractPlayer player, int move) {
        //TODO implement
    }
}
