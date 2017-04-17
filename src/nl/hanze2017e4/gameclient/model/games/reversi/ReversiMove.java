package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.SETTINGS;
import nl.hanze2017e4.gameclient.model.master.Player;

public class ReversiMove {

    private Player playerMoves;
    private Player opponent;
    private int move;
    private ReversiBoard boardAfterMove;
    private int score;
    private int generation;

    public ReversiMove(Player playerMoves, Player opponent, int move, ReversiBoard sourceBoard, int generation) {
        this.playerMoves = playerMoves;
        this.opponent = opponent;
        this.move = move;
        this.boardAfterMove = makeBoardAfterMove(move, sourceBoard);
        this.score = boardAfterMove != null ? boardAfterMove.getScore(playerMoves) : SETTINGS.SCORE_PENALTY_FOR_LEADING_TO_DRAW;
        this.generation = generation;
    }

    public void createNextGen() {
        ReversiAI reversiAI = new ReversiAI(boardAfterMove, opponent, playerMoves, generation + 1);
        ReversiMove bestReversiMove = reversiAI.getBestMove();

        if (!playerMoves.equals(boardAfterMove.getPlayerTwo())) {
            score += bestReversiMove.getScore();
        } else {
            score -= bestReversiMove.getScore();
        }

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

    public int getScore() {
        return score;
    }

    public int getMove() {
        return move;
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
