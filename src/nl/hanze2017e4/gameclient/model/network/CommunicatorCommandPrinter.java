package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;

import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class CommunicatorCommandPrinter implements Runnable {

    private boolean threadSwitch = true;
    private Communicator communicator;
    private PrintWriter printWriter;
    private LinkedBlockingQueue<Command> outgoingCommands;

    public CommunicatorCommandPrinter(Communicator communicator, PrintWriter printWriter) {
        this.communicator = communicator;
        this.printWriter = printWriter;
        this.outgoingCommands = new LinkedBlockingQueue<>();
    }

    public void login(String playerName) {
        try {
            outgoingCommands.put(new Command(Command.Type.LOGIN, playerName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void get(Command.Mode getCommandArgument) {
        try {
            outgoingCommands.put(new Command(Command.Type.GET, getCommandArgument.string));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(AbstractGame.GameMode gameMode) {
        try {
            outgoingCommands.put(new Command(Command.Type.SUBSCRIBE, gameMode.name));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void challenge(String opponentName, AbstractGame.GameMode gameMode) {
        try {
            outgoingCommands.put(new Command(Command.Type.CHALLENGE, "\"" + opponentName + "\" \"" + gameMode.name + "\""));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acceptChallenge(int challengeNo) {
        try {
            outgoingCommands.put(new Command(Command.Type.CHALLENGE_ACCEPT, "" + challengeNo));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void move(int placeAtPos) {
        try {
            outgoingCommands.put(new Command(Command.Type.MOVE, "" + placeAtPos));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void forfeit() {
        try {
            outgoingCommands.put(new Command(Command.Type.FORFEIT, ""));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
