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
    private int priority;

    public ReversiMove(Player playerMoves, Player opponent, int move, ReversiBoard sourceBoard, int generation) {
        this.playerMoves = playerMoves;
        this.opponent = opponent;
        this.move = move;
        this.boardAfterMove = makeBoardAfterMove(move, sourceBoard);
        this.score = boardAfterMove != null ? boardAfterMove.getScore(playerMoves) : SETTINGS.SCORE_PENALTY_FOR_LEADING_TO_DRAW;
        this.generation = generation;
        setPriorityScore();
    }

    public void createNextGen() {
        ReversiAI reversiAI = new ReversiAI(boardAfterMove, opponent, playerMoves, generation + 1);
        ReversiMove bestReversiMove = reversiAI.getBestMove();

        //To handle a pass when no move can be made by the opponent.
        if (playerMoves.equals(boardAfterMove.getPlayerOne())) {
            //us
            score += bestReversiMove.getScore();
            priority = bestReversiMove.getPriority();
        } else {
            //opponent
            if (!(bestReversiMove.getMove() == -1)) {
                score -= bestReversiMove.getScore();
                priority = (bestReversiMove.getPriority() * -1);
            }
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
            ", priority=" + priority +
            '}';
    }


    public void setPriorityScore() {

        if (contains(this.getMove(), ReversiPosPriority.getHighestPriority())) {
            this.priority = ReversiPosPriority.getHighestPriorityValue();
        } else if (contains(this.getMove(), ReversiPosPriority.getHighPriority())) {
            this.priority = ReversiPosPriority.getHighPriorityValue();
        } else if (contains(this.getMove(), ReversiPosPriority.getNormalPriorty())) {
            this.priority = ReversiPosPriority.getNormalPriorityValue();
        } else if (contains(this.getMove(), ReversiPosPriority.getLowPriorty())) {
            this.priority = ReversiPosPriority.getLowPriorityValue();
        } else if (contains(this.getMove(), ReversiPosPriority.getLowestPriorty())) {
            this.priority = ReversiPosPriority.getLowestPriorityValue();
        } else {
            priority = 0;
        }
    }

    private boolean contains(int match, int[] array) {

        for (int pos : array) {
            if (match == pos) {
                return true;
            }
        }

        return false;
    }


    public int getGeneration() {
        return generation;
    }

    public int getPriority() {
        return priority;
    }
}
