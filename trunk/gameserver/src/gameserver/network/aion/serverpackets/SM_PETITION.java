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

import gameserver.model.Petition;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.PetitionService;
import java.nio.ByteBuffer;

public class SM_PETITION extends AionServerPacket
{
	private Petition petition;
	
    public SM_PETITION()
    {
        this.petition = null;
    }
    
    public SM_PETITION(Petition petition)
    {
    	this.petition = petition;
    }

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(petition == null)
		{
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeH(buf, 0x00);
			writeC(buf, 0x00);
		}
		else
		{
			writeC(buf, 0x01); // Action ID ?
			writeD(buf, 100); // unk (total online players ?)
			writeH(buf, PetitionService.getInstance().getWaitingPlayers(con.getActivePlayer().getObjectId())); // Users waiting for Support
			writeS(buf, Integer.toString(petition.getPetitionId())); // Ticket ID
			writeH(buf, 0x00);
			writeC(buf, 50); // Total Petitions
			writeC(buf, 49); // Remaining Petitions
			writeH(buf, PetitionService.getInstance().calculateWaitTime(petition.getPlayerObjId())); // Estimated minutes before GM reply
			writeD(buf, 0x00);
		}
	}
}
