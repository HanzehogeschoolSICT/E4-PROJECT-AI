package nl.hanze2017e4.gameclient.model.games.bke;

import nl.hanze2017e4.gameclient.model.master.AbstractBoard;
import nl.hanze2017e4.gameclient.model.master.Player;

public class BkeBoard extends AbstractBoard {

    public BkeBoard(Player player1, Player player2) {
        super(3, 3, player1, player2);
    }

    public BkeBoard(BkeBoard board) {
        super(board);
    }

    /**
     * Returns the score (win or loss or draw) for certain board
     *
     * @param player
     * @param opponent
     *
     * @return
     */
    public int getBoardScore(Player player, Player opponent) {


        //check for win in rows and columns
        for (int i = 0; i < this.getRows(); i++) {
            if (this.getBoard()[i][0] == this.getBoard()[i][1] && this.getBoard()[i][1] == this.getBoard()[i][2]) {
                if (this.getBoard()[i][0] == player.getUserID()) {
                    return 1;
                }

                if (this.getBoard()[i][0] == opponent.getUserID()) {
                    return -1;
                }

            }
            if (this.getBoard()[0][i] == this.getBoard()[1][i] && this.getBoard()[1][i] == this.getBoard()[2][i]) {
                if (this.getBoard()[0][i] == player.getUserID()) {
                    return 1;
                }

                if (this.getBoard()[0][i] == opponent.getUserID()) {
                    return -1;
                }
            }
        }

        //check for win in diagonal
        if (this.getBoard()[0][0] == this.getBoard()[1][1] && this.getBoard()[1][1] == this.getBoard()[2][2]) {
            if (this.getBoard()[0][0] == player.getUserID()) {
                return 1;
            }

            if (this.getBoard()[0][0] == opponent.getUserID()) {
                return -1;
            }

        }

        //check for win in other diagonal
        if (this.getBoard()[0][2] == this.getBoard()[1][1] && this.getBoard()[1][1] == this.getBoard()[2][0]) {
            if (this.getBoard()[0][2] == player.getUserID()) {
                return 1;
            }

            if (this.getBoard()[0][2] == opponent.getUserID()) {
                return -1;
            }
        }

        //else return draw
        return 0;
    }

    public boolean isFull() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                if (this.getBoard()[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                if (this.getBoard()[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
