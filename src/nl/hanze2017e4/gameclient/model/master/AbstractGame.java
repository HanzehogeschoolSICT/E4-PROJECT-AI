package nl.hanze2017e4.gameclient.model.master;

public abstract class AbstractGame {

    Board board;
    GameState gameState;
    Player player1;
    Player player2;
    Player playsFirst;

    public AbstractGame(int rows, int columns, Player player1, Player player2, Player playsFirst) {
        this.gameState = GameState.EMPTY;
        this.player1 = player1;
        this.player2 = player2;
        this.playsFirst = playsFirst;
        this.board = new Board(rows, columns, player1, player2);
        gameSetup();
    }

    public void gameSetup() {
        this.gameState = GameState.INIT;
    }

    public void onMoveDetected(Player player, int move, String details) {
        println("PROCESSMOVE > " + player.getUsername() + " placed at position " + move);
        board.setPersonAtPos(player, move);
    }

    public int onMyTurnDetected(Player player) {
        int move;
        this.gameState = GameState.MY_TURN;
        println("MYMOVE > My turn!");

        if(player.getPlayerType() == Player.PlayerType.AI){
            move = executeMyAIMove(board);
        } else {
            move = executeMyGUIMove(board);
        }

        this.gameState = GameState.OPPONENTS_TURN;
        return move;
    }

    public void onGameEndDetected(GameState gameEndState) {
        println("END > Game ended in a " + gameEndState);
        gameState = gameEndState;
    }

    protected abstract int executeMyGUIMove(Board board);

    protected abstract int executeMyAIMove(Board board);

    public abstract int getScore(Player player1, Player player2, Board possibleBoard);

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

    public GameState getGameState() {
        return gameState;
    }

    public Board getBoard() {
        return board;
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
