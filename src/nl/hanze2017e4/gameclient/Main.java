package nl.hanze2017e4.gameclient;

import nl.hanze2017e4.gameclient.controller.InteractiveModeController;
import nl.hanze2017e4.gameclient.model.master.Player;
import nl.hanze2017e4.gameclient.model.network.Communicator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Communicator communicator = new Communicator(
                determineHost(),
                determinePort(),
                determineTimePerTurn(),
                determinePlayerType(),
                determineUserName(),
                determineSymbol()
        );
        communicator.start();
        try {
            communicator.join();
            communicator.getThreadPool().execute(new InteractiveModeController(communicator));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static String determineUserName() {
        System.out.println("[INITIALIZATION] = Enter the desired username.");
        System.out.println("[INITIALIZATION] = Enter name and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
    private static String determineHost() {
        System.out.println("[INITIALIZATION] = Enter the server IP.");
        System.out.println("[INITIALIZATION] = Enter ip and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
    private static int determinePort() {
        System.out.println("[INITIALIZATION] = Enter the server Port.");
        System.out.println("[INITIALIZATION] = Enter port number and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
    private static int determineTimePerTurn() {
        System.out.println("[INITIALIZATION] = Enter the max time a turn may take.");
        System.out.println("[INITIALIZATION] = Enter the the time in seconds and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
    private static Player.PlayerType determinePlayerType() {
        while(true) {
            System.out.println("[INITIALIZATION] = How would you like to play?");
            System.out.println("[INITIALIZATION] = (1: manual via terminal), (2: manual via gui), (3: let the ai play) ");
            System.out.println("[INITIALIZATION] = Enter number and press enter.");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextInt()){
                case 1: {
                    return Player.PlayerType.IMPLAYER;
                }
                case 2: {
                    return Player.PlayerType.GUIPLAYER;
                }
                case 3: {
                    return Player.PlayerType.AI;
                }
                default: {
                    System.out.println("[INITIALIZATION] ERROR > Unknown mode.");
                }
            }
        }
    }
    private static int determineSymbol() {
        while(true) {
            System.out.println("[INITIALIZATION] = Which symbol do you want?");
            System.out.println("[INITIALIZATION] = (1: X / Black ), (2: O / White)");
            System.out.println("[INITIALIZATION] = Enter number and press enter.");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextInt()){
                case 1: {
                    return 1;
                }
                case 2: {
                    return 2;
                }
                default: {
                    System.out.println("[INITIALIZATION] ERROR > Unknown answer.");
                }
            }
        }
    }

}
