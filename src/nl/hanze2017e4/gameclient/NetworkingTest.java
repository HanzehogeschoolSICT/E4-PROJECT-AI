package nl.hanze2017e4.gameclient;

import nl.hanze2017e4.gameclient.networking.communication.Communicator;

import java.util.Scanner;

public class NetworkingTest {

    static Communicator c = new Communicator("localhost", 7789);
    //static Communicator c = new Communicator("145.33.225.170", 7789);

    public static void main(String[] args) {
        //Communicator c = new Communicator("145.33.225.170", 7789);
        c = new Communicator("localhost", 7789);
        c.connect();
        c.login("joristest");
        startInteractiveMode();
    }

    private static void startInteractiveMode()  {
        Scanner scanner = new Scanner(System.in);

        //noinspection InfiniteLoopStatement
        while (true) {
            switch (scanner.next()) {
                case "acc": {
                    int whichChallenge = Integer.parseInt(scanner.next());
                    c.acceptChallenge(whichChallenge);
                    break;
                    }
                case "move": {
                    int moveno = Integer.parseInt(scanner.next());
                    c.move(moveno);
                    break;
                }
                case "sub": {
                    String type = scanner.next();
                    if(type.equals("r")){
                        c.subscribe(Communicator.GameMode.REVERSI);
                    }
                    else if(type.equals("t")) {
                        c.subscribe(Communicator.GameMode.TICTACTOE);
                    }
                    else {
                        System.out.println("????");
                    }
                    break;
                }

                case "cha": {
                    String player = scanner.next();
                    String type = scanner.next();
                    if(type.equals("r")){
                        c.challenge(player, Communicator.GameMode.REVERSI);
                    }
                    else if(type.equals("t")) {
                        c.challenge(player, Communicator.GameMode.TICTACTOE);
                    }
                    else {
                        System.out.println("????");
                    }
                    break;
                }
                case "get": {
                    String type = scanner.next();
                    if(type.equals("g")){
                        c.get(Communicator.GetMode.GAMELIST);
                    }
                    else if(type.equals("p")) {
                        c.get(Communicator.GetMode.PLAYERLIST);
                    }
                    else {
                        System.out.println("????");
                    }
                    break;
                }
            }
        }
    }
}
