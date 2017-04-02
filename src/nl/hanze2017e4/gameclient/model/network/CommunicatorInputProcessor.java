package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.helper.GameStateChangeObserver;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class CommunicatorInputProcessor implements Runnable {

    private boolean threadSwitch = true;
    private Communicator communicator;
    private LinkedBlockingQueue<String> incomingMessagesQueue;
    private GameStateChangeObserver gameStateChangeObserver;

    public CommunicatorInputProcessor(Communicator communicator, LinkedBlockingQueue<String> incomingMessagesQueue, GameStateChangeObserver gameStateChangeObserver) {
        this.incomingMessagesQueue = incomingMessagesQueue;
        this.communicator = communicator;
        this.gameStateChangeObserver = gameStateChangeObserver;
    }

    @Override
    public void run() {
        while (threadSwitch) {
            try {
                processMessage(incomingMessagesQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String message) {
        if (message.matches("^Strategic Game Server Fixed .*$")) {
            println("STARTUP > " + message);
        } else if (message.contains("Copyright")) {
            println("STARTUP > " + message);
            communicator.setCommunicatorState(Communicator.CommunicatorState.READY);
        } else if (message.contains("ERR")) {
            println("CHECK > Last command lead to an error.");
            println("ERROR > " + message);
        } else if (message.matches("OK")) {
            println("CHECK > Last command is ok.");
        } else if (message.contains("PLAYERLIST")) {
            println("RESULT > " + message);
        } else if (message.contains("GAMELIST")) {
            println("RESULT > " + message);
        } else if (message.contains("MATCH")) {
            HashMap<ResponseType, String> response = decodeResponse(message);
            println("MATCH > Match found, playing against: " + response.get(ResponseType.OPPONENT) + ".");
            gameStateChangeObserver.onNewGameDetected(
                    response.get(ResponseType.GAMETYPE),
                    response.get(ResponseType.OPPONENT),
                    response.get(ResponseType.PLAYERMOVE)
            );
        } else if (message.contains("YOURTURN")) {
            gameStateChangeObserver.onMyTurnDetected();
        } else if (message.contains("MOVE")) {
            HashMap<ResponseType, String> response = decodeResponse(message);
            gameStateChangeObserver.onNewMoveDetected(
                    response.get(ResponseType.PLAYER),
                    response.get(ResponseType.MOVE),
                    response.get(ResponseType.DETAILS));
        } else if (message.contains("WIN")) {
            gameStateChangeObserver.onEndGameDetected(AbstractGame.GameState.GAME_END_WIN);
        } else if (message.contains("LOSS")) {
            gameStateChangeObserver.onEndGameDetected(AbstractGame.GameState.GAME_END_LOSS);
        } else if (message.contains("DRAW")) {
            gameStateChangeObserver.onEndGameDetected(AbstractGame.GameState.GAME_END_DRAW);
        } else if (message.contains("CHALLENGER")) {
            println("MATCH > Approaching challenger: " + message);
            println("MATCH > Accept challenge by using {acc {challengeNo}}.");
        } else if (message.contains("CANCELLED")) {
            println("MATCH > Challenge cancelled: " + message);
            //TODO implement automatic handling
        }
    }

    private HashMap<ResponseType, String> decodeResponse(String message) {
        //TODO embrace special signs

        HashMap<ResponseType, String> result = new HashMap<>();
        String[] split = message.replaceAll("[^A-Za-z0-9 ]", "").split(" ");

        for (int i = 0; i < split.length; i++) {
            String current = split[i];
            if (current.contains("PLAYERTOMOVE")) {
                String argument = split[i + 1];
                result.put(ResponseType.PLAYERMOVE, argument);
            } else if (current.contains("GAMETYPE")) {
                String argument = split[i + 1];
                result.put(ResponseType.GAMETYPE, argument);
            } else if (current.contains("OPPONENT")) {
                String argument = split[i + 1];
                result.put(ResponseType.OPPONENT, argument);
            } else if (current.contains("PLAYER")) {
                String argument = split[i + 1];
                result.put(ResponseType.PLAYER, argument);
            } else if (current.contains("MOVE")) {
                String argument = split[i + 1];
                result.put(ResponseType.MOVE, argument);
            } else if (current.contains("DETAILS")) {
                try {
                    String argument = split[i + 1];
                    result.put(ResponseType.DETAILS, argument);
                } catch (ArrayIndexOutOfBoundsException e) {
                    result.put(ResponseType.DETAILS, "-");
                }
            }
        }
        return result;
    }

    private void println(String message) {
        System.out.println("[COMMUNICATOR] = " + message);
    }

    public enum ResponseType {
        PLAYERMOVE,
        GAMETYPE,
        OPPONENT,
        PLAYER,
        MOVE,
        DETAILS;
    }

}
