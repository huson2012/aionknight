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

package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.ToyPet;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PET_MOVE;
import gameserver.utils.PacketSendUtility;

public class CM_PET_MOVE extends AionClientPacket
{
	
	private int actionId;
	
	private float x1;
	private float y1;
	private float z1;
	
	private int h;
	
	private float x2;
	private float y2;
	private float z2;
	
	public CM_PET_MOVE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		actionId = readC();
		switch(actionId)
		{
			case 0:
				x1 = readF();
				y1 = readF();
				z1 = readF();
				h = readC();
				break;
			case 12:
				x1 = readF();
				y1 = readF();
				z1 = readF();
				h = readC();
				x2 = readF();
				y2 = readF();
				z2 = readF();
				break;
			default:
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player == null)
			return;
		ToyPet pet = player.getToyPet();
		if (pet == null)
			return;
		
		switch(actionId)
		{
			case 0:
				pet.setX1(x1);
				pet.setY1(y1);
				pet.setZ1(z1);
				pet.setX2(x1);
				pet.setY2(y1);
				pet.setZ2(z1);
				pet.setH(h);
			case 12:
				pet.setX1(x1);
				pet.setY1(y1);
				pet.setZ1(z1);
				pet.setX2(x2);
				pet.setY2(y2);
				pet.setZ2(z2);
				pet.setH(h);
			default:
				PacketSendUtility.broadcastPacket(player, new SM_PET_MOVE(actionId, pet), true);
				break;
		}
	}
}