package nl.hanze2017e4.gameclient.model.network;

import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class CommunicatorOutputPlacer implements Runnable {

    private boolean threadSwitch = true;
    private Communicator communicator;
    private PrintWriter printWriter;
    private LinkedBlockingQueue<Command> outgoingCommands;

    public CommunicatorOutputPlacer(Communicator communicator, PrintWriter printWriter, LinkedBlockingQueue<Command> outgoingCommands) {
        this.communicator = communicator;
        this.printWriter = printWriter;
        this.outgoingCommands = outgoingCommands;
    }

    @Override
    public void run() {
        while (threadSwitch) {
            if (communicator.getCommunicatorState() == Communicator.CommunicatorState.READY) {
                println("Print buffer started.");
                while (threadSwitch) {
                    try {
                        Command c = outgoingCommands.take();
                        if (c.getArgs().equals("")) {
                            println(c.getCommand().name);
                            printWriter.println(c.getCommand().name);
                        } else {
                            println(c.getCommand().name + " " + c.getArgs());
                            printWriter.println(c.getCommand().name + " " + c.getArgs());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    println("Cannot start print buffer. Trying again in 5 seconds.");
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void println(String message) {
        System.out.println("[COMMUNICATOR] = SENDER > " + message);
    }


}
