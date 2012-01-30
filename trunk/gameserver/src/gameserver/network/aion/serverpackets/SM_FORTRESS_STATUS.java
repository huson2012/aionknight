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
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;
import javolution.util.FastList;
import java.nio.ByteBuffer;

public class SM_FORTRESS_STATUS extends AionServerPacket
{
	public SM_FORTRESS_STATUS()
	{
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		FastList<SiegeLocation> validLocations = new FastList<SiegeLocation>();
		
		for(SiegeLocation loc : SiegeService.getInstance().getSiegeLocations().values())
		{
			if(loc.getSiegeType() == SiegeType.FORTRESS)
			{
				validLocations.add(loc);
			}
		}
		
		writeC(buf, 1); //unk
		writeD(buf, SiegeService.getInstance().getSiegeTime());
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeH(buf, 3); //map count
		
		writeD(buf, 210050000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 220070000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 400010000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		
		writeH(buf, validLocations.size());
		
		for(SiegeLocation loc : validLocations)
		{
			writeD(buf, loc.getLocationId());
			writeC(buf, 1); //unk
		}
	}
}