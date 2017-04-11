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

	public int calculateBestMove(Board board, Player player1) {
		ArrayList<ReversiMove> possibleMoves = determinePossibleMoves(board);
		ArrayList<ReversiMove> legalMoves = detemineLegalMoves(possibleMoves, player1);
		ReversiMove move = determineScore(legalMoves);
		return move.getMove();
	}

	private ArrayList<ReversiMove> detemineLegalMoves(ArrayList<ReversiMove> possibleMoves, Player player1) {
		ArrayList<ReversiMove> legalMoves = new ArrayList<>();

		for (ReversiMove move : possibleMoves) {
			if (canMoveBePlacedHere(player1, move)) {
				legalMoves.add(move);
			}
		}


		return legalMoves;
	}

	private boolean canMoveBePlacedHere(Player playerWhoPlaced, ReversiMove move) {
		if (hasTileDiagonal(move.getMove(), move.getMoveBoard(), playerWhoPlaced)) {
			System.out.println("MOVE " + move + "HAS DIAGONAL");
			return true;
		} else if (hasTileHorizontal(move.getMove(), move.getMoveBoard(), playerWhoPlaced)) {
			System.out.println("MOVE " + move + "HAS HORIZONTAL");
			return true;
		} else if (hasTileVertical(move.getMove(), move.getMoveBoard(), playerWhoPlaced)) {
			System.out.println("MOVE " + move + "HAS VERTICAL");
			return true;
		} else {
			return false;
		}
	}

	private boolean hasTileDiagonal(int pos, Board board, Player playerWhoPlaced) {
		ArrayList<Integer> streakPos = new ArrayList<>();
		boolean result = false;

		//diagonal top left  -9
		for (int i = (pos - 9); ((i > 0) && ((i + 1) % 8 != 0)); i = i - 9) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		//diagonal top right -7
		for (int i = (pos - 7); ((i > 0) && (i % 8 != 0)); i = i - 7) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		//diagonal bot left +7
		for (int i = (pos + 7); i < 64; i = i + 7) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		//diagonal bot right +9
		for (int i = (pos + 9); i < 64; i = i + 9) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		return result;
	}

	private boolean hasTileHorizontal(int pos, Board board, Player playerWhoPlaced) {
		ArrayList<Integer> streakPos = new ArrayList<>();
		boolean result = false;

		// horizontal right +1 tot einde row
		for (int i = (pos + 1); ((i) % 8 != 0); i++) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		// -1 tot begin row
		for (int i = (pos - 1); ((i + 1) % 8 != 0); i--) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		return false;
	}

	private boolean hasTileVertical(int pos, Board board, Player playerWhoPlaced) {
		ArrayList<Integer> streakPos = new ArrayList<>();
		boolean result = false;

		// +8 tot begin col
		for (int i = (pos + 8); i < 63; i = i + 8) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		// -8 tot eind col
		for (int i = (pos - 8); i > 0; i = i - 8) {
			StreakIdentifier streakIdentifier = verifyStreak(i, playerWhoPlaced, streakPos, board);
			if (streakIdentifier != StreakIdentifier.OPPONENTTILE) {
				if (streakIdentifier == StreakIdentifier.OWNTILEWITHCOMPLETESTREAK) {
					result = true;
				}
				break;
			}
		}
		return result;
	}

	private StreakIdentifier verifyStreak(int pos, Player playerWhoPlaced, ArrayList<Integer> streakPos, Board board) {
		int valueAtPos = 0;

		if (board.getPlayerAtPos(pos) != null) {
			valueAtPos = board.getPlayerAtPos(pos).getUserID();
		}

		if (valueAtPos == playerWhoPlaced.getUserID()) {
			if (streakPos.size() > 0) {

				streakPos.clear();
				return StreakIdentifier.OWNTILEWITHCOMPLETESTREAK;
			} else {
				return StreakIdentifier.OWNTILENOSTREAK;
			}
		} else if (valueAtPos != 0) {
			streakPos.add(pos);
			return StreakIdentifier.OPPONENTTILE;
		} else {
			return StreakIdentifier.EMPTYTILE;
		}
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

	private ReversiMove determineScore(ArrayList<ReversiMove> legalMoves) {

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

	public enum StreakIdentifier {
		EMPTYTILE,
		OPPONENTTILE,
		OWNTILEWITHCOMPLETESTREAK,
		OWNTILENOSTREAK;
	}

}
