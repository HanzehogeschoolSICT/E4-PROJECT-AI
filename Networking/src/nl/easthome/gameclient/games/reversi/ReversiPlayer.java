package nl.easthome.gameclient.games.reversi;

import nl.easthome.gameclient.games.master.AbstractPlayer;

public class ReversiPlayer extends AbstractPlayer {

    //Add methods, fields oa.

    String color;

    public ReversiPlayer(String username, String color) {
        super(username);
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
