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
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.apache.log4j.Logger;

/**
 * Handler for "/loc" command
 */
public class CM_CLIENT_COMMAND_LOC extends AionClientPacket
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(CM_CLIENT_COMMAND_LOC.class);

	/**
	 * Constructs new client packet instance.
	 * @param opcode
	 */
	public CM_CLIENT_COMMAND_LOC(int opcode)
	{
		super(opcode);

	}

	/**
	 * Nothing to do
	 */
	@Override
	protected void readImpl()
	{
		// empty
	}

	/**
	 * Logging
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		log.info("Received \"/loc\" command");
		
		sendPacket(SM_SYSTEM_MESSAGE.CURRENT_LOCATION(player.getWorldId(), player.getX(), player.getY(), player.getZ()));
	}
}