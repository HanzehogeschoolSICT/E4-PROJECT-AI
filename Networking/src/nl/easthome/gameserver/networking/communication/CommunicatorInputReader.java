package nl.easthome.gameserver.networking.communication;

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
                    processor.processMessage(line);
                }
            }catch (Exception e){
                e.toString();
            }
        }
    }
}
