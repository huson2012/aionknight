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
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_WINDSTREAM;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * Packet about player flying teleport movement.
 */
public class CM_FLIGHT_TELEPORT extends AionClientPacket
{
	float x, y, z;
	int distance;

	/**
	 * Constructs new instance of <tt>CM_FLIGHT_TELEPORT </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_FLIGHT_TELEPORT(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		readD(); // mapId
		x = readF();
		y = readF();
		z = readF();
		readC(); // locationId
		distance = readD();	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		if(player != null && player.getEnterWindstream() > 0)
		{
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(player.getEnterWindstream(),1));
			player.setEnterWindstream(0);
		}
		
		if(player != null && player.isInState(CreatureState.FLIGHT_TELEPORT))
		{
			player.setFlightDistance(distance);
			World.getInstance().updatePosition(player, x, y, z, (byte)0);
		}
	}
}