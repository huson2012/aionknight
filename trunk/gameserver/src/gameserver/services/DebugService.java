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

package gameserver.services;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.AionConnection;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class DebugService
{
	private static final Logger	log = Logger.getLogger(DebugService.class);
	private static final int ANALYZE_PLAYERS_INTERVAL = 30 * 60 * 1000;
	public static final DebugService getInstance()
	{
		return SingletonHolder.instance;
	}

	private DebugService()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				analyzeWorldPlayers();
			}

		}, ANALYZE_PLAYERS_INTERVAL, ANALYZE_PLAYERS_INTERVAL);
		log.info("[DEBUG SERVICE] Analyze iterval: "+ANALYZE_PLAYERS_INTERVAL);
	}

	private void analyzeWorldPlayers()
	{
		log.info("Analysis of world players: " + System.currentTimeMillis());

		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player player)
			{
				AionConnection connection = player.getClientConnection();
				if(connection == null)
				{
					log.warn(String.format("[DEBUG SERVICE] Player without connection: "
						+ "detected: ObjId %d, Name %s, Spawned %s", player.getObjectId(), player.getName(), player
						.isSpawned()));
					return true;
				}
				long lastPingTimeMS = connection.getLastPingTimeMS();
				long pingInterval = System.currentTimeMillis() - lastPingTimeMS;
				if(lastPingTimeMS > 0 && pingInterval > 300000)
				{
					log.warn(String.format("[DEBUG SERVICE] Player with large ping interval: "
						+ "ObjId %d, Name %s, Spawned %s, PingMS %d", player.getObjectId(), player.getName(), player
						.isSpawned(), pingInterval));
				}
				return true;
			}
		}, true);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DebugService instance = new DebugService();
	}
}
