package nl.hanze2017e4.gameclient.model.games.reversi;

public class ReversiPosPriority {
    private static final int[] highestPriority = new int[]{0, 7, 56, 63};
    private static final int[] highPriority = new int[]{2, 3, 4, 5, 16, 24, 32, 40, 58, 59, 60, 61, 23, 31, 39, 47};
    private static final int[] normalPriorty = new int[]{18, 19, 20, 21, 26, 27, 28, 29, 34, 35, 36, 37, 42, 43, 44, 45};
    private static final int[] lowPriorty = new int[]{10, 11, 12, 13, 17, 22, 25, 30, 33, 38, 41, 46, 50, 51, 52, 53};
    private static final int[] lowestPriorty = new int[]{1, 6, 8, 9, 14, 15, 48, 49, 54, 55, 57, 62};
    private static final int highestPriorityValue = 0;
    private static final int highPriorityValue = 0;
    private static final int normalPriorityValue = 0;
    private static final int lowPriorityValue = -0;
    private static final int lowestPriorityValue = -0;

    public static int[] getHighestPriority() {
        return highestPriority;
    }

    public static int[] getHighPriority() {
        return highPriority;
    }

    public static int[] getNormalPriorty() {
        return normalPriorty;
    }

    public static int[] getLowPriorty() {
        return lowPriorty;
    }

    public static int[] getLowestPriorty() {
        return lowestPriorty;
    }

    public static int getHighestPriorityValue() {
        return highestPriorityValue;
    }

    public static int getHighPriorityValue() {
        return highPriorityValue;
    }

    public static int getNormalPriorityValue() {
        return normalPriorityValue;
    }

    public static int getLowPriorityValue() {
        return lowPriorityValue;
    }

    public static int getLowestPriorityValue() {
        return lowestPriorityValue;
    }
}
