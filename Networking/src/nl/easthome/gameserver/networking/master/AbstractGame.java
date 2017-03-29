package nl.easthome.gameserver.networking.master;

import nl.easthome.gameserver.networking.communication.Communicator;

public abstract class AbstractGame {

    Communicator.GameMode gameMode;
    AbstractPlayer player1;
    AbstractPlayer player2;

    public AbstractGame(Communicator.GameMode gameMode, AbstractPlayer player1, AbstractPlayer player2) {
        this.gameMode =gameMode;
        this.player1 = player1;
        this.player2 = player2;
    }

    public abstract void gameSetup();
    public abstract void gameStart();
    public abstract void processMove(AbstractPlayer player, int move);
    public abstract int thinkMove(AbstractPlayer player);
}
