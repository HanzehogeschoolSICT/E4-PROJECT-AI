import nl.easthome.gameclient.games.bke.BKEPlayer;

public class Main {

    public static void main(String[] args) {
        BKEPlayer player = new BKEPlayer("test", "x");
        Board board = new Board();
        board.test();
        board.setPosition(0,0,player);
        board.getPlayerAtPosition(0,0);
        board.getTokenFromPosition(0,0);
    }
}

