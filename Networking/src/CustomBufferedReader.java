import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class CustomBufferedReader extends BufferedReader {
    int size = 8192;

    public CustomBufferedReader(Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        //SOURCE = http://stackoverflow.com/questions/33035436/java-alternative-to-bufferedreader by @SubOptimal

        StringBuilder sb = new StringBuilder();
        int read = super.read();
        while (read )


        for (int read = super.read(); read >= 0 && read != '\n'; read = super.read()) {
            sb.append((char) read);
        }
        return sb.toString();
     }
}
