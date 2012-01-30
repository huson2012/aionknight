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

import gameserver.model.siege.Influence;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;
import java.nio.ByteBuffer;

public class SM_INFLUENCE_RATIO extends AionServerPacket
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		Influence inf = Influence.getInstance();
		
		writeD(buf, SiegeService.getInstance().getSiegeTime());
		writeF(buf, inf.getElyos());
		writeF(buf, inf.getAsmos());
		writeF(buf, inf.getBalaur());

		writeH(buf, 3);
		for(int i=0; i<3; i++)
		{
			switch(i)
			{
				case 0:
					writeD(buf, 210050000);
					writeF(buf, inf.getElyosInggison());
					writeF(buf, inf.getAsmosInggison());
					writeF(buf, inf.getBalaurInggison());
					break;
				case 1:
					writeD(buf, 220070000);
					writeF(buf, inf.getElyosGelkmaros());
					writeF(buf, inf.getAsmosGelkmaros());
					writeF(buf, inf.getBalaurGelkmaros());
					break;
				case 2:
					writeD(buf, 400010000);
					writeF(buf, inf.getElyosAbyss());
					writeF(buf, inf.getAsmosAbyss());
					writeF(buf, inf.getBalaurAbyss());
					break;
			}
		}
	}
}