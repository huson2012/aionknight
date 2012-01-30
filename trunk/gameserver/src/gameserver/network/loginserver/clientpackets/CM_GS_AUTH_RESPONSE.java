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

package gameserver.network.loginserver.clientpackets;

import commons.utils.ExitCode;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LoginServerConnection.State;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.serverpackets.SM_ACCOUNT_LIST;
import gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

/**
 * This packet is response for SM_GS_AUTH its notify Gameserver if registration was ok or what was wrong.
 */
public class CM_GS_AUTH_RESPONSE extends LsClientPacket
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger	log	= Logger.getLogger(CM_GS_AUTH_RESPONSE.class);

	/**
	 * Response: 0=Authed,1=NotAuthed,2=AlreadyRegistered
	 */
	private int						response;

	/**
	 * Constructs new instance of <tt>CM_GS_AUTH_RESPONSE </tt> packet.
	 * @param opcode
	 */
	public CM_GS_AUTH_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		response = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		/**
		 * Authed
		 */
		if(response == 0)
		{
			getConnection().setState(State.AUTHED);
			sendPacket(new SM_ACCOUNT_LIST(LoginServer.getInstance().getLoggedInAccounts()));
		}

		/**
		 * NotAuthed
		 */
		else if(response == 1)
		{
			log.fatal("GameServer is not authenticated at LoginServer side, shutting down!");
			System.exit(ExitCode.CODE_ERROR);
		}
		/**
		 * AlreadyRegistered
		 */
		else if(response == 2)
		{
			log.info("GameServer is already registered at LoginServer side! trying again...");
			/**
			 * try again after 10s
			 */
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					CM_GS_AUTH_RESPONSE.this.getConnection().sendPacket(new SM_GS_AUTH());
				}

			}, 10000);
		}
	}
}
