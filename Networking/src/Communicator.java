import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Communicator {
    private String host;
    private int port;
    private GameMode gameMode;
    private Socket socket;
    private PrintWriter output;
    private TelnetInputReader telnetInputReader;
    private CommunicatorInputProcessor communicatorInputProcessor;
    private LinkedBlockingQueue<String> messageQueue;
    private CommunicatorMode communicatorMode = CommunicatorMode.DISCONNECTED;
    private String loggedInName = "";
    private boolean startup = false;
    private Game game;

    public Communicator(String host, int port, GameMode gameMode)  {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.host = host;
        this.port = port;
        this.gameMode = gameMode;

    }
    public void connect(){
        try {
            communicatorMode = CommunicatorMode.CONNECTING;
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            this.communicatorInputProcessor = new CommunicatorInputProcessor(this);
            this.telnetInputReader = new TelnetInputReader(new BufferedReader(new InputStreamReader(socket.getInputStream())), message -> this.communicatorInputProcessor.processMessage(message));
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
            output.println(command.name);
            System.out.println("SENDING: " + command);
        } else {
            output.println(command.name + " " + args);
            System.out.println("SENDING: " + command + " " + args);
        }
        communicatorMode = CommunicatorMode.WAITING;
        try {
             String take= messageQueue.take();
             handleCommandResponse(command, take, args);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        communicatorMode = CommunicatorMode.READY;
    }

    private void handleCommandResponse(CommandType command, String response, String args) throws Exception{
        if (response.equals("OK")) {
            println(command + " is done.");
            switch (command) {
                case GET:{
                    System.out.println("GET RESULT: " + messageQueue.take());
                    //TODO PARSE
                    break;
                }
                case HELP:{
                    for (int i = 0; i < 13; i++) {
                        System.out.println("HELP: " + messageQueue.take());
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
                System.out.println("COMMUNICATOR LOGIN: Already logged in as " + loggedInName);
            }
        } else {
            System.out.println("COMMUNICATOR: Communicator is currently not ready. Current state: " + communicatorMode);
        }
    }
    public void get(GetMode getCommandArgument) {
        sendCommand(CommandType.GET, getCommandArgument.string);
    }
    public void subscribe() {
        sendCommand(CommandType.SUBSCRIBE, this.gameMode.name);
    }
    public void challenge(String opponentName){
        sendCommand(CommandType.CHALLENGE,"\"" + opponentName + "\" \"" + this.gameMode.name + "\"");
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
        System.out.println("[Communicator] " + message);
    }
    enum CommunicatorMode{
        DISCONNECTED,
        CONNECTING,
        READY,
        ERROR,
        SENDING,
        WAITING
    }
    enum CommandType{
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
}

