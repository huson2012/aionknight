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
import org.apache.log4j.Logger;

/**
 * Received when a player reports another player with /ReportAutoHunting
 */
public class CM_REPORT_PLAYER extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_REPORT_PLAYER.class);

	private String				player;

	/**
	 * A player gets reported.
	 * 
	 * @param opcode
	 */
	public CM_REPORT_PLAYER(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		readB(1); // unknown byte.
		player = readS(); // the name of the reported person.
	}

	@Override
	protected void runImpl()
	{
		Player p = getConnection().getActivePlayer();
		log.info("[AUDIT] " + p.getName() + " reports the player: " + player);
	}
}