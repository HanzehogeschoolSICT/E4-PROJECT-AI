package nl.easthome.gameclient.networking.communication;

import java.io.BufferedReader;

public class CommunicatorInputReader extends Thread {

    private boolean threadswitch = true;
    private BufferedReader inputStream;
    private CommunicatorInputProcessor processor;

    public CommunicatorInputReader(BufferedReader bufferedReader, CommunicatorInputProcessor processor ) {
        this.inputStream = bufferedReader;
        this.processor = processor;
        this.start();
    }

    @Override
    public void run() {
        while(threadswitch){
            try {
                String line;
                while((line=inputStream.readLine())!=null){
                    processor.submitMessage(line);
                }
            }catch (Exception e){
                e.toString();
            }
        }
    }
}
