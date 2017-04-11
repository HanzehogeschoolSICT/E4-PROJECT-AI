package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ReversiAI {

    Board board;
    Player player1;
    Player player2;
    int lookForwardMoves;
    private ArrayList<ReversiMove> legalMoves;
    /**
     * @param board
     * @param player1
     * @param player2
     * @param lookForwardMoves Number of moves to look ahead. Including opponent moves.
     */
    public ReversiAI(Board board, Player player1, Player player2, int lookForwardMoves) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.lookForwardMoves = lookForwardMoves;

    }

	public int calculateBestMove(Board board) {
		ArrayList<ReversiMove> possibleMoves = determinePossibleMoves(board);
		ArrayList<ReversiMove> legalMoves = detemineLegalMoves(board);
		ReversiMove move = determineScore(legalMoves, board);
		return move.getMove();
	}

	private ArrayList<ReversiMove> detemineLegalMoves(Board board) {
		return null;
	}

	private ArrayList<ReversiMove> determinePossibleMoves(Board board) {
		Set<Integer> possibleMoveSet = new HashSet<>();

		for (int i = 0; i < 63; i++) {
			int diagonallTopLeft = i - 9;
			int diagonallTopRight = i - 7;
			int diagonalBotRight = i + 9;
			int diagonalBotLeft = i + 7;
			int horizontalRight = i + 1;
			int horizontalLeft = i - 1;
			int verticalUp = i - 8;
			int verticalDown = i + 8;

			if (board.getPlayerAtPos(i) == null) {
				try {
					// diagonal checker
					if (board.getPlayerAtPos(diagonallTopLeft) != null || board.getPlayerAtPos(diagonalBotRight) != null || board.getPlayerAtPos(diagonallTopRight) != null || board.getPlayerAtPos(diagonalBotLeft) != null) {
						possibleMoveSet.add(i);
					}
					//horizontal checker
					if (board.getPlayerAtPos(horizontalRight) != null || board.getPlayerAtPos(horizontalLeft) != null) {
						possibleMoveSet.add(i);
					}
					//vertical checker
					if (board.getPlayerAtPos(verticalUp) != null || board.getPlayerAtPos(verticalDown) != null) {
						possibleMoveSet.add(i);
					}
				} catch (IndexOutOfBoundsException ex) {

				}
			}
		}
		ArrayList<ReversiMove> possiblePositions = new ArrayList<>();

		for (Integer legalMove : possibleMoveSet) {
			possiblePositions.add(new ReversiMove(player1, legalMove, board));
		}

		return possiblePositions;
	}

	private ReversiMove determineScore(ArrayList<ReversiMove> legalMoves, Board board) {

		ReversiMove bestMove = legalMoves.get(0);
		int bestValue = 0;

		for (int i = 0; i < legalMoves.size(); i++) {
			int thisScore = legalMoves.get(i).getScore();
			if (thisScore > bestValue) {
				bestValue = thisScore;
				bestMove = legalMoves.get(i);
			}
		}

        //return the move we want to play
        return bestMove;
    }

}
