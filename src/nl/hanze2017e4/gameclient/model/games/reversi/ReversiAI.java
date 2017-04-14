package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static nl.hanze2017e4.gameclient.model.games.reversi.ReversiAiCalculate.determineLegalMoves;
import static nl.hanze2017e4.gameclient.model.games.reversi.ReversiAiCalculate.determinePossibleMoves;

public class ReversiAI {

    private ReversiBoard sourceBoard;
    private ArrayList<ReversiMove> firstGenLegalMoves;
    private Player player1;
    private Player player2;
    private int timeForThinking;


    public ReversiAI(ReversiBoard sourceBoard, Player player1, Player player2, int timeForThinking) {
        this.sourceBoard = sourceBoard;
        this.player1 = player1;
        this.player2 = player2;
        this.timeForThinking = timeForThinking;
    }

    public int getBestMove() {
        firstGenLegalMoves = getFirstGenLegalMoves();

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        try {
            ArrayList<ReversiMove> processedMoves = new ArrayList<>();

            for (ReversiMove reversiMove : firstGenLegalMoves) {
                executorService.execute(() -> processedMoves.add(reversiMove.createNextGen()));
            }
            executorService.shutdown();
            if (!executorService.awaitTermination(timeForThinking, TimeUnit.SECONDS)) {
                System.out.println("TIMEOUT");
            }
            ;

            return ReversiAiCalculate.determineBestMove(processedMoves, player1, player2, 1).getMove();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private ArrayList<ReversiMove> getFirstGenLegalMoves() {
        ArrayList<ReversiMove> possibleMoves = determinePossibleMoves(sourceBoard, player1, player2, 1);
        return determineLegalMoves(possibleMoves, sourceBoard, player1, 1);
    }
}
