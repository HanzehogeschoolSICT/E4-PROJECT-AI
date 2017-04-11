package nl.hanze2017e4.gameclient.model.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalPrinter {


    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30;40;1m";
    public static final String RED = "\u001B[31;40;1m";
    public static final String GREEN = "\u001B[32;40;1m";
    public static final String YELLOW = "\u001B[33;40;1m";
    public static final String BLUE = "\u001B[34;40;1m";
    public static final String PURPLE = "\u001B[35;40;1m";
    public static final String CYAN = "\u001B[36;40;1m";
    public static final String WHITE = "\u001B[37;40;1m";


    /**
     * Parses a string with ANSI color codes based on the input
     *
     * @param input the input string
     *
     * @return the parsed ANSI string
     * SOURCE: https://gist.github.com/nathan-fiscaletti/9dc252d30b51df7d710a
     */
    private static String parseColors(String input) {
        String ret = input;
        Pattern regexChecker = Pattern.compile(":\\S+,\\S+:");
        Matcher regexMatcher = regexChecker.matcher(input);
        while (regexMatcher.find()) {
            if (regexMatcher.group().length() != 0) {
                String sub = regexMatcher.group().trim();
                sub = sub.replace(":", "");
                String[] colors = sub.split(",");

                ret = (colors[1].equalsIgnoreCase("N")) ?
                    ret.replace(
                        regexMatcher.group().trim(),
                        "\u001B[3" + getColorID(colors[0]) + ";1m"
                    )
                    :
                    ret.replace(
                        regexMatcher.group().trim(),
                        "\u001B[3" + getColorID(colors[0]) +
                            ";4" + getColorID(colors[1]) + ";1m"
                    );

                ret = ret.replace("[RC]", TerminalPrinter.RESET);
            }
        }
        ret = ret + TerminalPrinter.RESET;
        return ret;
    }

    /**
     * Internal function for getting a colors value
     *
     * @param color The color as test
     *
     * @return The colors integral value
     * SOURCE: https://gist.github.com/nathan-fiscaletti/9dc252d30b51df7d710a
     */
    private static int getColorID(String color) {
        if (color.equalsIgnoreCase("BLACK")) {
            return 0;
        } else if (color.equalsIgnoreCase("RED")) {
            return 1;
        } else if (color.equalsIgnoreCase("GREEN")) {
            return 2;
        } else if (color.equalsIgnoreCase("YELLOW")) {
            return 3;
        } else if (color.equalsIgnoreCase("BLUE")) {
            return 4;
        } else if (color.equalsIgnoreCase("MAGENTA")) {
            return 5;
        } else if (color.equalsIgnoreCase("CYAN")) {
            return 6;
        } else if (color.equalsIgnoreCase("WHITE")) {
            return 7;
        }

        return 7;
    }

    public static void println(String source, String subject, String message) {
        if (source.length() < 15) {
            int rest = 15 - source.length();
            String newStrPre = "";
            String newStrAft = "";

            if (rest % 2 != 0) {
                newStrPre += "-";
            }
            for (int i = 0; i < rest / 2; i++) {
                newStrPre += "-";
            }
            for (int i = 0; i < rest / 2; i++) {
                newStrAft += "-";
            }
            source = newStrPre + source + newStrAft;
        } else if (source.length() > 15) {
            source = source.substring(0, 14);
            source += ".";
        }

        System.out.println(parseColors("[" + source + "] = " + subject + " > " + message));
    }

    public static void print(String string) {
        System.out.println(parseColors(string));
    }
}
