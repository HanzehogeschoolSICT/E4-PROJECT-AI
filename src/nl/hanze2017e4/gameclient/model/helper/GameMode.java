package nl.hanze2017e4.gameclient.model.helper;

public enum GameMode {
    TICTACTOE("Tic-tac-toe", "X", "O"),
    REVERSI("Reversi", "B", "W");
    //Add game here, name for the server goes between ().

    public String name;
    public String symbolP1;
    public String symbolP2;


    GameMode(String name, String symbolP1, String symbolP2) {
        this.name = name;
        this.symbolP1 = symbolP1;
        this.symbolP2 = symbolP2;
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
