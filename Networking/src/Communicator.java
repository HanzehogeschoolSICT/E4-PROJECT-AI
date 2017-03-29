import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Communicator {
    private String host;
    private int port;
    private GameMode gameMode;
    private CommunicatorState communicatorState;
    private Socket socket;
    private PrintWriter output;
    private InputStream input;
    private long timeout = 10000l;

    private static final String DONE_CONNECT = "(C) Copyright 2015 Hanzehogeschool Groningen";
    private static final String DONE_HELP = "SVR HELP help         : Displays this help information";
    private static final String DONE_LOGIN = "OK";
    private static final String DONE_GET = "]";
    private static final String DONE_SUBSCRIBED = "OK";
    private static final String DONE_ERR = "ERR";
    private static final String DONE_CHALLENGE = "";

    //TODO account for getting an error or challenge while doing other commands.

    public Communicator(String host, int port, GameMode gameMode) {
        this.host = host;
        this.port = port;
        this.gameMode = gameMode;
        this.communicatorState = CommunicatorState.DISCONNECTED;
    }

    public void exit(){
        logout("EXIT");
    }
    public void quit() {
        logout("QUIT");
    }
    public void bye() { logout("BYE"); }
    public void disconnect() { logout("DISCONNECT");}
    public void connect(){
        try {
            communicatorState = CommunicatorState.CONNECTING;
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = socket.getInputStream();
            System.out.println("CONNECT >> " + readUntil(DONE_CONNECT));
            communicatorState = CommunicatorState.CONNECTED;
        } catch (IOException e) {
            e.printStackTrace();
            communicatorState = CommunicatorState.ERROR;
        }
    }
    public void help() {
        try {
            communicatorState = CommunicatorState.SENDING;
            output.println("help");
            communicatorState = CommunicatorState.RECEIVING;
            System.out.println("HELP >>" + readUntil(DONE_HELP));
            communicatorState = CommunicatorState.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
            communicatorState = CommunicatorState.ERROR;
        }
    }
    public void login(String playerName){
        try {
            communicatorState = CommunicatorState.SENDING;
            output.println("login " + playerName);
            communicatorState = CommunicatorState.RECEIVING;
            System.out.println("LOGIN >> " + readUntil(DONE_LOGIN));
            communicatorState = CommunicatorState.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void logout(String label){
        if ( label.isEmpty()) { label = "LOGOUT";}

        try {
            communicatorState = CommunicatorState.SENDING;
            output.println("logout");
            communicatorState = CommunicatorState.RECEIVING;
            if(socket != null) {
                socket.close();
                System.out.println(label + " >> Connection closed.");
                communicatorState = CommunicatorState.DISCONNECTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            communicatorState = CommunicatorState.ERROR;
        }
    }
    public ArrayList<String> get(GetCommandArgument getCommandArgument) {
        ArrayList<String> result = new ArrayList<>();

        try {
            communicatorState = CommunicatorState.SENDING;
            output.println("get " + getCommandArgument.string);
            communicatorState = CommunicatorState.RECEIVING;
            String returned = readUntil(DONE_GET);
            result = formatListToArrayList(returned);
            System.out.println("GET " + getCommandArgument.string +" >> " + returned);
            communicatorState = CommunicatorState.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(result);
        return result;
    }
    public ArrayList<String> subscribe() {
        ArrayList<String> result = new ArrayList<>();

        try {
            communicatorState = CommunicatorState.SENDING;
            output.println("subscribe " + this.gameMode.name);
            communicatorState = CommunicatorState.RECEIVING;
            String returned = readUntil(DONE_SUBSCRIBED);
            System.out.println("SUBSCRIBE " + this.gameMode.name +" >> " + returned);
            communicatorState = CommunicatorState.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public void challenge(String opponentName){
        try {
            communicatorState = CommunicatorState.SENDING;
            output.println("challenge \"" + opponentName + "\" \"" + this.gameMode.name + "\"");
            communicatorState = CommunicatorState.RECEIVING;
            String returned = readUntil(DONE_CHALLENGE);
            System.out.println("CHALLENGE " + this.gameMode.name +" >> " + returned);
            communicatorState = CommunicatorState.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
            communicatorState = CommunicatorState.ERROR;
        }
    }

    public void acceptChallenge(int challengeNo){

    }

    public void move(int move){

    }
    public void forfeit(){}

    public String readUntil(String pattern) throws IOException {
        //SOURCE = https://github.com/lbw100/JavaTelnetClient/blob/master/JavaTelnetClient.java
        long lastTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        while (true) {
            int c = -1;
            byte[] text;
            if (input.available() > 0) {
                c = input.read(text = new byte[input.available()]);
                sb.append(new String(text));
            }
            long now = System.currentTimeMillis();
            if (c != -1) {
                lastTime = now;
            }
            if (now - lastTime > timeout) {
                break;
            }
            if (sb.toString().contains(pattern)) {
                return sb.toString();
            }
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
        return sb.toString();
    }
    private ArrayList<String> formatListToArrayList(String returned) {
        ArrayList<String> output = new ArrayList<>();

        String[] a = returned.split("\\[(.*?)\\]");

        for (String s : a) {
            if (s.contains("\"")){
                System.out.println("x");
                System.out.println(s);
            }
        }


        return output;

    }
    public void waitForNextMove() {
        try {
            readUntil("xxx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
