package nl.hanze2017e4.gameclient;

public class SETTINGS {

    //How many generations to look forward.
    public static final int GENERATION_LIMIT = 4;

    //How long we have on the server to make a move
    public static final int SERVER_TURN_TIME = 10;

    //How much threads can run simultaneously.
    public static final int NUMBER_OF_THREADS_PER_SUBMOVE_SIMULTANEOUSLY = 100;

    //Number of milliseconds to take into account when awaiting threads, in order to make a successful move even if threads take too long.
    public static final int SAVE_MOVE_DELAY = 500;

    //True when wanting to see the cyan messages in the console. Can cause lag.
    public static final boolean DEBUG_AIMODE = true;

    //
    public static final int SCORE_PENALTY_FOR_LEADING_TO_DRAW = -50;
}
