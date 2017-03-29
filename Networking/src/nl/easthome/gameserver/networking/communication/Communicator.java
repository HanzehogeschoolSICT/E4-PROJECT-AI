package nl.easthome.gameserver.networking.communication;

import nl.easthome.gameserver.networking.master.AbstractGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Communicator {
    private String host;
    private int port;
    private Socket socket;
    private PrintWriter output;
    private CommunicatorInputReader communicatorInputReader;
    private CommunicatorInputProcessor communicatorInputProcessor;
    private LinkedBlockingQueue<String> messageQueue;
    private CommunicatorMode communicatorMode = CommunicatorMode.DISCONNECTED;
    private String loggedInName = "";
    private boolean startup = false;
    private AbstractGame runningGame;

    public Communicator(String host, int port)  {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.host = host;
        this.port = port;

    }

    public void connect(){
        try {
            communicatorMode = CommunicatorMode.CONNECTING;
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            this.communicatorInputProcessor = new CommunicatorInputProcessor(this);
            this.communicatorInputReader = new CommunicatorInputReader(
                    new BufferedReader(new InputStreamReader(socket.getInputStream())),
                    communicatorInputProcessor);
            while(!startup){}
            communicatorMode = CommunicatorMode.READY;
        } catch (IOException e) {
            communicatorMode = CommunicatorMode.ERROR;
            e.printStackTrace();
        }
    }
    private void sendCommand(CommandType command, String args){
        communicatorMode = CommunicatorMode.SENDING;

        if (args.equals("")){
            println("SEND > " + command);
            output.println(command.name);
        } else {
            println("SEND > " + command + " " + args);
            output.println(command.name + " " + args);
        }
        communicatorMode = CommunicatorMode.WAITING;
        try {
             String take = messageQueue.take();
             handleCommandResponse(command, take);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        communicatorMode = CommunicatorMode.READY;
    }
    private void handleCommandResponse(CommandType command, String response) throws Exception{
        if (response.equals("OK")) {
            switch (command) {
                case GET:{
                    println("GET > "+ messageQueue.take());
                    //TODO PARSE
                    break;
                }
                case HELP:{
                    for (int i = 0; i < 13; i++) {
                        println("HELP > " + messageQueue.take());
                    }
                    break;
                }
            }
        } else {
            throw new Exception(response);
        }
    }

    public void login(String playerName){
        if(communicatorMode == CommunicatorMode.READY ){
            if(loggedInName.isEmpty()){
                sendCommand(CommandType.LOGIN, playerName);
            } else {
                println("LOGIN > Already logged in as " + loggedInName);
            }
        } else {
            println("Not ready. Current state: " + communicatorMode);
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

    private void println(String message){
        System.out.println("[COMMUNICATOR] = " + message);
    }

    public LinkedBlockingQueue<String> getMessageQueue() {
        return messageQueue;
    }
    public void setMessageQueue(LinkedBlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }
    public CommunicatorMode getCommunicatorMode() {
        return communicatorMode;
    }
    public void setCommunicatorMode(CommunicatorMode communicatorMode) {
        this.communicatorMode = communicatorMode;
    }
    public void setStartup(boolean startup) {
        this.startup = startup;
    }
    public AbstractGame getRunningGame() {
        return runningGame;
    }
    public void setRunningGame(AbstractGame runningGame) {
        this.runningGame = runningGame;
    }

    enum CommunicatorMode{
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




}

