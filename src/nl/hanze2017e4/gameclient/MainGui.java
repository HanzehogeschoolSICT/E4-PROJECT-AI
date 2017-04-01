package nl.hanze2017e4.gameclient;

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
