package nl.hanze2017e4.gameclient;/**
 * Copyright (C) 4/1/17 By joris
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

import javafx.application.Application;
import javafx.stage.Stage;
import nl.hanze2017e4.gameclient.controller.InteractiveMode;
import nl.hanze2017e4.gameclient.model.network.Communicator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("Duplicates")
public class MainGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //TODO GUI stuff, pack as much stuff in the view package.


        Communicator communicator = new Communicator("localhost", 7789, 60);
        communicator.start();
        try {
            communicator.join();
            ExecutorService threadPool = Executors.newFixedThreadPool(5);
            threadPool.execute(communicator.createCommunicatorInputReader());
            threadPool.execute(new InteractiveMode(communicator));
            threadPool.execute(communicator.createCommunicatorInputProcessor());
            threadPool.execute(communicator.createCommunicatorOutputPlacer());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
