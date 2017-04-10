package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.StrategicGameClient;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import static nl.hanze2017e4.gameclient.model.network.Connector.ConnectorState.*;

public class Connector extends Thread {

    private ConnectorState connectorState = ConnectorState.DISCONNECTED;
    private StrategicGameClient strategicGameClient;
    private CommandOutput commandOutput;
    private CommandInputReader commandInputReader;
    private CommandInputProcessor commandInputProcessor;
    private LinkedBlockingQueue<String> incomingMessages;
    private String host;
    private int port;
    private Socket socket;

    public Connector(StrategicGameClient strategicGameClient, String host, int port) {
        this.incomingMessages = new LinkedBlockingQueue<>();
        this.strategicGameClient = strategicGameClient;
        this.host = host;
        this.port = port;
        this.start();
    }

    @Override
    public void run() {
        do {
            connectorState = CONNECTING;
            TerminalPrinter.println("CONNECTOR", "WAIT", "Connecting to server...");
            this.socket = connectToSocket();
            if (socket != null) {
                connectorState = CONNECTED;
                TerminalPrinter.println("CONNECTOR", "SUCCESS", "Connected!");
                commandInputProcessor = createCommunicatorInputProcessor();
                commandInputProcessor.start();
                commandInputReader = createCommunicatorInputReader();
                commandInputReader.start();
                commandOutput = createCommunicatorOutputPlacer();
                commandOutput.start();
                commandOutput.login(strategicGameClient.getMyUserName());
                break;
            } else {
                connectorState = ERROR;
                TerminalPrinter.println("CONNECTOR", ":red,n:ERROR", "Connection cannot be established.");
                TerminalPrinter.println("CONNECTOR", ":red,n:ERROR", "Retrying in 5 seconds.");
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (connectorState != CONNECTED);
    }

    /**
     * Creates the CommunicatorInputreader object associated with the socket.
     *
     * @return The created CommandInputReader.
     */
    public CommandInputReader createCommunicatorInputReader() {
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            return new CommandInputReader(this, br, incomingMessages);
        } catch (IOException e) {
            TerminalPrinter.println("CONNECTOR", ":red,n:ERROR", "ERROR > Input cannot be read. ");
            return null;
        }
    }

    /**
     * Creates the CommandInputProcessor Object associated with the socket.
     *
     * @return The created CommandInputProcessor.
     */
    public CommandInputProcessor createCommunicatorInputProcessor() {
        return new CommandInputProcessor(strategicGameClient, incomingMessages);
    }

    /**
     * Creates the CommandOutput Object associated with the socket.
     *
     * @return The created CommandOutput
     */
    public CommandOutput createCommunicatorOutputPlacer() {
        try {
            return new CommandOutput(this, new PrintWriter(socket.getOutputStream(), true));
        } catch (IOException e) {
            TerminalPrinter.println("CONNECTOR", ":red,n:ERROR", "Output cannot be send.");
            return null;
        }
    }

    /**
     * Connects to the specified socket.
     *
     * @return The connected socket, null if no connection can be made.
     */
    private Socket connectToSocket() {
        try {
            return new Socket(this.host, this.port);
        } catch (IOException e) {
            return null;
        }
    }

    public ConnectorState getConnectorState() {
        return connectorState;
    }

    public void setConnectorState(ConnectorState connectorState) {
        this.connectorState = connectorState;
    }

    public CommandOutput getCommandOutput() {
        return commandOutput;
    }

    public CommandInputReader getCommandInputReader() {
        return commandInputReader;
    }

    public CommandInputProcessor getCommandInputProcessor() {
        return commandInputProcessor;
    }

    public enum ConnectorState {
        ERROR,
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        READY, LOGGEDIN;
    }
}
