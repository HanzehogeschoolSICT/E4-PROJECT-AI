package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;

import java.io.BufferedReader;
import java.util.concurrent.LinkedBlockingQueue;

import static nl.hanze2017e4.gameclient.model.network.Connector.ConnectorState.CONNECTED;

public class CommandInputReader extends Thread {

    private boolean threadSwitch = true;
    private BufferedReader inputStream;
    private LinkedBlockingQueue<String> incomingMessagesQueue;
    private Connector connector;

    public CommandInputReader(Connector connector, BufferedReader bufferedReader, LinkedBlockingQueue<String> incomingMessagesQueue) {
        this.connector = connector;
        this.inputStream = bufferedReader;
        this.incomingMessagesQueue = incomingMessagesQueue;
    }

    @Override
    public void run() {
        while (threadSwitch) {
            if (connector.getConnectorState() == CONNECTED) {
                TerminalPrinter.println("INPUTREADER", "READY", "Input reader started.");
                try {
                    String line;
                    while ((line = inputStream.readLine()) != null) {
                        incomingMessagesQueue.put(line);
                    }
                } catch (Exception e) {
                    e.toString();
                }
            } else {
                try {
                    TerminalPrinter.println("INPUTREADER", ":red,n:ERROR", "Cannot start input reader. Trying again in 5 seconds.");
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
