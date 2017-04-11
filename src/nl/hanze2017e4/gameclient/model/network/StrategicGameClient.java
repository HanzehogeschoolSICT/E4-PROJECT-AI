package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.games.bke.BkeGame;
import nl.hanze2017e4.gameclient.model.games.reversi.ReversiGame;
import nl.hanze2017e4.gameclient.model.helper.GameMode;
import nl.hanze2017e4.gameclient.model.helper.GameStateChangeObserver;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

public class StrategicGameClient implements GameStateChangeObserver {

    private Connector connector;
    private AbstractGame game;
    private int serverTurnTime;
    private String myUserName;
    private Player.PlayerType playerType;

    public StrategicGameClient(String host, int port, int serverTurnTime, String myUserName, Player.PlayerType playerType) {
        this.connector = new Connector(this, host, port);
        this.serverTurnTime = serverTurnTime;
        this.myUserName = myUserName;
        this.playerType = playerType;
    }

    @Override
    public void onNewGameDetected(GameMode gameMode, String opponentName, String playsFirst) {
        if (game == null) {
            createNewGame(gameMode, playsFirst, myUserName, opponentName);
        } else {
            TerminalPrinter.println("GAME", ":red,n:ERROR", "A game is already running.");
        }
    }

    @Override
    public void onNewMoveDetected(String playerUsername, String move, String details) {

        if (game.getPlayer1().getUsername().equals(playerUsername)) {
            game.onMoveDetected(game.getPlayer1(), Integer.parseInt(move), details);
        } else if (game.getPlayer2().getUsername().equals(playerUsername)) {
            game.onMoveDetected(game.getPlayer2(), Integer.parseInt(move), details);
        } else {
            TerminalPrinter.println("GAME", ":red,n:ERROR", "Unknown player performed move.");
        }
    }

    @Override
    public void onMyTurnDetected() {
        switch (game.getPlayer1().getPlayerType()) {
            case AI: {
                int moveResult = game.onMyTurnDetected(game.getPlayer1());
                if (moveResult < 0) {
                    TerminalPrinter.println("AI", ":cyan,n:Legal Moves", "No legal moves found, I have to forfeit.");
                    connector.getCommandOutput().forfeit();
                } else {
                    connector.getCommandOutput().move(moveResult);
                }
                break;
            }
            case GUIPLAYER: {
                connector.getCommandOutput().move(game.onMyTurnDetected(game.getPlayer1()));
                break;
            }
            case IMPLAYER: {
                TerminalPrinter.println("GAME", ":blue,n:MANUAL", "Enter manual: {move {pos}} command.");
                break;
            }
            case OPPONENT: {
                TerminalPrinter.println("GAME", ":blue,b:ERROR", "Cannot play against self!");
                connector.getCommandOutput().forfeit();
                break;
            }
        }
    }

    @Override
    public void onEndGameDetected(AbstractGame.GameState gameEnd) {
        game.onGameEndDetected(gameEnd);
        game = null;
    }

    private void createNewGame(GameMode gameMode, String playsFirstUserName, String myUserName, String opponentUserName) {

        Player player1 = new Player(myUserName, gameMode.symbolP1, gameMode.colorP1, playerType, myUserName.equals(playsFirstUserName));
        Player player2 = new Player(opponentUserName, gameMode.symbolP2, gameMode.colorP2, Player.PlayerType.OPPONENT, opponentUserName.equals(playsFirstUserName));

        switch (gameMode) {

            case TICTACTOE:
                this.game = new BkeGame(player1, player2, serverTurnTime);
                break;
            case REVERSI:
                this.game = new ReversiGame(player1, player2, serverTurnTime);
                break;
        }

        TerminalPrinter.println("GAME", "NEW", "Created " + gameMode + " instance. We are " + player1.getSymbol() + ".");

    }

    public Connector getConnector() {
        return connector;
    }

    public String getMyUserName() {
        return myUserName;
    }
}
