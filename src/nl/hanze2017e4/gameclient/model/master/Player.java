package nl.hanze2017e4.gameclient.model.master;

public class Player {

    private static int uniquePlayerCounter = 1;
    private String username;
    private int userID;
    private String symbol;
    private String color;
    private PlayerType playerType;
    private boolean playsFirst;

    public Player(String username, String symbol, String color, PlayerType playerType, boolean playsFirst) {
        this.playerType = playerType;
        this.symbol = symbol;
        this.color = color;
        this.username = username;
        this.userID = uniquePlayerCounter;
        this.playsFirst = playsFirst;
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

    public PlayerType getPlayerType() {
        return playerType;
    }

    public boolean isPlaysFirst() {
        return playsFirst;
    }

    public String getColor() {
        return color;
    }

    public enum PlayerType{
        OPPONENT,
        AI,
        GUIPLAYER,
        IMPLAYER;
    }
}
