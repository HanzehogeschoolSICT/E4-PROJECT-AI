import java.io.BufferedReader;

/**
 * Copyright (C) 3/29/17 By joris
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class TelnetInputReader extends Thread {

    private boolean threadswitch = true;
    private BufferedReader inputStream;
    private TelnetInputReaderObserver observer;

    public TelnetInputReader(BufferedReader bufferedReader, TelnetInputReaderObserver observer ) {
        this.inputStream=bufferedReader;
        this.observer = observer;
        this.start();
    }

    @Override
    public void run() {
        while(threadswitch){
            try {
                String line;
                while((line=inputStream.readLine())!=null){
                    observer.onMessageReceived(line);
                }
            }catch (Exception e){
                e.toString();
            }
        }
    }

    public interface TelnetInputReaderObserver {
        void onMessageReceived(String message);
    }

    public void setThreadswitch(boolean threadswitch) {
        this.threadswitch = threadswitch;
    }
}
