package nl.hanze2017e4.gameclient.games.master;

import nl.hanze2017e4.gameclient.networking.communication.Communicator;

public abstract class AbstractGame {

    Board board;
    Communicator.GameState gameState;
    Player player1;
    Player player2;
    Player playsFirst;

    public AbstractGame(int rows, int columns, Player player1, Player player2, Player playsFirst) {
        this.gameState = Communicator.GameState.EMPTY;
        this.player1 = player1;
        this.player2 = player2;
        this.playsFirst = playsFirst;
        this.board = new Board(rows, columns, player1, player2);
        gameSetup();
    }

    public void gameSetup() {
        this.gameState = Communicator.GameState.INIT;
    }

    public void onMoveDetected(Player player, int move, String details) {
        println("PROCESSMOVE > " + player.getUsername() + " placed at position " + move);
        board.setPersonAtPos(player, move);
    }

    public int onMyTurnDetected(Player player) {
        this.gameState = Communicator.GameState.MY_TURN;
        int move = executeMyMove(player);
        this.gameState = Communicator.GameState.OPPONENTS_TURN;
        return move;
    }

    public void onGameEndDetected(Communicator.GameState gameEndState) {
        println("END > Game ended in a " + gameEndState);
        gameState = gameEndState;
    }

    protected abstract int executeMyMove(Player player);

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getPlaysFirst() {
        return playsFirst;
    }

    protected void println(String message) {
        System.out.println("[----GAME----] = " + message);
    }

    public Communicator.GameState getGameState() {
        return gameState;
    }

    public Board getBoard() {
        return board;
    }
}
