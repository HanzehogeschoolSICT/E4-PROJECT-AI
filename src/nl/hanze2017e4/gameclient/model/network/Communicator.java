package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.games.BKEGame;
import nl.hanze2017e4.gameclient.model.games.ReversiGame;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;


public class Communicator extends Thread {
    private String host;
    private int port;
    private int turnTimeInSec;
    private Socket socket;
    private CommunicatorState communicatorState = CommunicatorState.DISCONNECTED;
    private String loggedInName = "";
    private AbstractGame runningGame;
    private LinkedBlockingQueue<String> incomingMessages;
    private LinkedBlockingQueue<Command> outgoingCommands;
    private GameStateChangeObserver gameStateChangeObserver;

    public Communicator(String host, int port, int turnTimeInSec) {
        this.host = host;
        this.port = port;
        this.turnTimeInSec = turnTimeInSec;
        incomingMessages = new LinkedBlockingQueue<>();
        outgoingCommands = new LinkedBlockingQueue<>();
        gameStateChangeObserver = new GameStateChangeObserver() {
            @Override
            public void onNewGameDetected(String gameMode, String opponentName, String playsFirst) {
                if (runningGame == null) {
                    //TODO improve
                    switch (gameMode) {
                        case "Tictactoe": {
                            Player p1 = new Player(loggedInName, "X");
                            Player p2 = new Player(opponentName, "O");
                            runningGame = new BKEGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1));
                            println("GAME > Created BKEGame instance. We are " + p1.getSymbol() + ".");
                            break;
                        }
                        case "Reversi": {
                            Player p1 = new Player(loggedInName, "B");
                            Player p2 = new Player(opponentName, "W");
                            runningGame = new ReversiGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1));
                            println("GAME > Created ReversiGame instance. We are: " + p1.getSymbol() + ".");
                            break;
                        }
                    }
                } else {
                    println("ERROR > A game is already running.");
                    //TODO does this happen?
                }
            }

            @Override
            public void onNewMoveDetected(String playerUsername, String move, String details) {
                if (runningGame.getPlayer1().getUsername().equals(playerUsername)) {
                    runningGame.onMoveDetected(runningGame.getPlayer1(), Integer.parseInt(move), details);
                } else if (runningGame.getPlayer2().getUsername().equals(playerUsername)) {
                    runningGame.onMoveDetected(runningGame.getPlayer2(), Integer.parseInt(move), details);
                } else {
                    System.out.println("UNKNOWN PLAYER");
                }
                System.out.println("BOARD:");
                System.out.println(runningGame.getBoard().toString());

            }

            @Override
            public void onMyTurnDetected() {
                //TODO on live (put move() around it)
                runningGame.onMyTurnDetected(runningGame.getPlayer1());
                println("MANUAL > Enter manual move command.");
            }

            @Override
            public void onEndGameDetected(AbstractGame.GameState gameEnd) {
                runningGame.onGameEndDetected(gameEnd);
                runningGame = null;
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

    //HELPERS

    private Socket connectToSocket() {
        try {
            return new Socket(this.host, this.port);
        } catch (IOException e) {
            return null;
        }
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

    public CommunicatorOutputPlacer createCommunicatorOutputPlacer() {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            return new CommunicatorOutputPlacer(this, pw, outgoingCommands);
        } catch (IOException e) {
            println("ERROR > Output cannot be send.");
            return null;
        }
    }

    private void println(String message) {
        System.out.println("[COMMUNICATOR] = " + message);
    }

    //COMMANDS

    public void login(String playerName) {
        try {
            outgoingCommands.put(new Command(Command.CommandType.LOGIN, playerName));
            loggedInName = playerName;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void get(Command.GetMode getCommandArgument) {
        try {
            outgoingCommands.put(new Command(Command.CommandType.GET, getCommandArgument.string));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(AbstractGame.GameMode gameMode) {
        try {
            outgoingCommands.put(new Command(Command.CommandType.SUBSCRIBE, gameMode.name));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void challenge(String opponentName, AbstractGame.GameMode gameMode) {
        try {
            outgoingCommands.put(new Command(Command.CommandType.CHALLENGE, "\"" + opponentName + "\" \"" + gameMode.name + "\""));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acceptChallenge(int challengeNo) {
        try {
            outgoingCommands.put(new Command(Command.CommandType.CHALLENGE_ACCEPT, "" + challengeNo));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void move(int placeAtPos) {
        try {
            outgoingCommands.put(new Command(Command.CommandType.MOVE, "" + placeAtPos));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void forfeit() {
        try {
            outgoingCommands.put(new Command(Command.CommandType.FORFEIT, ""));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //GETTERS, SETTERS, ENUMS, INTERFACES

    public CommunicatorState getCommunicatorState() {
        return communicatorState;
    }

    public void setCommunicatorState(CommunicatorState communicatorState) {
        this.communicatorState = communicatorState;
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

    public interface GameStateChangeObserver {
        void onNewGameDetected(String gameMode, String opponentName, String playsFirst);

        void onNewMoveDetected(String playerUsername, String move, String details);

        void onMyTurnDetected();

        void onEndGameDetected(AbstractGame.GameState gameEnd);
    }

}

