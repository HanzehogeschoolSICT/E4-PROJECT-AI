package nl.hanze2017e4.gameclient.model.network;

import nl.hanze2017e4.gameclient.model.helper.GameMode;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;

import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;

import static nl.hanze2017e4.gameclient.model.network.Connector.ConnectorState.READY;

public class CommandOutput extends Thread {

    private boolean threadSwitch = true;
    private Connector connector;
    private PrintWriter printWriter;
    private LinkedBlockingQueue<Command> outgoingCommands;

    public CommandOutput(Connector connector, PrintWriter printWriter) {
        this.connector = connector;
        this.printWriter = printWriter;
        this.outgoingCommands = new LinkedBlockingQueue<>();
    }

    public void login(String playerName) {
        try {
            outgoingCommands.put(new Command(Command.Type.LOGIN, playerName));
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.outgoingCommands = new LinkedBlockingQueue<>();
        }
    }
    public void get(Command.Mode getCommandArgument) {
        try {
            outgoingCommands.put(new Command(Command.Type.GET, getCommandArgument.string));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(GameMode gameMode) {
        try {
            outgoingCommands.put(new Command(Command.Type.SUBSCRIBE, gameMode.name));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void challenge(String opponentName, GameMode gameMode) {
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
            if (connector.getConnectorState() == READY) {
                TerminalPrinter.println("COMMANDOUTPUT", "READY", "Print buffer started.");
                while (threadSwitch) {
                    try {
                        Command c = outgoingCommands.take();
                        if (c.getArgs().equals("")) {
                            TerminalPrinter.println("COMMANDOUTPUT", "SENDING", c.getCommand().name);
                            printWriter.println(c.getCommand().name);
                        } else {
                            TerminalPrinter.println("COMMANDOUTPUT", "SENDING", c.getCommand().name + " " + c.getArgs());
                            printWriter.println(c.getCommand().name + " " + c.getArgs());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    TerminalPrinter.println("COMMANDOUTPUT", ":red,n:ERROR", "Cannot start print buffer. Trying again in 5 seconds.");
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
