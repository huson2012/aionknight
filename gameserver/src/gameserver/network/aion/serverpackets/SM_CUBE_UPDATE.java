/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;


import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;


/**
 * 
 * @author Sweetkr
 *
 */
public class SM_CUBE_UPDATE extends AionServerPacket
{
	private Player player;
	private int cubeType;
	private int advancedSlots;

	/**
	 * Constructs new <tt>SM_CUBE_UPDATE</tt> packet
	 * 
	 * @param player
	 */
	public SM_CUBE_UPDATE(Player player, int cubeType, int advancedSlots)
	{
		this.player = player;
		this.cubeType = cubeType;
		this.advancedSlots = advancedSlots;
	}

	public SM_CUBE_UPDATE(Player player, int cubeType)
	{
		this.player = player;
		this.cubeType = cubeType;
		this.advancedSlots = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, cubeType);
		writeC(buf, advancedSlots);
		switch(cubeType)
		{
			case 0:
				writeD(buf, player.getInventory().size());
				writeC(buf, player.getCubeSize()); // cube size from npc (so max 5 for now)
				writeC(buf, 0); // cube size from quest (so max 2 for now)
				writeC(buf, 0); // unk
				break;
			case 6:
				break;
			default:
				break;
		}
	}
}
