package nl.hanze2017e4.gameclient.networking.communication;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class CommunicatorInputProcessor extends Thread{

    private boolean threadSwitch = true;
    private Communicator communicator;
    private LinkedBlockingQueue<String> queue;

    public CommunicatorInputProcessor(Communicator communicator) {
        this.communicator = communicator;
        this.queue = new LinkedBlockingQueue();
        this.start();
    }

    @Override
    public void run() {
        while (threadSwitch) {
            try {
                processMessage(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void submitMessage(String message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message) {
            if (message.matches("^Strategic Game Server Fixed .*$")) {
                println("STARTUP > " + message);
            }
            else if (message.contains("Copyright")) {
                println("STARTUP > " + message);
                communicator.setCanServerAcceptCommands(true);
            } else if (message.contains("ERR")) {
                println("CHECK > Last command lead to an error.");
                println("ERROR > " + message);
            } else if (message.matches("OK")) {
                println("CHECK > Last command is ok.");
            } else if (message.contains("PLAYERLIST")) {
                println("RESULT > " + message);
            } else if (message.contains("GAMELIST")) {
                println("RESULT > " + message);
            } else if (message.contains("MATCH")){
                HashMap<Communicator.ResponseType, String> response = decodeResponse(message);
                println("MATCH > Match found, playing against: " + response.get(Communicator.ResponseType.OPPONENT) + ".");
                communicator.newGameDetected(
                        response.get(Communicator.ResponseType.GAMETYPE),
                        response.get(Communicator.ResponseType.OPPONENT),
                        response.get(Communicator.ResponseType.PLAYERMOVE)
                );
            } else if (message.contains("YOURTURN")){
                communicator.myTurnDetected();
            } else if (message.contains("MOVE")){
                HashMap<Communicator.ResponseType, String> response = decodeResponse(message);
                communicator.moveDetected(
                        response.get(Communicator.ResponseType.PLAYER),
                        response.get(Communicator.ResponseType.MOVE),
                        response.get(Communicator.ResponseType.DETAILS)
                );
            } else if (message.contains("WIN")) {
                communicator.gameEndDetected(Communicator.GameState.GAME_END_WIN);
            } else if (message.contains("LOSS")){
                communicator.gameEndDetected(Communicator.GameState.GAME_END_LOSS);
            } else if (message.contains("DRAW")){
                communicator.gameEndDetected(Communicator.GameState.GAME_END_DRAW);
            } else if (message.contains("CHALLENGER")){
                println("MATCH > Approaching challenger: " + message);
                //TODO implement automatic handling
            } else if (message.contains("CANCELLED")){
                println("MATCH > Challenge cancelled: " + message);
                //TODO implement automatic handling
            }
    }

    private HashMap<Communicator.ResponseType, String> decodeResponse(String message){
        HashMap<Communicator.ResponseType, String> result = new HashMap<>();
        String[] split = message.replaceAll("[^A-Za-z0-9 ]", "").split(" ");

        for (int i = 0; i < split.length; i++) {
            String current = split[i];
            if(current.contains("PLAYERTOMOVE")){
                String argument = split[i+1];
                result.put(Communicator.ResponseType.PLAYERMOVE, argument);
            }
            else if (current.contains("GAMETYPE")) {
                String argument = split[i+1];
                result.put(Communicator.ResponseType.GAMETYPE, argument);
            }
            else if (current.contains("OPPONENT")) {
                String argument = split[i+1];
                result.put(Communicator.ResponseType.OPPONENT, argument);
            }
            else if (current.contains("PLAYER")) {
                String argument = split[i+1];
                result.put(Communicator.ResponseType.PLAYER, argument);
            }
            else if (current.contains("MOVE")) {
                String argument = split[i+1];
                result.put(Communicator.ResponseType.MOVE, argument);
            }
            else if (current.contains("DETAILS")) {
                try{
                    String argument = split[i+1];
                    result.put(Communicator.ResponseType.DETAILS, argument);
                }catch (ArrayIndexOutOfBoundsException e) {
                    result.put(Communicator.ResponseType.DETAILS, "-");
                }
            }
        }
        return result;
    }

    private void println(String message){
        System.out.println("[COMMUNICATOR] = " + message);
    }

}
