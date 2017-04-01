package nl.hanze2017e4.gameclient.model.network;

/**
 * Copyright (C) 4/1/17 By joris
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Command {

    CommandType command;
    String args;

    public Command(CommandType command, String args) {
        this.command = command;
        this.args = args;
    }

    public CommandType getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public enum CommandType {
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

        CommandType(String string) {
            this.name = string;
        }
    }

    public enum GetMode {
        GAMELIST("gamelist"),
        PLAYERLIST("playerlist");

        public String string;

        GetMode(String string) {
            this.string = string;
        }
    }
}
