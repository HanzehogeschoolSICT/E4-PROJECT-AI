import nl.easthome.gameclient.games.bke.BKEPlayer;

/**
 * Created by vincent on 28-3-2017.
 */
public class Cell {

    BKEPlayer player;

    public Cell(){
        this.player = null;

    }

    public void setPlayer(BKEPlayer player) {
        this.player = player;
    }

    public BKEPlayer getPlayer() {
        return player;
    }

}
