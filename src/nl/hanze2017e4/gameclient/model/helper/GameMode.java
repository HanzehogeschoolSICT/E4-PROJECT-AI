package nl.hanze2017e4.gameclient.model.helper;

public enum GameMode {
    TICTACTOE("Tic-tac-toe", "X", "O", "black", "yellow"),
    REVERSI("Reversi", "B", "W", "black", "yellow");
    //Add game here, name for the server goes between ().

    public String name;
    public String symbolP1;
    public String symbolP2;
    public String colorP1;
    public String colorP2;


    GameMode(String name, String symbolP1, String symbolP2, String colorP1, String colorP2) {
        this.name = name;
        this.symbolP1 = symbolP1;
        this.symbolP2 = symbolP2;
        this.colorP1 = colorP1;
        this.colorP2 = colorP2;
    }

    public static GameMode getEnumFromString(String message) {
        GameMode returnValue = null;

        for (GameMode gameMode : GameMode.values()) {
            if (gameMode.name.equals(message)) {
                returnValue = gameMode;
                break;
            }
        }
        return returnValue;
    }

}
