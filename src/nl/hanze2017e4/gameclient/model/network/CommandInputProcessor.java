package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.StrategicGameClient;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static nl.hanze2017e4.gameclient.model.network.Connector.ConnectorState.READY;

public class CommandInputProcessor extends Thread {

    private boolean threadSwitch = true;
    private Connector connector;
    private LinkedBlockingQueue<String> incomingMessagesQueue;
    private StrategicGameClient strategicGameClient;

    public CommandInputProcessor(Connector connector, LinkedBlockingQueue<String> incomingMessagesQueue, StrategicGameClient strategicGameClient) {
        this.incomingMessagesQueue = incomingMessagesQueue;
        this.connector = connector;
        this.strategicGameClient = strategicGameClient;
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
            TerminalPrinter.println("READER", "STARTUP", message);
        } else if (message.contains("Copyright")) {
            TerminalPrinter.println("READER", "STARTUP", message);
            connector.setConnectorState(READY);
        } else if (message.contains("ERR")) {
            TerminalPrinter.println("READER", ":red,n:ERROR", "Last command lead to an error.");
        } else if (message.matches("OK")) {
            TerminalPrinter.println("READER", "READY", "Last command is ok.");
        } else if (message.contains("PLAYERLIST")) {
            TerminalPrinter.println("READER", "PLAYERLIST", message);
        } else if (message.contains("GAMELIST")) {
            TerminalPrinter.println("READER", "GAMELIST", message);
        } else if (message.contains("MATCH")) {
            HashMap<ResponseType, String> response = decodeResponse(message);
            TerminalPrinter.println("READER", "MATCH", "Match found, playing against: " + response.get(ResponseType.OPPONENT) + ".");
            this.strategicGameClient.onNewGameDetected(response.get(ResponseType.GAMETYPE), response.get(ResponseType.OPPONENT), response.get(ResponseType.PLAYERMOVE));
        } else if (message.contains("YOURTURN")) {
            this.strategicGameClient.onMyTurnDetected();
        } else if (message.contains("MOVE")) {
            HashMap<ResponseType, String> response = decodeResponse(message);
            this.strategicGameClient.onNewMoveDetected(response.get(ResponseType.PLAYER), response.get(ResponseType.MOVE), response.get(ResponseType.DETAILS));
        } else if (message.contains("WIN")) {
            this.strategicGameClient.onEndGameDetected(AbstractGame.GameState.GAME_END_WIN);
        } else if (message.contains("LOSS")) {
            this.strategicGameClient.onEndGameDetected(AbstractGame.GameState.GAME_END_LOSS);
        } else if (message.contains("DRAW")) {
            this.strategicGameClient.onEndGameDetected(AbstractGame.GameState.GAME_END_DRAW);
        } else if (message.contains("CHALLENGER")) {
            TerminalPrinter.println("READER", "CHALLENGE", "Approaching challenger: " + message);
            TerminalPrinter.println("READER", "CHALLENGE", "Accept challenge by using {acc {challengeNo}}.");
        } else if (message.contains("CANCELLED")) {
            TerminalPrinter.println("READER", "CHALLENGE", "Challenge cancelled: " + message);
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

    public enum ResponseType {
        PLAYERMOVE,
        GAMETYPE,
        OPPONENT,
        PLAYER,
        MOVE,
        DETAILS;
    }

}
