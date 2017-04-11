package nl.hanze2017e4.gameclient.model.games.bke;

import nl.hanze2017e4.gameclient.model.helper.TerminalPrinter;
import nl.hanze2017e4.gameclient.model.master.AbstractGame;
import nl.hanze2017e4.gameclient.model.master.Player;

import java.util.Random;

public class BkeGame extends AbstractGame<BkeBoard> {


    public BkeGame(Player player1, Player player2, int turnTimeInSec) {
        super(player1, player2, turnTimeInSec, new BkeBoard(player1, player2));
    }

    @Override
    protected int executeMyInteractiveMove(BkeBoard board) {
        return 0;
    }

    @Override
    protected int executeMyGUIMove(BkeBoard board) {
        return 0;
    }

    @Override
    protected int executeMyAIMove(BkeBoard board) {
        if (board.isEmpty()) {
            Random random = new Random();
            return random.nextInt(9);
        } else {
            BkeAI bkeAI = new BkeAI(board, getPlayer1(), getPlayer2());
            return bkeAI.calculateBestMove();
        }
    }

    @Override
    protected void launchGUIMode(BkeBoard board) {

    }

    @Override
    protected void updateGUIAfterMove(BkeBoard board) {

    }

    @Override
    protected void updateGUIAfterMatchEnd() {
        //TODO close GUI after match has concluded
    }

    @Override
    protected void onMoveDetected(Player player, int move) {
        super.getBoard().setPlayerAtPos(player, move);
        TerminalPrinter.println("GAME", ":green,n:BOARD", "-> ");
        TerminalPrinter.print(super.getBoard().toString());
    }
}