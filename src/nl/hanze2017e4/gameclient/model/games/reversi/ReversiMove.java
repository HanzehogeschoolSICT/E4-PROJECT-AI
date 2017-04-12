package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.SETTINGS;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;

public class ReversiMove {

    private Player playerMoves;
    private Player opponent;
    private int move;
    private ReversiBoard boardAfterMove;
    private int score;
    private int allGenMoveScore;
    private ArrayList<ReversiMove> nextGenLegal;
    private int generation;

    public ReversiMove(Player playerMoves, Player opponent, int move, ReversiBoard sourceBoard, int generation) {
        this.playerMoves = playerMoves;
        this.opponent = opponent;
        this.move = move;
        this.boardAfterMove = makeBoardAfterMove(move, sourceBoard);
        if (sourceBoard != null) {
            this.score = boardAfterMove.getScore(playerMoves);
        } else {
            this.score = -50;
        }


        this.generation = generation;
    }

    public ReversiMove createNextGen() {
        ArrayList<ReversiMove> nextGenPossible = ReversiAiCalculate.determinePossibleMoves(boardAfterMove, opponent, playerMoves, generation + 1);
        nextGenLegal = ReversiAiCalculate.determineLegalMoves(nextGenPossible, boardAfterMove, opponent, generation + 1);
        if (generation <= SETTINGS.GENERATIONLIMIT) {
            ArrayList<ReversiMove> foundBestMoves = new ArrayList<>();

            for (ReversiMove nextGenMove : nextGenLegal) {
                foundBestMoves.add(nextGenMove.createNextGen());
            }

            ReversiMove bestNextMove = ReversiAiCalculate.determineBestMove(foundBestMoves, opponent, playerMoves, generation);

            if (!playerMoves.equals(boardAfterMove.getPlayerTwo())) {
                score += bestNextMove.getScore();
            } else {
                score -= bestNextMove.getScore();
            }

            return this;
        }
        return this;
    }

    private ReversiBoard makeBoardAfterMove(int move, ReversiBoard sourceBoard) {
        if (move < 0) {
            return null;
        } else {
            boardAfterMove = new ReversiBoard(sourceBoard);
            boardAfterMove.setPlayerAtPos(playerMoves, move);
            return boardAfterMove;
        }
    }

    public int getMove() {
        return move;
    }

    public ArrayList<ReversiMove> getNextGenLegal() {
        return nextGenLegal;
    }

    public void addMoveToNextGenMoveList(ReversiMove nextMove) {
        nextGenLegal.add(nextMove);
    }

    public int getScore() {
        return score;
    }

    public ReversiBoard getBoardAfterMove() {
        return boardAfterMove;
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


    public int getGeneration() {
        return generation;
    }
}
