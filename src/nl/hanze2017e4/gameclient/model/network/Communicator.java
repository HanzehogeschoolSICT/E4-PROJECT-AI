package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.games.BKEGame;
import nl.hanze2017e4.gameclient.model.games.ReversiGame;
import nl.hanze2017e4.gameclient.model.helper.GameStateChangeObserver;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private CommunicatorInputProcessor communicatorInputProcessor;
    private CommunicatorCommandPrinter communicatorCommandPrinter;
    private ExecutorService threadPool;

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
                System.out.println("BOARD AFTER MOVE: ");
                System.out.println(game.getBoard().toString());

            }

            @Override
            public void onMyTurnDetected() {
                switch (game.getPlayer1().getPlayerType()) {
                    case AI: {
                        communicatorCommandPrinter.move(game.onMyTurnDetected(game.getPlayer1()));
                        break;
                    }
                    case GUIPLAYER: {
                        communicatorCommandPrinter.move(game.onMyTurnDetected(game.getPlayer1()));
                        break;
                    }
                    case IMPLAYER: {
                        println("MANUAL > Enter manual: {move {pos}} command.");
                        break;
                    }
                    case OPPONENT: {
                        println("ERROR > Cannot play against self!");
                        communicatorCommandPrinter.forfeit();
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
                communicatorCommandPrinter.login(userName);
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
    private Socket connectToSocket() {
        try {
            return new Socket(this.host, this.port);
        } catch (IOException e) {
            return null;
        }
    }
    private void startAllThreads() {
        threadPool = Executors.newFixedThreadPool(5);
        this.communicatorInputProcessor = createCommunicatorInputProcessor();
        this.communicatorCommandPrinter = createCommunicatorOutputPlacer();
        threadPool.execute(createCommunicatorInputReader());
        threadPool.execute(communicatorInputProcessor);
        threadPool.execute(communicatorCommandPrinter);
    }
    public CommunicatorInputReader createCommunicatorInputReader() {
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            return new CommunicatorInputReader(this, br, incomingMessages);
        } catch (IOException e) {
            println("ERROR > Input cannot be read. ");
            return null;
        }
    }
    public CommunicatorInputProcessor createCommunicatorInputProcessor() {
        return new CommunicatorInputProcessor(this, incomingMessages, gameStateChangeObserver);
    }
    public CommunicatorCommandPrinter createCommunicatorOutputPlacer() {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            return new CommunicatorCommandPrinter(this, pw);
        } catch (IOException e) {
            println("ERROR > Output cannot be send.");
            return null;
        }
    }
    private void println(String message) {
        System.out.println("[COMMUNICATOR] = " + message);
    }

    public CommunicatorState getCommunicatorState() {
        return communicatorState;
    }
    public void setCommunicatorState(CommunicatorState communicatorState) {
        this.communicatorState = communicatorState;
    }
    public ExecutorService getThreadPool() {
        return threadPool;
    }
    public CommunicatorCommandPrinter getCommunicatorCommandPrinter() {
        return communicatorCommandPrinter;
    }

    public enum CommunicatorState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        READY,
        ERROR,
        SENDING,
        WAITING
    }

}

