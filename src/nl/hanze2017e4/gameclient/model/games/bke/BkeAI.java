package nl.hanze2017e4.gameclient.model.games.bke;

import nl.hanze2017e4.gameclient.model.master.Player;

public class BkeAI {

    private BkeBoard sourceBoard;
    private Player player1;
    private Player player2;

    public BkeAI(BkeBoard sourceBoard, Player player1, Player player2) {
        this.sourceBoard = sourceBoard;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int calculateBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];
        BkeBoard possibleBoard = new BkeBoard(sourceBoard);

        for (int i = 0; i < sourceBoard.getRows(); i++) {
            for (int j = 0; j < sourceBoard.getColumns(); j++) {
                if (possibleBoard.getPlayerAtXY(i, j) == null) {
                    possibleBoard.setPlayerAtXY(sourceBoard.getPlayerOne(), i, j);
                    int thisScore = miniMax(false, sourceBoard);
                    if (thisScore > bestScore) {
                        bestScore = thisScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                    possibleBoard.clearPlayerAtXY(i, j);
                }
            }
        }
        return sourceBoard.convertXYToPos(bestMove[0], bestMove[1]);
    }

    /**
     * MiniMax algorithm for determining best move
     *
     * @param ourTurn
     *
     * @return
     */
    public int miniMax(boolean ourTurn, BkeBoard board) {
        BkeBoard moveBoard = new BkeBoard(board);

        int currentScore = moveBoard.getBoardScore(moveBoard.getPlayerOne(), moveBoard.getPlayerTwo());

        //if won or lost, return corresponding score
        if (currentScore == 1 || currentScore == -1) {
            return currentScore;
        }

        //if no win or los, but sourceBoard full, return draw (0)
        if (moveBoard.isFull()) {
            return 0;
        }

        if (ourTurn) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < moveBoard.getRows(); i++) {
                for (int j = 0; j < moveBoard.getColumns(); j++) {
                    if (moveBoard.getBoard()[i][j] == 0) {
                        moveBoard.setPlayerAtXY(player1, i, j);
                        int thisScore = miniMax(false, moveBoard);
                        if (thisScore > bestScore) {
                            bestScore = thisScore;
                        }
                        moveBoard.clearPlayerAtXY(i, j);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < moveBoard.getRows(); i++) {
                for (int j = 0; j < moveBoard.getColumns(); j++) {
                    if (moveBoard.getBoard()[i][j] == 0) {
                        moveBoard.setPlayerAtXY(player2, i, j);
                        int thisScore = miniMax(true, moveBoard);
                        if (thisScore < bestScore) {
                            bestScore = thisScore;
                        }
                        moveBoard.clearPlayerAtXY(i, j);
                    }
                }
            }
            return bestScore;
        }
    }


}
