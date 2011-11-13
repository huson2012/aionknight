/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 *    Aion-Knight is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Aion-Knight is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;


import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;


/**
 * @author Nemiroff
 * Date: 01.12.2009
 */
public class SM_TITLE_UPDATE extends AionServerPacket {
    private int objectId;
    private int titleId;

    /**
     * Constructs new <tt>SM_TITLE_UPDATE </tt> packet
     * @param player
     * @param titleId
     */
    public SM_TITLE_UPDATE(Player player, int titleId) {
        this.objectId = player.getObjectId();
        this.titleId = titleId;
    }

    @Override
	protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, this.objectId);
        writeD(buf, this.titleId);
    }
}
