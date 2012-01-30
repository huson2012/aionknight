/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Item;
import gameserver.model.items.GodStone;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_UPDATE_PLAYER_APPEARANCE extends AionServerPacket
{
	public int playerId;
	public int size;
	public List<Item> items;

	public SM_UPDATE_PLAYER_APPEARANCE(int playerId, List<Item> items)
	{
		this.playerId = playerId;
		this.items = items;
		this.size = items.size();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerId);
		
		short mask = 0;
		for(Item item : items)
		{
			mask |= item.getEquipmentSlot();
		}
		
		writeH(buf, mask);

		for(Item item : items)
		{		
			writeD(buf, item.getItemSkinTemplate().getTemplateId());
			GodStone godStone = item.getGodStone();
			writeD(buf, godStone != null ? godStone.getItemId() : 0); 
			writeD(buf, item.getItemColor());
			writeH(buf, 0x00); // unk (0x00)
		}
	}
}