package nl.hanze2017e4.gameclient;

import nl.hanze2017e4.gameclient.controller.InteractiveModeController;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        StrategicGameClient sgc = new StrategicGameClient("localhost", 7789, 60, determineUserName());
        new InteractiveModeController(sgc).start();
    }

    /**
     * Waits for user input to enter a username.
     *
     * @return The entered username.
     */
    public static String determineUserName() {
        System.out.println("[INITIALIZATION] = Enter the desired username.");
        System.out.println("[INITIALIZATION] = Enter name and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    /**
     * Waits for user input to enter the hostname of the server.
     * @return The entered hostname.
     */
    public static String determineHost() {
        System.out.println("[INITIALIZATION] = Enter the server IP.");
        System.out.println("[INITIALIZATION] = Enter ip and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    /**
     * Waits for user input to enter the port of the server.
     * @return The entered port.
     */
    public static int determinePort() {
        System.out.println("[INITIALIZATION] = Enter the server Port.");
        System.out.println("[INITIALIZATION] = Enter port number and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    /**
     * Waits for user input to enter the time a user has to complete their turn.
     * @return The entered turn time.
     */
    public static int determineTimePerTurn() {
        System.out.println("[INITIALIZATION] = Enter the max time a turn may take.");
        System.out.println("[INITIALIZATION] = Enter the the time in seconds and press enter.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    /**
     * Waits for user input to enter the type of player that is logging in.
     * @return The entered playerType.
     */
    public static Player.PlayerType determinePlayerType() {
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

    /**
     * Waits for user input to enter which symbol they prefer.
     * @return The entered preferred symbol.
     */
    public static int determineSymbol() {
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
