package nl.easthome.gameclient.games.master;

import nl.easthome.gameclient.networking.communication.Communicator;

public abstract class AbstractGame<E extends AbstractPlayer> {

    Communicator.GameState gameState;
    E player1;
    E player2;
    E playsFirst;

    public AbstractGame(E player1, E player2, E playsFirst) {
        this.gameState = Communicator.GameState.EMPTY;
        this.player1 = player1;
        this.player2 = player2;
        this.playsFirst = playsFirst;
        gameSetup();
    }

    public void gameSetup() {
        executeOnNewGame();
        this.gameState = Communicator.GameState.INIT;
    }

    public void onMoveDetected(E player, int move, String details) {
        executeProcessMove(player, move);
    }

    ;

    public int onMyTurnDetected(E player) {
        this.gameState = Communicator.GameState.MY_TURN;
        int move = executeMyMove(player);
        this.gameState = Communicator.GameState.OPPONENTS_TURN;
        return move;
    }

    ;

    public void onGameEndDetected(Communicator.GameState gameEndState) {
        gameState = gameEndState;
        executeGameEnd();
    }

    protected abstract void executeOnNewGame();

    protected abstract void executeGameEnd();

    protected abstract int executeMyMove(E player);

    protected abstract void executeProcessMove(E player, int move);

    public E getPlayer1() {
        return player1;
    }

    public E getPlayer2() {
        return player2;
    }

    public E getPlaysFirst() {
        return playsFirst;
    }

    @Override
    public String toString() {
        return "AbstractGame{" +
                "gameState=" + gameState +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", playsFirst=" + playsFirst +
                '}';
    }

    protected void println(String message) {
        System.out.println("[----GAME----] = " + message);
    }

    public Communicator.GameState getGameState() {
        return gameState;
    }
}
