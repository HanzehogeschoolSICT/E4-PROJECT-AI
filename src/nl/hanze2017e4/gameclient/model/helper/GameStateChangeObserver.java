package nl.hanze2017e4.gameclient.model.helper;

import nl.hanze2017e4.gameclient.model.master.AbstractGame;

public interface GameStateChangeObserver {
    void onNewGameDetected(String gameMode, String opponentName, String playsFirst);

    void onNewMoveDetected(String playerUsername, String move, String details);

    void onMyTurnDetected();

    void onEndGameDetected(AbstractGame.GameState gameEnd);
}
