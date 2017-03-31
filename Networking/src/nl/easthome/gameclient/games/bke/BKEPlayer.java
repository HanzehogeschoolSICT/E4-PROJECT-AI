package nl.easthome.gameclient.games.bke;

import nl.easthome.gameclient.games.master.AbstractPlayer;

public class BKEPlayer extends AbstractPlayer {

    //Add methods, fields oa.

    private String symbol;

    public BKEPlayer(String username, String symbol){
        super(username);

        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }

    @Override
    public String toString() {
        return "BKEPlayer{" +
                "username='" + getUsername() + '\'' +
                "symbol='" + symbol + '\'' +
                '}';
    }
}
