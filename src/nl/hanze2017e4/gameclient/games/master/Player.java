package nl.hanze2017e4.gameclient.games.master;

public class Player {

    private static int uniquePlayerCounter = 1;
    private String username;
    private int userID;
    private String symbol;

    public Player(String username, String symbol) {
        this.symbol = symbol;
        this.username = username;
        this.userID = uniquePlayerCounter;
        uniquePlayerCounter++;
    }

    public String getUsername() {
        return username;
    }

    public int getUserID() {
        return userID;
    }

    public String getSymbol() {
        return symbol;
    }
}
