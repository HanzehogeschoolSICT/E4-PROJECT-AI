package nl.hanze2017e4.gameclient.controller;

import nl.hanze2017e4.gameclient.StrategicGameClient;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.network.Command;
import nl.hanze2017e4.gameclient.model.network.Connector;

import java.util.Scanner;

public class InteractiveModeController extends Thread {

    private boolean threadSwitch = true;
    private StrategicGameClient strategicGameClient;


    public InteractiveModeController(StrategicGameClient strategicGameClient) {
        this.strategicGameClient = strategicGameClient;
    }

    @Override
    public void run() {
        while (threadSwitch) {
            if (strategicGameClient.getConnector().getConnectorState() == Connector.ConnectorState.READY) {
                TerminalPrinter.println("INTERACTIVEMODE", "READY", "Communicator mode ready.");
                TerminalPrinter.println("INTERACTIVEMODE", "HELP", "[login] {name}, [acc] {challengeNo}, [move] {moveNo}, [sub] {r(eversi)/t(ic-tac-toe)}, [cha] {player} {r(eversi)/t(ic-tac-toe)}, [get] {g(amelist)/p(layerlist)}, [quit]");
                Scanner scanner = new Scanner(System.in);
                //noinspection InfiniteLoopStatement
                while (true) {
                    switch (scanner.next()) {
                        case "login": {
                            strategicGameClient.getConnector().getCommandOutput().login(scanner.next());
                            break;
                        }
                        case "acc": {
                            int whichChallenge = Integer.parseInt(scanner.next());
                            strategicGameClient.getConnector().getCommandOutput().acceptChallenge(whichChallenge);
                            break;
                        }
                        case "move": {
                            int moveno = Integer.parseInt(scanner.next());
                            strategicGameClient.getConnector().getCommandOutput().move(moveno);
                            break;
                        }
                        case "sub": {
                            String type = scanner.next();
                            if (type.equals("r")) {
                                strategicGameClient.getConnector().getCommandOutput().subscribe(AbstractGame.GameMode.REVERSI);
                            } else if (type.equals("t")) {
                                strategicGameClient.getConnector().getCommandOutput().subscribe(AbstractGame.GameMode.TICTACTOE);
                            } else {
                                System.out.println("????");
                            }
                            break;
                        }

                        case "cha": {
                            String player = scanner.next();
                            String type = scanner.next();
                            if (type.equals("r")) {
                                strategicGameClient.getConnector().getCommandOutput().challenge(player, AbstractGame.GameMode.REVERSI);
                            } else if (type.equals("t")) {
                                strategicGameClient.getConnector().getCommandOutput().challenge(player, AbstractGame.GameMode.TICTACTOE);
                            } else {
                                System.out.println("????");
                            }
                            break;
                        }
                        case "get": {
                            String type = scanner.next();
                            if (type.equals("g")) {
                                strategicGameClient.getConnector().getCommandOutput().get(Command.Mode.GAMELIST);
                            } else if (type.equals("p")) {
                                strategicGameClient.getConnector().getCommandOutput().get(Command.Mode.PLAYERLIST);
                            } else {
                                System.out.println("????");
                            }
                            break;
                        }
                        case "quit": {
                            System.exit(0);
                        }
                    }
                }
            } else {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
