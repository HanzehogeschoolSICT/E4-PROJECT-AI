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
		ArrayList<ReversiMove> legalMoves = detemineLegalMoves(possibleMoves, board);
		ReversiMove move = determineScore(legalMoves, board);
		return move.getMove();
	}

	private ArrayList<ReversiMove> detemineLegalMoves(ArrayList<ReversiMove> possibleMoves, Board board) {
		int prevScore = board.getScore(player1);
		ArrayList<ReversiMove> legalMoves = new ArrayList<>();

        for (int i = 0; i < possibleMoves.size(); i++) {
            if(possibleMoves.get(i).getScore() > prevScore + 1){
                legalMoves.add(possibleMoves.get(i));
            }
        }

        return legalMoves;
    }

	private ArrayList<ReversiMove> determinePossibleMoves(Board board) {
		Set<Integer> possibleMoveSet = new HashSet<>();

		for (int i = 0; i < 63; i++) {
		    int[] temp = {i-9,i-7,i+9,i+7,i+1,i-1,i-8,i+8};

		    if(board.getPlayerAtPos(i) == null){
                for (int j = 0; j < temp.length; j++) {
                    if (temp[j] >= 0 && temp[j] <= 63){
                        if(board.getPlayerAtPos(temp[j]) == player2){
                            possibleMoveSet.add(i);
                        }
                    }

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
