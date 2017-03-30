package nl.easthome.gameclient.games.bke;

import nl.easthome.gameclient.games.master.AbstractGame;

/**
 * Copyright (C) 3/30/17 By joris
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
public class BKEGame extends AbstractGame<BKEPlayer> {

    public BKEGame(BKEPlayer p1, BKEPlayer p2, BKEPlayer playsFirst) {
       super(p1, p2, playsFirst);
    }


    @Override
    public void gameSetup() {

    }

    @Override
    public void gameStart() {

    }

    @Override
    public void processMove(BKEPlayer player, int move) {
        System.out.println("[DEBUG] = PROCESS MOVE > " + player + "@ " + move);

    }

    @Override
    public int thinkMove(BKEPlayer player) {
        return 0;
    }
}
