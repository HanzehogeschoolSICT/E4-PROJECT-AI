package nl.hanze2017e4.gameclient.model.games.reversi;

import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

public class ReversiGame extends AbstractGame<ReversiBoard> {

    public ReversiGame(Player player1, Player player2, int turnTimeInSec) {
        super(player1, player2, turnTimeInSec, new ReversiBoard(player1, player2));
    }

    @Override
    protected int executeMyInteractiveMove(ReversiBoard board) {
        return 0;
    }

    @Override
    protected int executeMyGUIMove(ReversiBoard board) {
        return 0;
    }

    @Override
    protected int executeMyAIMove(ReversiBoard board) {
        return new ReversiAI(board, getPlayer1(), getPlayer2(), 3, super.getTurnTimeInSec() - 2).getBestMove();
    }

    @Override
    protected void launchGUIMode(ReversiBoard board) {

    }

    @Override
    protected void updateGUIAfterMove(ReversiBoard board) {

    }

    @Override
    protected void updateGUIAfterMatchEnd() {

    }

    @Override
    protected void onMoveDetected(Player player, int move) {
        super.getBoard().setPlayerAtPos(player, move);
        String p1Score = "Our Score: " + super.getPlayer1().getSymbol() + " = " + super.getBoard().getScore(super.getPlayer1());
        String p2Score = "Opponent Score: " + super.getPlayer2().getSymbol() + " = " + super.getBoard().getScore(super.getPlayer2());

        TerminalPrinter.println("GAME", ":green,n:SCORE", p1Score + ", " + p2Score);
        TerminalPrinter.println("GAME", ":green,n:BOARD", "-> ");
        TerminalPrinter.print(super.getBoard().toString());
    }
}
