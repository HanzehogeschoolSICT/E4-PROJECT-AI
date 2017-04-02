package nl.hanze2017e4.gameclient.model.master;

public abstract class AbstractGame {

    private Board board;
    private GameState gameState;
    private Player player1;
    private Player player2;
    private Player playsFirst;
    private int turnTimeInSec;

    public AbstractGame(int rows, int columns, Player player1, Player player2, Player playsFirst, int turnTimeInSec) {
        this.gameState = GameState.EMPTY;
        this.player1 = player1;
        this.player2 = player2;
        this.playsFirst = playsFirst;
        this.turnTimeInSec = turnTimeInSec;
        this.board = new Board(rows, columns, player1, player2);
        gameSetup();
    }

    public void gameSetup() {
        if (player1.getPlayerType() == Player.PlayerType.GUIPLAYER) {
            launchGUIMode(board);
        }
        this.gameState = GameState.INIT;
    }
    public void onMoveDetected(Player player, int move, String details) {
        println("PROCESSMOVE > " + player.getUsername() + " placed at position " + move + ((details != null) ? "Details: " + details.toString() : "" ));
        board.setPlayerAtPos(player, move);
    }
    public int onMyTurnDetected(Player player) {
        int move;
        this.gameState = GameState.MY_TURN;
        println("MYMOVE > My turn!");
        switch (player.getPlayerType()){
            case AI: {
                move = executeMyAIMove(board);
                break;
            }
            case GUIPLAYER: {
                move = executeMyGUIMove(board);
                break;
            }
            default: {
                move = 0;
            }
        }

        this.gameState = GameState.OPPONENTS_TURN;
        return move;
    }
    public void onGameEndDetected(GameState gameEndState) {
        println("END > Game ended in a " + gameEndState);
        gameState = gameEndState;
    }
    protected void println(String message) {
        System.out.println("[----GAME----] = " + message);
    }

    protected abstract int executeMyGUIMove(Board board);
    protected abstract int executeMyAIMove(Board board);
    protected abstract void launchGUIMode(Board board);
    protected abstract void updateGUIAfterMove(Board board);
    protected abstract void updateGUIAfterMatchEnd();
    public abstract int getBoardScore(Player player1, Player player2, Board possibleBoard);

    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }
    public Player getPlaysFirst() {
        return playsFirst;
    }
    public GameState getGameState() {
        return gameState;
    }
    public Board getBoard() {
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
    public enum GameMode {

        TICTACTOE("Tic-tac-toe"),
        REVERSI("Reversi");
        //Add game here, name for the server goes between ().

        public String name;

        GameMode(String name) {
            this.name = name;
        }
    }

}
