import nl.easthome.gameserver.networking.communication.Communicator;
import nl.easthome.gameserver.networking.master.AbstractGame;
import nl.easthome.gameserver.networking.master.AbstractPlayer;

/**
 * Copyright (C) 3/29/17 By joris
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
public class BKEGame extends AbstractGame {

    public BKEGame(Communicator.GameMode gameMode, AbstractPlayer player1, AbstractPlayer player2) {
        super(gameMode, player1, player2);
    }

    @Override
    public void gameSetup() {

    }

    @Override
    public void gameStart() {

    }

    @Override
    public void processMove(AbstractPlayer player, int move) {

    }

    @Override
    public int thinkMove(AbstractPlayer player) {
        return 0;
    }
}
