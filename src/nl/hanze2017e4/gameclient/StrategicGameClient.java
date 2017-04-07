package nl.hanze2017e4.gameclient;

import nl.hanze2017e4.gameclient.model.games.BKEGame;
import nl.hanze2017e4.gameclient.model.games.ReversiGame;
import nl.hanze2017e4.gameclient.model.helper.GameStateChangeObserver;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;
import nl.hanze2017e4.gameclient.model.network.Connector;

public class StrategicGameClient implements GameStateChangeObserver {

    private Connector connector;
    private AbstractGame game;

    public StrategicGameClient(String host, int port) {
        this.connector = new Connector(this, host, port);
    }

    @Override
    public void onNewGameDetected(String gameMode, String opponentName, String playsFirst) {
        String userName = "Joris";
        int symbol = 1;
        Player.PlayerType playerType = Player.PlayerType.IMPLAYER;
        int turnTimeInSec = 60;


        if (game == null) {
            switch (gameMode) {
                case "Tictactoe": {
                    Player p1 = new Player(userName, ((symbol == 1) ? "X" : "O"), playerType);
                    Player p2 = new Player(opponentName, ((symbol == 1) ? "O" : "X"), Player.PlayerType.OPPONENT);
                    game = new BKEGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1), turnTimeInSec);
                    TerminalPrinter.println("GAME", "NEW", "Created BKEGame instance. We are " + p1.getSymbol() + ".");
                    break;
                }
                case "Reversi": {
                    Player p1 = new Player(userName, ((symbol == 1) ? "B" : "W"), playerType);
                    Player p2 = new Player(opponentName, ((symbol == 1) ? "W" : "B"), Player.PlayerType.OPPONENT);
                    game = new ReversiGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1), turnTimeInSec);
                    TerminalPrinter.println("GAME", "NEW", "Created ReversiGame instance. We are " + p1.getSymbol() + ".");
                    break;
                }
            }
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
            System.out.println("UNKNOWN PLAYER");
        }
        System.out.println("SCORE: " + game.getBoardScore(game.getPlayer1(), game.getPlayer2(), game.getBoard()));
        System.out.println("BOARD: ");
        System.out.println(game.getBoard().toString());

    }

    @Override
    public void onMyTurnDetected() {
        switch (game.getPlayer1().getPlayerType()) {
            case AI: {
                connector.getCommandOutput().move(game.onMyTurnDetected(game.getPlayer1()));
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

    public Connector getConnector() {
        return connector;
    }
}
