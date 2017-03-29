public class NetworkingTest {

    public static void main(String[] args) {
        //Communicator c = new Communicator("145.33.225.170", 7789);
        Communicator c = new Communicator("localhost", 7789, GameMode.TICTACTOE);
        c.connect();
        c.login("jorisdev");
        c.get(GetCommandArgument.PLAYERLIST);
        //c.waitForNextMove();
        //c.bye();
    }
}
