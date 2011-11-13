/**
 * This file is part of Aion-Knight <Aion-Knight.smfnew.com>.
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


import gameserver.model.gameobjects.player.ToyPet;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;


/**
 * @author Sylar 
 */
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
