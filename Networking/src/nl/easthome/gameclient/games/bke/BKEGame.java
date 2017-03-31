package nl.easthome.gameclient.games.bke;

import nl.easthome.gameclient.games.master.AbstractGame;

public class BKEGame extends AbstractGame<BKEPlayer> {

    public BKEGame(BKEPlayer p1, BKEPlayer p2, BKEPlayer playsFirst) {
       super(p1, p2, playsFirst);
    }

    @Override
    protected void executeOnNewGame() {
        println("NEWGAME > " + this);
        //TODO implement
    }

    @Override
    protected void executeGameEnd() {
        println("ENDGAME > The game has resulted in a " + getGameState());
        //TODO implement
    }

    @Override
    protected int executeMyMove(BKEPlayer player) {
        println("MYMOVE > My turn!");
        //TODO implement, return move value
        return 0;
    }

    @Override
    protected void executeProcessMove(BKEPlayer player, int move) {
        println("PROCESSMOVE > " + player.toString() + " placed at position " + move);
        //TODO implement
    }

    @Override
    public String toString() {
        return "BKEGame{" + super.toString() + "}";
    }

}
