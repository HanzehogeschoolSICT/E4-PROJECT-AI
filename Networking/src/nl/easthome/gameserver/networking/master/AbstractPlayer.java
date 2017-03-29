package nl.easthome.gameserver.networking.master;

public abstract class AbstractPlayer {

    String username;

    public AbstractPlayer(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
