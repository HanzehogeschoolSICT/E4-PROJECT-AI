package nl.hanze2017e4.gameclient;

public class SETTINGS {

    //How many generations to look forward.
    public static final int GENERATION_LIMIT = 6;



    //Number of milliseconds to take into account when awaiting threads, in order to make a successful move even if threads take too long.
    public static final int SAVE_MOVE_DELAY = 500;

    //True when wanting to see the cyan messages in the console. Can cause lag.
    public static final boolean DEBUG_AIMODE = false;

    //
    public static final int SCORE_PENALTY_FOR_LEADING_TO_DRAW = -50;


    //How long we have on the server to make a move in milliseconds
    public static final int SERVER_TURN_TIME = 10000;
    //Server turn time minus this is the time the system can go in depth.
    public static final int WAIT_DELAY = 2000;
    //Wait_Delay minus this time is the time the system has to report a good move.
    public static final int TERMINATION_DELAY = 1000;
}
