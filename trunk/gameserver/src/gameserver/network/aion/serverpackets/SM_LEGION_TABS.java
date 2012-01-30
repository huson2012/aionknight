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

import gameserver.model.legion.LegionHistory;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SM_LEGION_TABS extends AionServerPacket
{
	private int	page;
	private Collection<LegionHistory> legionHistory;

	public SM_LEGION_TABS(Collection<LegionHistory> legionHistory)
	{
		this.legionHistory = legionHistory;
		this.page = 0;
	}

	public SM_LEGION_TABS(Collection<LegionHistory> legionHistory, int page)
	{
		this.legionHistory = legionHistory;
		this.page = page;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		/**
		 * If history size is less than page*8 return
		 */
		if(legionHistory.size() < (page * 8))
			return;
		
		// TODO: Formula's could use a refactor
		int hisSize = legionHistory.size() - (page * 8);

		if(page == 0 && legionHistory.size() > 8)
			hisSize = 8;
		if(page == 1 && legionHistory.size() > 16)
			hisSize = 8;
		if(page == 2 && legionHistory.size() > 24)
			hisSize = 8;

		writeD(buf, 0x12); // Unk
		writeD(buf, page); // current page
		writeD(buf, hisSize);

		int i = 0;
		for(LegionHistory history : legionHistory)
		{
			if(i >= (page * 8) && i <= (8 + (page * 8)))
			{
				writeD(buf, (int) (history.getTime().getTime() / 1000));
				writeC(buf, history.getLegionHistoryType().getHistoryId());
				writeC(buf, 0);
				if(history.getName().length() > 0)
				{
					writeS(buf, history.getName());
					int size = 134 - (history.getName().length() * 2 + 2);
					writeB(buf, new byte[size]);
				}
				else
					writeB(buf, new byte[134]);
			}
			i++;
			if(i >= (8 + (page * 8)))
				break;
		}
		writeH(buf, 0);
	}
}