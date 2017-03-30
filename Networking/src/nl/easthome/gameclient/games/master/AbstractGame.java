package nl.easthome.gameclient.games.master;

public abstract class AbstractGame<E extends AbstractPlayer> {

    AbstractPlayer player1;
    AbstractPlayer player2;
    AbstractPlayer playsFirst;

    public AbstractGame(E player1, E player2, E playsFirst) {
        this.player1 = player1;
        this.player2 = player2;
        this.playsFirst = playsFirst;
        gameSetup();
    }

    public abstract void gameSetup();
    public abstract void gameStart();
    public abstract void processMove(E player, int move);
    public abstract int thinkMove(E player);

    public AbstractPlayer getPlayer1() {
        return player1;
    }

    public AbstractPlayer getPlayer2() {
        return player2;
    }

    public AbstractPlayer getPlaysFirst() {
        return playsFirst;
    }
}
