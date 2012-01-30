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

import gameserver.configs.main.CustomConfig;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_PONG;
import org.apache.log4j.Logger;

/**
 * I have no idea wtf is this
 */
public class CM_PING extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_PING.class);
	private static boolean firstPing = true;

	/**
	 * Constructs new instance of <tt>CM_PING </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_PING(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		long lastMS = getConnection().getLastPingTimeMS();
		
		if(lastMS > 0)
		{
			long pingInterval = System.currentTimeMillis() - lastMS;
			// PingInterval should be 3min (180000ms)
			if(pingInterval < CustomConfig.KICK_PINGINTERVAL)// client timer cheat
			{
				String ip = getConnection().getIP();
				//String ip = getConnection().getSource();
				String name = "[unknown]";
				if(getConnection().getActivePlayer() != null)
					name = getConnection().getActivePlayer().getName();

				if(CustomConfig.KICK_SPEEDHACK)
				{
					if(!firstPing)
					{
					    log.info("[AUDIT] possible client timer cheat kicking player: " + pingInterval + " by " + name + ", ip=" + ip);
					    AionConnection connection = getConnection().getActivePlayer().getClientConnection();
					    if (connection != null)
					    	connection.close(true);
					    return;
					}
					firstPing = false;
				}else{				
					log.info("[AUDIT] possible client timer cheat: " + pingInterval + " by " + name + ", ip=" + ip);
				}
				
			}

		}
		getConnection().setLastPingTimeMS(System.currentTimeMillis());
		sendPacket(new SM_PONG());
	}
}
