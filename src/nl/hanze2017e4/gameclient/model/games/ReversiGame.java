package nl.hanze2017e4.gameclient.model.games;

import nl.hanze2017e4.gameclient.model.games.reversi.ReversiAI;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Board;
import nl.hanze2017e4.gameclient.model.master.Player;

public class ReversiGame extends AbstractGame {

    public ReversiGame(Player player1, Player player2, Player playsFirst, int turnTimeInSec) {
        super(8, 8, player1, player2, playsFirst, turnTimeInSec);
        setBoardBeginState();
    }

    private void setBoardBeginState() {
        super.getBoard().setPlayerAtPos(getPlaysFirst(), 28);
        super.getBoard().setPlayerAtPos((getPlaysFirst().equals(getPlayer1()) ? getPlayer2() : getPlayer1()), 27);
        super.getBoard().setPlayerAtPos(getPlaysFirst(), 35);
        super.getBoard().setPlayerAtPos((getPlaysFirst().equals(getPlayer1()) ? getPlayer2() : getPlayer1()), 36);
    }

    @Override
    protected int executeMyGUIMove(Board board) {
        return 0;
    }

    @Override
    protected int executeMyAIMove(Board board) {
        ReversiAI reversiAI = new ReversiAI(board, getPlayer1(), getPlayer2(), 3);

        return reversiAI.calculateBestMove(board);
    }

    @Override
    protected void launchGUIMode(Board board) {

    }

    @Override
    public int getBoardScore(Player player1, Player player2, Board board) {
        return 0;
    }

    @Override
    protected void updateGUIAfterMove(Board board) {

    }

    @Override
    protected void updateGUIAfterMatchEnd() {

    }


}
