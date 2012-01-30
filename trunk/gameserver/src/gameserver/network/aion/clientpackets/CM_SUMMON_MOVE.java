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

import gameserver.controllers.movement.MovementType;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_MOVE;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_SUMMON_MOVE extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_SUMMON_MOVE.class);
	private MovementType type;
	private byte heading;
	private byte movementType;
	private float x, y, z, x2, y2, z2;

	/**
	 * Constructs new instance of <tt>CM_MOVE </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_SUMMON_MOVE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		Player player = getConnection().getActivePlayer();

		if(!player.isSpawned())
			return;

		readD();//object id

		x = readF();
		y = readF();
		z = readF();

		heading = (byte) readC();
		movementType = (byte) readC();
		type = MovementType.getMovementTypeById(movementType);

		switch(type)
		{
			case MOVEMENT_START_MOUSE:
			case MOVEMENT_START_KEYBOARD:
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
		{
			log.error("CM_SUMMON_MOVE packet received but cannot get master player.");
			return;
		}
		Summon summon = player.getSummon();
		if(summon == null)
			return;
		// packet was not read correctly
		if(type == null)
			return;

		switch(type)
		{
			case MOVEMENT_START_MOUSE:
			case MOVEMENT_START_KEYBOARD:
				World.getInstance().updatePosition(summon, x, y, z, heading);
				PacketSendUtility.broadcastPacket(summon, new SM_MOVE(summon.getObjectId(), x, y, z, x2, y2, z2, heading, type));
			break;
			case VALIDATE_MOUSE:
			case VALIDATE_KEYBOARD:
				PacketSendUtility.broadcastPacket(summon, new SM_MOVE(summon.getObjectId(), x, y, z, x2, y2, z2, heading,
					(type == MovementType.VALIDATE_MOUSE) ? MovementType.MOVEMENT_START_MOUSE
						: MovementType.MOVEMENT_START_KEYBOARD));
			break;
			case MOVEMENT_STOP:
				PacketSendUtility.broadcastPacket(summon, new SM_MOVE(summon.getObjectId(), x, y, z, heading, type));
				World.getInstance().updatePosition(summon, x, y, z, heading);
			break;
			case UNKNOWN:
				StringBuilder sb = new StringBuilder();
				sb.append("Unknown movement type: ").append(movementType);
				sb.append("Coordinates: X=").append(x);
				sb.append(" Y=").append(y);
				sb.append(" Z=").append(z);
				sb.append(" player=").append(player.getName());
				log.warn(sb.toString());
			break;
			default:
			break;
		}
	}
}
