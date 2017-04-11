package nl.hanze2017e4.gameclient.model.master;

import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;

public abstract class AbstractGame<T extends AbstractBoard> {

    private T board;
    private GameState gameState;
    private Player player1;
    private Player player2;
    private int turnTimeInSec;

    public AbstractGame(Player player1, Player player2, int turnTimeInSec, T board) {
        this.gameState = GameState.EMPTY;
        this.player1 = player1;
        this.player2 = player2;
        this.turnTimeInSec = turnTimeInSec;
        this.board = board;
        if (player1.getPlayerType() == Player.PlayerType.GUIPLAYER) {
            launchGUIMode(board);
        }
        this.gameState = GameState.INIT;
    }

    public void onMoveDetected(Player player, int move, String details) {
        TerminalPrinter.println("GAME", "Move Detected", player.getUsername() + " placed at position " + move + ".");
        if (details != null) {
            TerminalPrinter.println("GAME", "Move Detected", "Details: " + details + ".");
        }
        onMoveDetected(player, move);
    }

    public int onMyTurnDetected(Player player) {
        int move;
        this.gameState = GameState.MY_TURN;
        TerminalPrinter.println("GAME", "My Turn Detected", "Its my turn!");
        switch (player.getPlayerType()){
            case AI: {
                move = executeMyAIMove(board);
                break;
            }
            case GUIPLAYER: {
                move = executeMyGUIMove(board);
                break;
            }
            case IMPLAYER:
                move = executeMyInteractiveMove(board);
                break;
            default: {
                move = 0;
            }
        }

        this.gameState = GameState.OPPONENTS_TURN;
        return move;
    }
    public void onGameEndDetected(GameState gameEndState) {
        TerminalPrinter.println("GAME", "END", "The game ended in a " + gameEndState);
        gameState = gameEndState;
    }

    protected abstract int executeMyInteractiveMove(T board);

    protected abstract int executeMyGUIMove(T board);

    protected abstract int executeMyAIMove(T board);

    protected abstract void launchGUIMode(T board);

    protected abstract void updateGUIAfterMove(T board);
    protected abstract void updateGUIAfterMatchEnd();

    protected abstract void onMoveDetected(Player player, int move);


    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }
    public GameState getGameState() {
        return gameState;
    }

    public T getBoard() {
        return board;
    }
    public int getTurnTimeInSec() {
        return turnTimeInSec;
    }


    public enum GameState {
        EMPTY,
        INIT,
        OPPONENTS_TURN,
        MY_TURN,
        GAME_END_LOSS,
        GAME_END_WIN,
        GAME_END_DRAW;
    }
}
