public class NetworkingTest {

    public static void main(String[] args) {
        Communicator c = new Communicator("145.33.225.170", 7789);
        c.connect();
        c.help();
        c.disconnect();
    }
}
