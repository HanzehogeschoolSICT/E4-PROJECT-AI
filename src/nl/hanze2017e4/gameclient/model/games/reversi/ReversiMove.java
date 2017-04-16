package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.SETTINGS;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReversiMove implements Runnable {

    private Player playerMoves;
    private Player otherPlayer;
    private int move;
    private ReversiBoard boardAfterMove;
    private int score;
    private int allGenMoveScore;
    private ArrayList<ReversiMove> nextGenMoveList;
    private int lookForwardSteps;
    private int priority;

    public ReversiMove(Player playerMoves, Player otherPlayer, int move, ReversiBoard sourceBoard, int lookForwardSteps) {
        this.playerMoves = playerMoves;
        this.otherPlayer = otherPlayer;
        this.move = move;
        this.boardAfterMove = makeBoardAfterMove(move, sourceBoard);
        this.nextGenMoveList = new ArrayList<>();
        this.score = boardAfterMove.getScore(playerMoves);
        this.lookForwardSteps = lookForwardSteps;
    }

    @Override
    public void run() {
        if (lookForwardSteps > 0) {

            ArrayList<ReversiMove> possibleMoves = ReversiAI.determinePossibleMoves(boardAfterMove, otherPlayer, playerMoves, lookForwardSteps);
            nextGenMoveList = ReversiAI.determineLegalMoves(possibleMoves, boardAfterMove, otherPlayer, lookForwardSteps);
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (ReversiMove move : nextGenMoveList) {
                executorService.execute(move);
            }
            try {
                executorService.shutdown();
                executorService.awaitTermination(SETTINGS.MAX_TIME_PER_THREAD_FROM_SECOND_GEN_IN_MILISECONDS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //TODO this is still weird. I dont know how to fix this right now @ 22:00...
            ReversiMove bestMove = ReversiAI.determineBestMove(nextGenMoveList, boardAfterMove, otherPlayer, playerMoves, lookForwardSteps);

            if (lookForwardSteps % 2 == 0) {
                score -= bestMove.getAllGenMoveScore();
            } else {
                score += bestMove.getAllGenMoveScore();
            }
        }
    }


    private ReversiBoard makeBoardAfterMove(int move, ReversiBoard sourceBoard) {
        if (move < 0) {
            return sourceBoard;
        } else {
            boardAfterMove = new ReversiBoard(sourceBoard);
            boardAfterMove.setPlayerAtPos(playerMoves, move);
            return boardAfterMove;
        }
    }

    public int getPriorityScore(){

        if(contains(this.getMove(),RevsiPosPriority.getHighestPriority())){
            this.priority = RevsiPosPriority.getHighestPriorityValue();
        }

        if(contains(this.getMove(),RevsiPosPriority.getHighPriority())){
            this.priority = RevsiPosPriority.getHighPriorityValue();
        }

        if(contains(this.getMove(),RevsiPosPriority.getNormalPriorty())){
            this.priority = RevsiPosPriority.getNormalPriorityValue();
        }

        if(contains(this.getMove(),RevsiPosPriority.getLowPriorty())){
            this.priority = RevsiPosPriority.getLowPriorityValue();
        }

        if(contains(this.getMove(),RevsiPosPriority.getLowestPriorty())){
            this.priority = RevsiPosPriority.getLowestPriorityValue();
        }

        return this.score + priority;
    }

    private boolean contains(int match, int[] array){

        for (int i = 0; i < array.length; i++) {
            if(match == array[i]){
                return true;
            }
        }

        return false;
    }

    public int getMove() {
        return move;
    }

    public ArrayList<ReversiMove> getNextGenMoveList() {
        return nextGenMoveList;
    }

    public void addMoveToNextGenMoveList(ReversiMove nextMove) {
        nextGenMoveList.add(nextMove);
    }

    public int getScore() {
        return boardAfterMove.getScore(playerMoves);
    }

    public ReversiBoard getBoardAfterMove() {
        return boardAfterMove;
    }

    public int getLookForwardSteps() {
        return lookForwardSteps;
    }

    public int getAllGenMoveScore() {
        return allGenMoveScore;
    }

    public synchronized void setAllGenMoveScore(int allGenMoveScore) {
        this.allGenMoveScore = allGenMoveScore;
    }

    @Override
    public String toString() {
        return "ReversiMove{" +
            "playerMoves=" + playerMoves.getUsername() +
            ", move=" + move +
            ", score=" + score +
            ", priorityvalue=" + (getPriorityScore()-score) +
            '}';
    }


}
