package nl.hanze2017e4.gameclient.model.network;

public class Command {

    Type command;
    String args;

    public Command(Type command, String args) {
        this.command = command;
        this.args = args;
    }

    public Type getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public enum Type {
        LOGIN("login"),
        HELP("help"),
        GET("get"),
        SUBSCRIBE("subscribe"),
        CHALLENGE("challenge"),
        CHALLENGE_ACCEPT("challenge accept"),
        MOVE("move"),
        FORFEIT("forfeit"),
        UNKNOWN("unknown");

        String name;

        Type(String string) {
            this.name = string;
        }
    }

    public enum Mode {
        GAMELIST("gamelist"),
        PLAYERLIST("playerlist");

        public String string;

        Mode(String string) {
            this.string = string;
        }
    }
}
