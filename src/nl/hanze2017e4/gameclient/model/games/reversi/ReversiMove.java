package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiMove {

    private Player playerMoves;
    private Player otherPlayer;
    private int generation;
    private int move;
    private ReversiBoard boardAfterMove;
    private int score;
    private int allGenMoveScore;
    private ArrayList<ReversiMove> nextGenMoveList;

    public ReversiMove(Player playerMoves, Player otherPlayer, int move, ReversiBoard sourceBoard, int generation) {
        this.playerMoves = playerMoves;
        this.otherPlayer = otherPlayer;
        this.move = move;
        this.boardAfterMove = makeBoardAfterMove(move, sourceBoard);
        this.nextGenMoveList = new ArrayList<>();
        this.score = boardAfterMove.getScore(playerMoves);
        this.generation = generation;
    }

    public void createNextGen() {

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
            '}';
    }


}
