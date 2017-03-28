import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Communicator {

    private String host;
    private int port;
    private CommunicatorState communicatorState;
    private Socket socket;
    private PrintWriter output;
    private CustomBufferedReader input;

    private final String DONE_CONNECTION_RECOGNITION = "(C) Copyright 2015 Hanzehogeschool Groningen";

    public Communicator(String host, int port) {
        this.host = host;
        this.port = port;
        this.communicatorState = CommunicatorState.DISCONNECTED;
    }

    public void connect(){
        try {
            communicatorState = CommunicatorState.CONNECTING;
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new CustomBufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while((line=input.readLine())!=null){
                System.out.println("CONNECT >> " + line);
                if(line.equals(DONE_CONNECTION_RECOGNITION)) {
                    communicatorState = CommunicatorState.CONNECTED;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            communicatorState = CommunicatorState.ERROR;
        }
    }

    public void disconnect(){
        try{
            if(socket != null) {
                socket.close();
                System.out.println("DISCONNECT >> Connection closed.");
                communicatorState = CommunicatorState.DISCONNECTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            communicatorState = CommunicatorState.ERROR;
        }

    }

    public void help() {
        communicatorState = CommunicatorState.SENDING;
        output.println("help");
        output.flush();
        communicatorState = CommunicatorState.RECEIVING;
        try{
            String line;
            while((line=input.readLine())!=null){
                System.out.println("CONNECT >> " + line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void login(){

    }

    public void logout(){

    }

    public void get(){

    }

}
