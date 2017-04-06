package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.games.BKEGame;
import nl.hanze2017e4.gameclient.model.games.ReversiGame;
import nl.hanze2017e4.gameclient.model.helper.GameStateChangeObserver;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;


public class Communicator extends Thread {
    private String host;
    private int port;
    private Socket socket;
    private CommunicatorState communicatorState = CommunicatorState.DISCONNECTED;
    private AbstractGame game;
    private String userName;
    private LinkedBlockingQueue<String> incomingMessages;
    private GameStateChangeObserver gameStateChangeObserver;
    private CommandInputProcessor commandInputProcessor;
    private CommandOutput commandOutput;
    private ExecutorService threadPool;

    /**
     * Communicator object that accomodates the reading, processing and sending of commands to and from the server.
     *
     * @param host          The host address of the server.
     * @param port          The port of the server.
     * @param turnTimeInSec The amount of time you can spend on a move turn.
     * @param playerType    The type of player that is going to play.
     * @param userName      The username of the user going to play.
     * @param symbol        The symbol of the user that is going to play.
     */
    public Communicator(String host, int port, int turnTimeInSec, Player.PlayerType playerType, String userName, int symbol) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        incomingMessages = new LinkedBlockingQueue<>();
        gameStateChangeObserver = new GameStateChangeObserver() {
            @Override
            public void onNewGameDetected(String gameMode, String opponentName, String playsFirst) {
                if (game == null) {
                    switch (gameMode) {
                        case "Tictactoe": {
                            Player p1 = new Player(userName, ((symbol == 1)? "X" : "O"), playerType);
                            Player p2 = new Player(opponentName, ((symbol == 1)? "O" : "X"), Player.PlayerType.OPPONENT);
                            game = new BKEGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1), turnTimeInSec);
                            println("GAME > Created BKEGame instance. We are " + p1.getSymbol() + ".");
                            break;
                        }
                        case "Reversi": {
                            Player p1 = new Player(userName, ((symbol == 1)? "B" : "W"), playerType);
                            Player p2 = new Player(opponentName, ((symbol == 1)? "W" : "B"), Player.PlayerType.OPPONENT);
                            game = new ReversiGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1), turnTimeInSec);
                            println("GAME > Created ReversiGame instance. We are: " + p1.getSymbol() + ".");
                            break;
                        }
                    }
                } else {
                    println("ERROR > A game is already running.");
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
                        commandOutput.move(game.onMyTurnDetected(game.getPlayer1()));
                        break;
                    }
                    case GUIPLAYER: {
                        commandOutput.move(game.onMyTurnDetected(game.getPlayer1()));
                        break;
                    }
                    case IMPLAYER: {
                        println("MANUAL > Enter manual: {move {pos}} command.");
                        break;
                    }
                    case OPPONENT: {
                        println("ERROR > Cannot play against self!");
                        commandOutput.forfeit();
                        break;
                    }
                }
            }

            @Override
            public void onEndGameDetected(AbstractGame.GameState gameEnd) {
                game.onGameEndDetected(gameEnd);
                game = null;
            }
        };
    }

    /**
     * Tries to connect to the socket, if it fails retries in 5 seconds.
     * When it does connect it creates all associated threads.
     */
    @Override
    public void run() {
        do {
            this.communicatorState = CommunicatorState.CONNECTING;
            println("Connecting to server...");
            this.socket = connectToSocket();
            if (socket != null) {
                this.communicatorState = CommunicatorState.CONNECTED;
                println("Connected!");
                startAllThreads();
                commandOutput.login(userName);
                break;
            } else {
                println("Connection cannot be established.");
                println("Retrying in 5 seconds.");
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (communicatorState != CommunicatorState.CONNECTED);
    }

    /**
     * Connects to the specified socket.
     * @return The connected socket, null if no connection can be made.
     */
    private Socket connectToSocket() {
        try {
            return new Socket(this.host, this.port);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reformat the specified message with a front.
     * @param message The message that needs to be printed.
     */
    private void println(String message) {
        System.out.println("[COMMUNICATOR] = " + message);
    }

    //<editor-fold desc="Getters and Setters">
    public CommunicatorState getCommunicatorState() {
        return communicatorState;
    }

    public void setCommunicatorState(CommunicatorState communicatorState) {
        this.communicatorState = communicatorState;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public CommandOutput getCommandOutput() {
        return commandOutput;
    }

    /**
     * The different states the comunicator can be in.
     */
    public enum CommunicatorState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        READY;
    }
    //</editor-fold>

}

