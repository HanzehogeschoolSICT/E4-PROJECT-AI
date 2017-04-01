package nl.hanze2017e4.gameclient.model.network;

import java.io.BufferedReader;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class CommunicatorInputReader implements Runnable {

    private boolean threadSwitch = true;
    private BufferedReader inputStream;
    private LinkedBlockingQueue<String> incomingMessagesQueue;
    private Communicator communicator;

    public CommunicatorInputReader(Communicator communicator, BufferedReader bufferedReader, LinkedBlockingQueue<String> incomingMessagesQueue) {
        this.communicator = communicator;
        this.inputStream = bufferedReader;
        this.incomingMessagesQueue = incomingMessagesQueue;
    }

    @Override
    public void run() {
        while (threadSwitch) {
            if (communicator.getCommunicatorState() == Communicator.CommunicatorState.CONNECTED) {
                println("Input reader started.");
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
                    println("Cannot start input reader. Trying again in 5 seconds.");
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void println(String message) {
        System.out.println("[COMMUNICATOR] = READER > " + message);
    }

}
