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
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionConnection.State;
import gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import gameserver.network.loginserver.LoginServer;
import gameserver.services.PlayerService;

/**
 * In this packets aion client is asking if may quit.
 */
public class CM_QUIT extends AionClientPacket
{
	/**
	 * Logout - if true player is wanted to go to character selection.
	 */
	private boolean	logout;
	
	/**
	 * Constructs new instance of <tt>CM_QUIT </tt> packet
	 * @param opcode
	 */
	public CM_QUIT(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		logout = readC() == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();
		
		Player player = null;
		if(client.getState() == State.IN_GAME)
		{
			player = client.getActivePlayer();
			// TODO! check if may quit
			if(!logout)
				LoginServer.getInstance().aionClientDisconnected(client.getAccount().getId());

			PlayerService.playerLoggedOut(player);

			/**
			 * clear active player.
			 */
			client.setActivePlayer(null);
		}

		if(logout)
		{
			if (player != null && player.isInEditMode())
			{
                sendPacket(new SM_QUIT_RESPONSE(true));
                player.setEditMode(false);
			}
			else
				sendPacket(new SM_QUIT_RESPONSE());
		}
		else
		{
			client.close(new SM_QUIT_RESPONSE(), true);
		}
	}
}