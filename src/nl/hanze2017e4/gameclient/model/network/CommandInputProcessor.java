package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.helper.GameMode;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static nl.hanze2017e4.gameclient.model.network.Connector.ConnectorState.LOGGEDIN;
import static nl.hanze2017e4.gameclient.model.network.Connector.ConnectorState.READY;

public class CommandInputProcessor extends Thread {

    private boolean threadSwitch = true;
    private Connector connector;
    private LinkedBlockingQueue<String> incomingMessagesQueue;
    private StrategicGameClient strategicGameClient;

    public CommandInputProcessor(StrategicGameClient strategicGameClient, LinkedBlockingQueue<String> incomingMessagesQueue) {
        this.incomingMessagesQueue = incomingMessagesQueue;
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
        String[] parsedMessage = message.split("[\\[\\{]");
        //System.out.println("PARSEDMESSAGE: " + parsedMessage[0]);
        switch (SVR_RESPONSE.getEnumFromString(parsedMessage[0])) {
            case OK: {
                TerminalPrinter.println("READER", "READY", "Last command was ok.");
                switch (strategicGameClient.getConnector().getConnectorState()) {
                    case READY: {
                        strategicGameClient.getConnector().setConnectorState(LOGGEDIN);
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            case ERR_TOURNAMNENT_IN_PROGRESS: {
                TerminalPrinter.println("READER", ":red,n:ERROR", "Cannot login, tournament in progress.");
                break;
            }
            case STARTUP1: {
                TerminalPrinter.println("READER", "STARTUP", message);
                break;
            }
            case STARTUP2: {
                TerminalPrinter.println("READER", "STARTUP", message);
                strategicGameClient.getConnector().setConnectorState(READY);
                break;
            }
            case GAME: {
                TerminalPrinter.println("READER", ":red,n:ERROR", "SVR GAME HAPPEND??? ");
                break;
            }
            case GAME_MOVE: {
                HashMap<ResponseType, String> response1 = decodeResponse(parsedMessage[1]);
                this.strategicGameClient.onNewMoveDetected(response1.get(ResponseType.PLAYER), response1.get(ResponseType.MOVE), response1.get(ResponseType.DETAILS));
                break;
            }
            case GAME_YOURTURN: {
                this.strategicGameClient.onMyTurnDetected();
                break;
            }
            case GAME_MATCH: {
                HashMap<ResponseType, String> response2 = decodeResponse(parsedMessage[1]);
                TerminalPrinter.println("READER", "MATCH", "Match found, playing against: " + response2.get(ResponseType.OPPONENT) + ".");
                this.strategicGameClient.onNewGameDetected(GameMode.getEnumFromString(response2.get(ResponseType.GAMETYPE)), response2.get(ResponseType.OPPONENT), response2.get(ResponseType.PLAYERMOVE));
                break;
            }
            case GAME_CHALLENGE: {
                HashMap<ResponseType, String> response3 = decodeResponse(parsedMessage[1]);
                TerminalPrinter.println("READER", "CHALLENGE", "Approaching challenger: " + response3.get(ResponseType.CHALLENGER) + " for game " + response3.get(ResponseType.GAMETYPE));
                TerminalPrinter.println("READER", "CHALLENGE", "Accept challenge by using {acc {" + response3.get(ResponseType.CHALLENGENUMBER) + "}}.");
                break;
            }
            case GAME_CHALLENGE_CANCELLED: {
                TerminalPrinter.println("READER", "CHALLENGE", "Challenge cancelled: " + message);
                break;
            }
            case GAME_WIN: {
                this.strategicGameClient.onEndGameDetected(AbstractGame.GameState.GAME_END_WIN);
                break;
            }
            case GAME_DRAW: {
                this.strategicGameClient.onEndGameDetected(AbstractGame.GameState.GAME_END_DRAW);
                break;
            }
            case GAME_LOSS: {
                this.strategicGameClient.onEndGameDetected(AbstractGame.GameState.GAME_END_LOSS);
                break;
            }
            case GAMELIST: {
                TerminalPrinter.println("READER", "GAMELIST", message);
                break;
            }
            case PLAYERLIST: {
                TerminalPrinter.println("READER", "PLAYERLIST", message);
                break;
            }
        }
    }

    private HashMap<ResponseType, String> decodeResponse(String message) {
        //TODO embrace special signs
        //TODO implement ResponseType enum

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
        PLAYERMOVE("PLAYERMOVE"),
        GAMETYPE("GAMETYPE"),
        OPPONENT("OPPONENT"),
        PLAYER("PLAYER"),
        MOVE("MOVE"),
        DETAILS("DETAILS"),
        CHALLENGER("CHALLENGER"),
        CHALLENGENUMBER("CHALLENGENUMBER");

        private final String name;


        ResponseType(String s) {
            name = s;
        }

        public static ResponseType getEnumFromString(String message) {
            ResponseType returnValue = null;

            for (ResponseType resp : ResponseType.values()) {
                if (resp.name.equals(message)) {
                    returnValue = resp;
                    break;
                }
            }
            return returnValue;
        }
    }

    public enum SVR_RESPONSE {
        OK("OK"),
        ERR_TOURNAMNENT_IN_PROGRESS("ERR Tournament in progress, login disabled"),
        STARTUP1("Strategic Game Server Fixed "),
        STARTUP2("(C) Copyright 2015 Hanzehogeschool Groningen"),
        GAME("SVR GAME "),
        GAME_MOVE("SVR GAME MOVE "),
        GAME_YOURTURN("SVR GAME YOURTURN "),
        GAME_MATCH("SVR GAME MATCH "),
        GAME_CHALLENGE("SVR GAME CHALLENGE "),
        GAME_CHALLENGE_CANCELLED("SVR GAME CHALLENGE CANCELLED"),
        GAME_WIN("SVR GAME WIN "),
        GAME_DRAW("SVR GAME DRAW "),
        GAME_LOSS("SVR GAME LOSS "),
        GAMELIST("SVR GAMELIST "),
        PLAYERLIST("SVR PLAYERLIST ");


        private final String name;

        SVR_RESPONSE(String s) {
            name = s;
        }

        public static SVR_RESPONSE getEnumFromString(String message) {
            SVR_RESPONSE returnValue = null;

            for (SVR_RESPONSE resp : SVR_RESPONSE.values()) {
                if (resp.name.equals(message)) {
                    returnValue = resp;
                    break;
                }
            }
            return returnValue;
        }

    }

}
