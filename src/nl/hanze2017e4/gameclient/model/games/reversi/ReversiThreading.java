package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.SETTINGS;
import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class ReversiThreading {

    private static ExecutorService executorService;

    public static void executeInThreadingPool(Runnable runnable) throws RejectedExecutionException {
        if (executorService == null || executorService.isShutdown() || executorService.isTerminated()) {
            executorService = Executors.newCachedThreadPool();
        }
        executorService.execute(runnable);
    }

    /**
     * Shuts the executorService down and waits for the threads to finish in a certain period of time.
     *
     * @return True if all threads finished on time, and if no errors occurred while shutting down.
     */
    public static boolean shutdownAndWait() {
        try {
            Thread.sleep(SETTINGS.SERVER_TURN_TIME - SETTINGS.WAIT_DELAY);
            executorService.shutdown();

            if (!executorService.awaitTermination((SETTINGS.WAIT_DELAY - SETTINGS.TERMINATION_DELAY), TimeUnit.MILLISECONDS)) {
                TerminalPrinter.println("AI", ":cyan,n:TIMEOUT", "The subtreads took too long, reverting back to safeMove.");
                return false;
            } else {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }


}
