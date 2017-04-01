package nl.hanze2017e4.gameclient.model.master;

public class Player {

    private static int uniquePlayerCounter = 1;
    private String username;
    private int userID;
    private String symbol;
    private PlayerType playerType;

    public Player(String username, String symbol, PlayerType playerType) {
        this.playerType = playerType;
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

    public PlayerType getPlayerType() {
        return playerType;
    }

    public enum PlayerType{
        OPPONENT,
        AI,
        GUIPLAYER,
        IMPLAYER;
    }
}
