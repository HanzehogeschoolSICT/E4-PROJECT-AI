package nl.hanze2017e4.gameclient.controller;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.network.Command;
import nl.hanze2017e4.gameclient.model.network.Communicator;

import java.util.Scanner;

import static java.lang.Thread.sleep;

public class InteractiveModeController implements Runnable {

    private boolean threadSwitch = true;
    private Communicator communicator;


    public InteractiveModeController(Communicator communicator) {
        this.communicator = communicator;
    }

    @Override
    public void run() {
        while (threadSwitch) {
            if (communicator.getCommunicatorState() == Communicator.CommunicatorState.READY) {
                Scanner scanner = new Scanner(System.in);
                //noinspection InfiniteLoopStatement
                while (true) {
                    switch (scanner.next()) {
                        case "login": {
                            communicator.getCommandOutput().login(scanner.next());
                            break;
                        }
                        case "acc": {
                            int whichChallenge = Integer.parseInt(scanner.next());
                            communicator.getCommandOutput().acceptChallenge(whichChallenge);
                            break;
                        }
                        case "move": {
                            int moveno = Integer.parseInt(scanner.next());
                            communicator.getCommandOutput().move(moveno);
                            break;
                        }
                        case "sub": {
                            String type = scanner.next();
                            if (type.equals("r")) {
                                communicator.getCommandOutput().subscribe(AbstractGame.GameMode.REVERSI);
                            } else if (type.equals("t")) {
                                communicator.getCommandOutput().subscribe(AbstractGame.GameMode.TICTACTOE);
                            } else {
                                System.out.println("????");
                            }
                            break;
                        }

                        case "cha": {
                            String player = scanner.next();
                            String type = scanner.next();
                            if (type.equals("r")) {
                                communicator.getCommandOutput().challenge(player, AbstractGame.GameMode.REVERSI);
                            } else if (type.equals("t")) {
                                communicator.getCommandOutput().challenge(player, AbstractGame.GameMode.TICTACTOE);
                            } else {
                                System.out.println("????");
                            }
                            break;
                        }
                        case "get": {
                            String type = scanner.next();
                            if (type.equals("g")) {
                                communicator.getCommandOutput().get(Command.Mode.GAMELIST);
                            } else if (type.equals("p")) {
                                communicator.getCommandOutput().get(Command.Mode.PLAYERLIST);
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
