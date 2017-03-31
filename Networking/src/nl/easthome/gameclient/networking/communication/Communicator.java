package nl.easthome.gameclient.networking.communication;

import nl.easthome.gameclient.games.bke.BKEGame;
import nl.easthome.gameclient.games.bke.BKEPlayer;
import nl.easthome.gameclient.games.master.AbstractGame;
import nl.easthome.gameclient.games.reversi.ReversiGame;
import nl.easthome.gameclient.games.reversi.ReversiPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Communicator {
    private String host;
    private int port;
    private Socket socket;
    private PrintWriter printWriterOutput;
    private CommunicatorInputReader communicatorInputReader;
    private CommunicatorInputProcessor communicatorInputProcessor;
    private CommunicatorMode communicatorMode = CommunicatorMode.DISCONNECTED;
    private String loggedInName = "";
    private boolean canServerAcceptCommands = false;
    private AbstractGame runningGame;

    public Communicator(String host, int port)  {
        this.host = host;
        this.port = port;

    }

    //COMMANDS

    public void connect(){
        try {
            communicatorMode = CommunicatorMode.CONNECTING;
            socket = new Socket(host, port);
            printWriterOutput = new PrintWriter(socket.getOutputStream(), true);
            this.communicatorInputProcessor = new CommunicatorInputProcessor(this);
            this.communicatorInputReader = new CommunicatorInputReader(
                    new BufferedReader(new InputStreamReader(socket.getInputStream())),
                    communicatorInputProcessor);
            while(!canServerAcceptCommands){}
            communicatorMode = CommunicatorMode.READY;
        } catch (IOException e) {
            communicatorMode = CommunicatorMode.ERROR;
            e.printStackTrace();
        }
    }
    public void login(String playerName){
        if (loggedInName.isEmpty()) {
            sendCommand(CommandType.LOGIN, playerName);
            loggedInName = playerName;
        } else {
            println("LOGIN > Already logged in as " + loggedInName);
        }
    }
    public void get(GetMode getCommandArgument) {
        sendCommand(CommandType.GET, getCommandArgument.string);
    }
    public void subscribe(GameMode gameMode) {
        sendCommand(CommandType.SUBSCRIBE, gameMode.name);
    }
    public void challenge(String opponentName, GameMode gameMode){
        sendCommand(CommandType.CHALLENGE,"\"" + opponentName + "\" \"" + gameMode.name + "\"");
    }
    public void acceptChallenge(int challengeNo){
        sendCommand(CommandType.CHALLENGE_ACCEPT, "" + challengeNo);
    }
    public void move(int placeAtPos){
        sendCommand(CommandType.MOVE, ""+placeAtPos);
    }
    public void forfeit(){
        sendCommand(CommandType.FORFEIT, "");
    }

    //HELPERS/DETECTORS

    public void newGameDetected(String gameMode, String opponentName, String playsFirst) {
        if (runningGame == null) {
            switch (gameMode) {
                case "Tictactoe": {
                    BKEPlayer p1 = new BKEPlayer(loggedInName, "X");
                    BKEPlayer p2 = new BKEPlayer(opponentName, "O");
                    runningGame = new BKEGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1));
                    println("GAME > Created BKEGame instance. We are " + p1.getSymbol());
                    break;
                }
                case "Reversi": {
                    ReversiPlayer p1 = new ReversiPlayer(loggedInName, "b");
                    ReversiPlayer p2 = new ReversiPlayer(opponentName, "w");
                    runningGame = new ReversiGame(p1, p2, (opponentName.equals(playsFirst) ? p2 : p1));
                    println("GAME > Created ReversiGame instance. We are: " + p1.getColor());
                    break;
                }
            }
        } else {
            println("ERROR > A game is already running.");
            //TODO does this happen?
        }


    }

    public void moveDetected(String playerUsername, String move, String details) {
        if (runningGame.getPlayer1().getUsername().equals(playerUsername)){
            runningGame.onMoveDetected(runningGame.getPlayer1(), Integer.parseInt(move), details);
        } else if (runningGame.getPlayer2().getUsername().equals(playerUsername)){
            runningGame.onMoveDetected(runningGame.getPlayer2(), Integer.parseInt(move), details);
        } else {
            System.out.println("UNKNOWN PLAYER");
        }


    }

    public void myTurnDetected() {
        move(runningGame.onMyTurnDetected(runningGame.getPlayer1()));
    }

    public void gameEndDetected(GameState gameEnd) {
        runningGame.onGameEndDetected(gameEnd);
        runningGame = null;
    }
    private void println(String message){
        System.out.println("[COMMUNICATOR] = " + message);
    }
    private void sendCommand(CommandType command, String args){
        if (communicatorMode == CommunicatorMode.READY) {
            communicatorMode = CommunicatorMode.SENDING;

            if (args.equals("")) {
                println("SEND > " + command);
                printWriterOutput.println(command.name);
            } else {
                println("SEND > " + command + " " + args);
                printWriterOutput.println(command.name + " " + args);
            }
            communicatorMode = CommunicatorMode.READY;
        } else {
            println("Not ready. Current state: " + communicatorMode);
        }
    }

    //GETTERS & SETTERS

    public void setCanServerAcceptCommands(boolean canServerAcceptCommands) {
        this.canServerAcceptCommands = canServerAcceptCommands;
    }

    //ENUMS

    public enum CommunicatorMode{
        DISCONNECTED,
        CONNECTING,
        READY,
        ERROR,
        SENDING,
        WAITING
    }
    public enum CommandType{
        LOGIN("login"),
        HELP("help"),
        GET("get"),
        SUBSCRIBE("subscribe"),
        CHALLENGE("challenge"),
        CHALLENGE_ACCEPT("challenge accept"),
        MOVE("move"),
        FORFEIT("forfeit"),
        UNKNOWN("unknown");

        String name;

        CommandType(String string) {
            this.name = string;
        }
    }
    public enum GameMode {

        TICTACTOE("Tic-tac-toe"),
        REVERSI("Reversi");
        //Add game here, name for the server goes between ().

        String name;

        GameMode(String name) {
            this.name = name;
        }
    }
    public enum GetMode {
        GAMELIST ("gamelist"),
        PLAYERLIST ("playerlist");

        public String string;

        GetMode(String string) {
            this.string = string;
        }
    }
    public enum ResponseType {
        PLAYERMOVE,
        GAMETYPE,
        OPPONENT,
        PLAYER,
        MOVE,
        DETAILS;
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

