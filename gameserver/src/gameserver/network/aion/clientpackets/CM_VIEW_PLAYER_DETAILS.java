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

import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_VIEW_PLAYER_DETAILS;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_VIEW_PLAYER_DETAILS extends AionClientPacket
{
	private static final Logger log = Logger.getLogger(CM_VIEW_PLAYER_DETAILS.class);
	private int targetObjectId;
	public CM_VIEW_PLAYER_DETAILS(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetObjectId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = World.getInstance().findPlayer(targetObjectId);
		if(player == null)
		{
			//probably targetObjectId can be 0
			log.warn("CHECKPOINT: can't show player details for " + targetObjectId);
			return;
		}

		AionConnection client = getConnection();

		if(client.getAccount().getAccessLevel() == 0)
		{
			if(player.getPlayerSettings().isInDeniedStatus(DeniedStatus.VEIW_DETAIL))
			{
				sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_WATCH(player.getName()));
				return;
			}
		}
		sendPacket(new SM_VIEW_PLAYER_DETAILS(targetObjectId, player.getEquipment().getEquippedItems()));
	}
}
