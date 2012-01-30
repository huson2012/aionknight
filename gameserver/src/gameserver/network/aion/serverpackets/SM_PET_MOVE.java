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

import gameserver.model.gameobjects.player.ToyPet;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_PET_MOVE extends AionServerPacket
{
	private int actionId;
	private ToyPet pet;
	
	public SM_PET_MOVE(int actionId, ToyPet pet)
	{
		this.actionId = actionId;
		this.pet = pet;
	}
	

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, pet.getUid());
		if (actionId != 0)
			writeC(buf, actionId);
		switch(actionId)
		{
			case 0:
				writeC(buf, 0);
				writeF(buf, pet.getX1());
				writeF(buf, pet.getY1());
				writeF(buf, pet.getZ1());
				writeC(buf, pet.getH());
			case 12:
				// move
				writeF(buf, pet.getX1());
				writeF(buf, pet.getY1());
				writeF(buf, pet.getZ1());
				writeC(buf, pet.getH());
				writeF(buf, pet.getX2());
				writeF(buf, pet.getY2());
				writeF(buf, pet.getZ2());
				break;
			default:
				break;					
		}
	}
}
